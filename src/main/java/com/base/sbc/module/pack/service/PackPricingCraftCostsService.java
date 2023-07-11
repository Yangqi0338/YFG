/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingCraftCostsDto;
import com.base.sbc.module.pack.entity.PackPricingCraftCosts;
import com.base.sbc.module.pack.vo.PackPricingCraftCostsVo;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;

/**
 * 类描述：资料包-二次加工费(工艺费用) service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingCraftCostsService
 * @email your email
 * @date 创建时间：2023-7-10 16:09:53
 */
public interface PackPricingCraftCostsService extends PackBaseService<PackPricingCraftCosts> {

// 自定义方法区 不替换的区域【other_start】

    PackPricingCraftCostsVo saveByDto(PackPricingCraftCostsDto dto);

    PageInfo<PackPricingCraftCostsVo> pageInfo(PackCommonPageSearchDto dto);

    BigDecimal calculateCosts(PackCommonSearchDto dto);


// 自定义方法区 不替换的区域【other_end】


}
