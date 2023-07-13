/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingDto;
import com.base.sbc.module.pack.entity.PackPricing;
import com.base.sbc.module.pack.vo.PackPricingVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 类描述：资料包-核价信息 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:16
 */
public interface PackPricingService extends PackBaseService<PackPricing> {

// 自定义方法区 不替换的区域【other_start】


    PackPricingVo getDetail(PackCommonSearchDto dto);

    Map<String, BigDecimal> calculateCosts(PackCommonSearchDto dto);

    BigDecimal formula(String formula, Map<String, Object> itemVal);

    PackPricingVo saveByDto(PackPricingDto dto);

// 自定义方法区 不替换的区域【other_end】


}
