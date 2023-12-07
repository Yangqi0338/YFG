/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;

import static com.base.sbc.config.constant.Constants.COMMA;

/** 
 * 类描述：品名多语言属性值配置表 service类
 * @address com.base.sbc.module.moreLanguage.service.StandardColumnCountryTranslateService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:41
 * @version 1.0  
 */
public interface StandardColumnCountryTranslateService extends BaseService<StandardColumnCountryTranslate>{

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】


    @Override
    default StandardColumnCountryTranslate findByCode(String code) {
        String[] codeList = code.split(COMMA);
        if (codeList.length < 3) throw new OtherException("缺少唯一组成");

        return this.findOne(new BaseLambdaQueryWrapper<StandardColumnCountryTranslate>()
                .notEmptyIn(StandardColumnCountryTranslate::getPropertiesCode, codeList[2])
                .eq(StandardColumnCountryTranslate::getCountryLanguageId, codeList[0])
                .eq(StandardColumnCountryTranslate::getTitleCode, codeList[1])
        );
    }
}
