/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.style.dto.StyleInfoSkuDto;
import com.base.sbc.module.style.entity.StyleInfoSku;

/** 
 * 类描述：款式设计SKU表 service类
 * @address com.base.sbc.module.style.service.StyleInfoSkuService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:34
 * @version 1.0  
 */
public interface StyleInfoSkuService extends BaseService<StyleInfoSku>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 根据id修改款式设计SKU
     * @param styleInfoSkuDto 款式设计SKU DTO
     */
    void updateStyleInfoSkuById(StyleInfoSkuDto styleInfoSkuDto);


// 自定义方法区 不替换的区域【other_end】

	
}
