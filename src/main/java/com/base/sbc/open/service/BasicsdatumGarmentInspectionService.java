/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.open.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.open.dto.BasicsdatumGarmentInspectionDto;
import com.base.sbc.open.entity.BasicsdatumGarmentInspection;

/** 
 * 类描述：成衣成分送检 service类
 * @address com.base.sbc.open.service.BasicsdatumGarmentInspectionService
 * @author your name
 * @email your email
 * @date 创建时间：2024-1-17 15:18:54
 * @version 1.0  
 */
public interface BasicsdatumGarmentInspectionService extends BaseService<BasicsdatumGarmentInspection>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 接收成衣送检数据
     * @param garmentInspectionDto
     */
    void saveGarmentInspection(BasicsdatumGarmentInspectionDto garmentInspectionDto);

// 自定义方法区 不替换的区域【other_end】

	
}
