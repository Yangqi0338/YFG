/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.constant.DictBusinessConstant;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.config.exception.BusinessException;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.exception.RightException;
import com.base.sbc.config.redis.RedisKeyBuilder;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.CountryTypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.LanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.TypeLanguageSaveDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.mapper.CountryLanguageMapper;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;

/**
 * 类描述：国家地区表 service类
 * @address com.base.sbc.module.moreLanguage.service.CountryService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:37
 * @version 1.0  
 */
@Service
public class CountryLanguageServiceImpl extends BaseServiceImpl<CountryLanguageMapper, CountryLanguage> implements CountryLanguageService {

    @Autowired
    private StandardColumnCountryRelationService relationService;

    @Autowired
    private StandardColumnService standardColumnService;

    @Autowired
    private CcmFeignService ccmFeignService;

    private final ReentrantLock saveLock = new ReentrantLock();

    @Override
    public List<CountryLanguageDto> listQuery(CountryQueryDto countryQueryDto) {
        return this.list(new BaseLambdaQueryWrapper<CountryLanguage>()
                        .notEmptyEq(CountryLanguage::getCountryCode, countryQueryDto.getCountryCode())
                        .notEmptyEq(CountryLanguage::getLanguageCode, countryQueryDto.getLanguageCode())
                        .notEmptyIn(CountryLanguage::getCountryName, countryQueryDto.getCountryName())
                        .notEmptyIn(CountryLanguage::getLanguageName, countryQueryDto.getLanguageName())
                        .notEmptyEq(CountryLanguage::getEnableFlag, countryQueryDto.getEnableFlag())
                        .orderByAsc(Arrays.asList(CountryLanguage::getCodeIndex,CountryLanguage::getId))
                ).stream().map(list-> BeanUtil.copyProperties(list, CountryLanguageDto.class))
//                .peek(it-> it.setCountryLanguageCode(it.getCode() < 10 ? ("GY" + "0" + it.getCode()) : ("GY" + it.getCode())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto, boolean cache) {

        String countryName = countryTypeLanguageSaveDto.getCountryName();
        String countryCode = countryTypeLanguageSaveDto.getCountryCode();

        // 插入国家表
        CountryLanguage baseCountryLanguage = new CountryLanguage();

        baseCountryLanguage.setCountryCode(countryCode);
        baseCountryLanguage.setCountryName(countryName);
        baseCountryLanguage.setEnableFlag(YesOrNoEnum.YES);

        String code;
        boolean isNew = countryTypeLanguageSaveDto.getCode() == null;
        if (isNew) {
            Integer maxCountryLanguageCode = this.findOneField(new BaseLambdaQueryWrapper<CountryLanguage>()
                    .orderByDesc(CountryLanguage::getCodeIndex), CountryLanguage::getCodeIndex);
            int codeIndex = maxCountryLanguageCode + 1;
            baseCountryLanguage.setCodeIndex(codeIndex);
            String fill = codeIndex < 10 ? "0" : "";
            code = "GY" + fill + codeIndex;
        }else {
            code = countryTypeLanguageSaveDto.getCode();
        }
        baseCountryLanguage.setCode(code);

        List<String> countryIdList = new ArrayList<>();

        // 查名字
        List<BasicBaseDict> dictInfoToList = ccmFeignService.getDictInfoToList(DictBusinessConstant.LANGUAGE);

        saveLock.lock();
        try {
            countryTypeLanguageSaveDto.getTypeLanguage().stream().sorted(Comparator.comparing(it-> it.getType().ordinal())).forEach(typeLanguageSaveDto-> {
                CountryLanguageType type = typeLanguageSaveDto.getType();
                List<String> standardColumnCodeList = typeLanguageSaveDto.getStandardColumnCodeList();
                List<String> languageCodeList = typeLanguageSaveDto.getLanguageCodeList();

                CountryLanguage countryTypeLanguage = BeanUtil.copyProperties(baseCountryLanguage, CountryLanguage.class);
                countryTypeLanguage.setType(type);

                LambdaQueryWrapper<CountryLanguage> queryWrapper = new LambdaQueryWrapper<CountryLanguage>()
                        .eq(CountryLanguage::getCode, code)
                        .eq(CountryLanguage::getType, type);

                // 检查是否已有国家表
                if (isNew && this.exists(queryWrapper)) {
                    throw new OtherException("已存在对应国家");
                }

                if (CollectionUtil.isNotEmpty(languageCodeList)) {
                    List<CountryLanguage> oldCountryLanguageList = this.list(queryWrapper.clone().in(CountryLanguage::getLanguageCode, languageCodeList));

                    this.remove(queryWrapper.clone().notIn(CountryLanguage::getLanguageCode, languageCodeList));

                    // 查找根
                    standardColumnCodeList.add(standardColumnService.findOneField(new LambdaQueryWrapper<StandardColumn>()
                            .eq(StandardColumn::getType, type.getStandardColumnType()), StandardColumn::getCode));

                    for (String languageCode : languageCodeList) {
                        CountryLanguage oldCountryLanguage = oldCountryLanguageList.stream().filter(it -> languageCode.equals(it.getLanguageCode())).findFirst().orElse(null);

                        if (oldCountryLanguage == null) {
                            oldCountryLanguage = BeanUtil.copyProperties(countryTypeLanguage, CountryLanguage.class);
                            oldCountryLanguage.setLanguageCode(languageCode);
                            oldCountryLanguage.setLanguageName(dictInfoToList.stream().filter(dict-> dict.getValue().equals(languageCode)).findFirst().map(BasicBaseDict::getName).orElse(null));
                            this.save(oldCountryLanguage);
                        }

                        String countryId = oldCountryLanguage.getId();

                        removeRelation(countryId);

                        List<StandardColumnCountryRelation> countryRelationList = standardColumnCodeList.stream().map(standardColumnCode -> {
                            // 从redis标准列数据
                            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
                            StandardColumn standardColumn = (StandardColumn)
                                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
                            return new StandardColumnCountryRelation(
                                    countryId,
                                    standardColumn.getCode(),
                                    standardColumn.getName(),
                                    ""
                            );
                        }).collect(Collectors.toList());

                        relationService.saveBatch(countryRelationList);

                        // 关联关系存到redis
                        countryIdList.add(countryId);
                    }

                    standardColumnCodeList.forEach(it-> RedisStaticFunUtils.lSet(RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode()),it));
                }
            });
        }finally {
            saveLock.unlock();
        }
        if (!cache) {
            RedisStaticFunUtils.removePattern(RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code));
        }
