/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.moreLanguage.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryRelation;

/** 
 * 类描述：吊牌列与国家地区关联表 service类
 * @address com.base.sbc.module.moreLanguage.service.StandardColumnCountryRelationService
 * @author KC
 * @email KC
 * @date 创建时间：2023-11-30 15:07:53
 * @version 1.0  
 */
public interface StandardColumnCountryRelationService extends BaseService<StandardColumnCountryRelation>{

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】


    @Override
    default StandardColumnCountryRelation findByCode(String code) {
        return null;
    }
}
