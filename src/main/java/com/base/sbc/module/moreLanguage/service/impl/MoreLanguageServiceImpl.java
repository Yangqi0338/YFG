/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.module.moreLanguage.dto.CountryAddDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageQueryDto;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;
import com.base.sbc.module.moreLanguage.service.CountryLanguageConfigService;
import com.base.sbc.module.moreLanguage.service.CountryModelService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService;
import com.base.sbc.module.standard.dto.StandardColumnDto;
import com.base.sbc.module.standard.dto.StandardColumnQueryDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.module.standard.service.StandardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    private CountryModelService countryModelService;

    @Autowired
    private CountryLanguageConfigService countryLanguageConfigService;

    @Autowired
    private StandardColumnCountryRelationService relationService;

    @Autowired
    private StandardColumnService standardColumnService;

// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<StandardColumnDto> queryCountryTitle(MoreLanguageQueryDto moreLanguageQueryDto) {
        StandardColumnQueryDto queryDto = new StandardColumnQueryDto();
        // 只查询可配置的
        queryDto.setNoModel(StandardColumnModel.TEXT);
        List<StandardColumnDto> standardColumnList = standardColumnService.listQuery(queryDto);

        // 父层包含关系修改
        return standardColumnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean countryAdd(CountryAddDto countryAddDto) {
        String countryCode = countryAddDto.getCountryCode();
        String languageCode = countryAddDto.getLanguageCode();

        // 字典校验 非必要 TODO

        // 检查是否已存在关联
        if (relationService.exists(new BaseLambdaQueryWrapper<StandardColumnCountryRelation>()
                .eq(StandardColumnCountryRelation::getCountryCode, countryCode)
                .eq(StandardColumnCountryRelation::getLanguageCode, languageCode))) {
            throw new OtherException("已存在对应国家-语言, 修改数据请使用对应的Excel导入");
        }


        List<StandardColumnCountryRelation> countryRelationList = countryAddDto.getStandardColumnCodeList().stream().map(standardColumnCode -> {
            // 从redis标准列数据
            RedisStaticFunUtils.setBusinessService(standardColumnService).setMessage("非法标准列code");
            StandardColumn standardColumn = (StandardColumn)
                    RedisStaticFunUtils.hget(RedisKeyConstant.STANDARD_COLUMN_LIST, standardColumnCode);
            return new StandardColumnCountryRelation(
                    countryCode,
                    languageCode,
                    standardColumn.getCode(),
                    standardColumn.getName(),
                    ""
            );
        }).collect(Collectors.toList());
        relationService.saveBatch(countryRelationList);
        return true;
    }

// 自定义方法区 不替换的区域【other_end】
	
}