//        // 使用redis作为中间通信,standardColumnCodeList参数暂时没用了
//        exportExcel(new MoreLanguageExcelQueryDto(countryId, new ArrayList<>()));
        RedisStaticFunUtils.clear();
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String review(CountryTypeLanguageSaveDto countryTypeLanguageSaveDto) {
        //  复用增加
        String code = save(countryTypeLanguageSaveDto, true);
        // 强行报错回滚,让controller进行捕捉
        throw new RightException(code);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void removeRelation(String countryId){
        // 直接删除关联
        relationService.physicalDeleteQWrap(new BaseQueryWrapper<StandardColumnCountryRelation>().eq("country_language_id", countryId));
    }

    @Override
    public CountryTypeLanguageSaveDto detail(String code, boolean cache) {
        List<CountryLanguage> countryLanguageList = this.list(new LambdaQueryWrapper<CountryLanguage>().eq(CountryLanguage::getCode, code));
        if (CollectionUtil.isEmpty(countryLanguageList)) throw new OtherException("未查询到国家语言");

        CountryLanguage baseCountryLanguage = countryLanguageList.get(0);
        CountryTypeLanguageSaveDto countryTypeLanguageSaveDto = BeanUtil.copyProperties(baseCountryLanguage, CountryTypeLanguageSaveDto.class);

        countryTypeLanguageSaveDto.setTypeLanguage(Arrays.stream(CountryLanguageType.values()).map(type-> {
            TypeLanguageSaveDto typeLanguageSaveDto = new TypeLanguageSaveDto();
            typeLanguageSaveDto.setType(type);
            typeLanguageSaveDto.setStandardColumnCodeList(this.findStandardColumnCodeList(code, type, cache));

            List<CountryLanguage> sameTypeCountryLanguageList = countryLanguageList.stream().filter(it -> it.getType().equals(type)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sameTypeCountryLanguageList)) {
                typeLanguageSaveDto.setLanguageCodeList(sameTypeCountryLanguageList.stream().map(CountryLanguage::getLanguageCode).collect(Collectors.toList()));
            }
            return typeLanguageSaveDto;
        }).collect(Collectors.toList()));
        return countryTypeLanguageSaveDto;
    }

    @Override
    public List<StandardColumn> findStandardColumnList(String code, CountryLanguageType type, boolean cache) {
        // 查询关联关系
        List<StandardColumn> standardColumnList = findStandardColumnCodeList(code, type, cache).stream().map(standardColumnCode -> {
            // 从redis标准列数据
            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
            return (StandardColumn) RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST.build(), type.getCode() + RedisKeyBuilder.COMMA + standardColumnCode);
        }).collect(Collectors.toList());
        RedisStaticFunUtils.clear();
        return standardColumnList;
    }

    @Override
    public List<String> findStandardColumnCodeList(String code, CountryLanguageType type, boolean cache) {
        // 查询关联关系
        String redisKey = RedisKeyConstant.STANDARD_COLUMN_COUNTRY_RELATION.addEnd(cache, code, type.getCode());
        List<Object> tempStandardColumnCodeList = RedisStaticFunUtils.lGet(redisKey);
        if (CollectionUtil.isEmpty(tempStandardColumnCodeList)) {
            if (!NumberUtil.isNumber(code)) {
                code = Integer.parseInt(code.replace("GY","")) + "";
            }
            String countryLanguageId = this.findOneField(new LambdaQueryWrapper<CountryLanguage>()
                    .eq(CountryLanguage::getCode, code)
                    .eq(CountryLanguage::getType, type), CountryLanguage::getId
            );
            if (StrUtil.isBlank(countryLanguageId)) return new ArrayList<>();
            List<String> standardColumnCodeList = relationService.listOneField(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                    .eq(StandardColumnCountryRelation::getCountryLanguageId, countryLanguageId), StandardColumnCountryRelation::getStandardColumnCode
            );
            RedisStaticFunUtils.lSet(redisKey, standardColumnCodeList.stream().collect(Collectors.toList()));
            return standardColumnCodeList;
        } else {
            return tempStandardColumnCodeList.stream().map(Object::toString).collect(Collectors.toList());
        }
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
