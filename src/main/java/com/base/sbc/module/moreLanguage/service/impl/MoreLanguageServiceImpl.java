/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.utils.Pinyin4jUtil;
import com.base.sbc.module.moreLanguage.dto.CountryAddDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.entity.Country;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.CountryService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类描述：吊牌列头翻译表 service类
 * @address com.base.sbc.module.moreLanguage.service.TagTranslateService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:58
 * @version 1.0  
 */
@Service
public class MoreLanguageServiceImpl implements MoreLanguageService {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryModelService countryModelService;

    @Autowired
    private StandardColumnCountryTranslateService standardColumnCountryTranslateService;

    @Autowired
    private StandardColumnCountryRelationService relationService;

    @Autowired
    private StandardColumnService standardColumnService;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<StandardColumnDto> queryCountryTitle(MoreLanguageQueryDto moreLanguageQueryDto) {
        String countryLanguageId = moreLanguageQueryDto.getCountryLanguageId();

        ForkJoinPool workPool = new ForkJoinPool(2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

        /* ----------------------------可以分成两个线程进行操作---------------------------- */

        // 查询根标准列
        ForkJoinTask<List<StandardColumnDto>> rootStandardColumnFuture = workPool.submit(() -> {
            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
            queryDto.setTypeList(CollUtil.toList(StandardColumnType.TAG_ROOT));
            return standardColumnService.listQuery(queryDto);
        }).fork();

        ForkJoinTask<List<StandardColumnDto>> standardColumnFuture = workPool.submit(() -> {
            // 查询 关联表
            // 可以从redis拿,只有countryAdd和导入会更新,频率低
            String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION + countryLanguageId;
            List<Object> tempStandardColumnCodeList = RedisStaticFunUtils.lGet(redisKey);
            List<String> standardColumnCodeList;
            if (CollectionUtil.isEmpty(tempStandardColumnCodeList)) {
                List<StandardColumnCountryRelation> relationList = relationService.list(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                        .eq(StandardColumnCountryRelation::getCountryLanguageId, countryLanguageId)
                );
                standardColumnCodeList = relationList.stream().map(StandardColumnCountryRelation::getStandardColumnCode).collect(Collectors.toList());
                RedisStaticFunUtils.lSet(redisKey, standardColumnCodeList);
            } else {
                standardColumnCodeList = tempStandardColumnCodeList.stream().map(Object::toString).collect(Collectors.toList());
            }

            // 只查询可配置的
            StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
            queryDto.setNoModel(StandardColumnModel.TEXT);
            queryDto.setCodeList(standardColumnCodeList);
            return standardColumnService.listQuery(queryDto);
        }).fork();

        return Stream.of(rootStandardColumnFuture.join(), standardColumnFuture.join()).flatMap(Collection::stream)
                .sorted(Comparator.comparing(StandardColumnDto::getId)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String countryAdd(CountryAddDto countryAddDto) {
        String countryName = countryAddDto.getCountryName();
        String languageName = countryAddDto.getLanguageName();
        String countryCode = Pinyin4jUtil.converterToFirstSpell(countryName);
        String languageCode = Pinyin4jUtil.converterToFirstSpell(languageName);

        // 字典校验 非必要 TODO

        // 检查是否已有国家表
        if (countryService.exists(new BaseLambdaQueryWrapper<Country>()
                .eq(Country::getCountryCode, countryCode)
                .eq(Country::getLanguageCode, languageCode))) {
            throw new OtherException("已存在对应国家-语言, 修改数据请使用对应的Excel导入");
        }

        // 插入国家表
        Country country = new Country();
        country.setCountryCode(countryCode);
        country.setCountryName(countryName);
        country.setLanguageCode(languageCode);
        country.setLanguageName(languageName);

        countryService.save(country);

        String countryId = country.getId();

        List<StandardColumnCountryRelation> countryRelationList = countryAddDto.getStandardColumnCodeList().stream().map(standardColumnCode -> {
            // 从redis标准列数据
            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
            StandardColumn standardColumn = (StandardColumn)
                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST, standardColumnCode);
            return new StandardColumnCountryRelation(
                    countryId,
                    standardColumn.getCode(),
                    standardColumn.getName(),
                    ""
            );
        }).collect(Collectors.toList());
        relationService.saveBatch(countryRelationList);

        // 存到redis
        RedisStaticFunUtils.lSet(RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION + countryId,
                countryRelationList.stream().map(StandardColumnCountryRelation::getStandardColumnCode).collect(Collectors.toList()));
        return countryId;
    }

    @Override
    public Map<String, Object> listQuery(MoreLanguageQueryDto moreLanguageQueryDto) {
        return null;
    }

// 自定义方法区 不替换的区域【other_end】
	
}
