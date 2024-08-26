/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.config.enums.business.PackPricingOtherCostsItemType;
import com.base.sbc.module.pack.dto.OtherCostsPageDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingOtherCostsDto;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.entity.PackPricingOtherCostsGst;
import com.base.sbc.module.pack.vo.PackPricingOtherCostsVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包-核价信息-其他费用 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingOtherCostsService
 * @email your email
 * @date 创建时间：2023-7-10 13:35:18
 */
public interface PackPricingOtherCostsGstService extends PackBaseService<PackPricingOtherCostsGst> {


// 自定义方法区 不替换的区域【other_start】

    PageInfo<PackPricingOtherCostsVo> pageInfo(OtherCostsPageDto dto);

// 自定义方法区 不替换的区域【other_end】


}
