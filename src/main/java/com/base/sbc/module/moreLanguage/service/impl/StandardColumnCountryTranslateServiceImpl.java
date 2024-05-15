/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.mapper.StandardColumnCountryTranslateMapper;
import com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 类描述：品名多语言属性值配置表 service类
 * @address com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:41
 * @version 1.0  
 */
@Service
public class StandardColumnCountryTranslateServiceImpl extends BaseServiceImpl<StandardColumnCountryTranslateMapper, StandardColumnCountryTranslate> implements StandardColumnCountryTranslateService {

// 自定义方法区 不替换的区域【other_start】

    public static final SFunction<StandardColumnCountryTranslate, String> countryLanguageIdFunc = StandardColumnCountryTranslate::getCountryLanguageId;
    public static final SFunction<StandardColumnCountryTranslate, String> contentFunc = StandardColumnCountryTranslate::getContent;
    public static final SFunction<StandardColumnCountryTranslate, String> titleCodeFunc = StandardColumnCountryTranslate::getTitleCode;
    public static final SFunction<StandardColumnCountryTranslate, String> titleNameFunc = StandardColumnCountryTranslate::getTitleName;
    public static final SFunction<StandardColumnCountryTranslate, String> propertiesCodeFunc = StandardColumnCountryTranslate::getPropertiesCode;
    public static final SFunction<StandardColumnCountryTranslate, String> propertiesNameFunc = StandardColumnCountryTranslate::getPropertiesName;
    public static final SFunction<StandardColumnCountryTranslate, Date> updateDateFunc = StandardColumnCountryTranslate::getUpdateDate;
    public static final SFunction<StandardColumnCountryTranslate, String> idFunc = StandardColumnCountryTranslate::getId;

// 自定义方法区 不替换的区域【other_end】`
	
}
