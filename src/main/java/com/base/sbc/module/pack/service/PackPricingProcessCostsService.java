/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingProcessCostsDto;
import com.base.sbc.module.pack.entity.PackPricingProcessCosts;
import com.base.sbc.module.pack.vo.PackPricingProcessCostsVo;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：资料包-加工费 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackPricingProcessCostsService
 * @email your email
 * @date 创建时间：2023-7-10 16:09:56
 */
public interface PackPricingProcessCostsService extends PackBaseService<PackPricingProcessCosts> {

// 自定义方法区 不替换的区域【other_start】

    PackPricingProcessCostsVo saveByDto(PackPricingProcessCostsDto dto);

    PageInfo<PackPricingProcessCostsVo> pageInfo(PackCommonPageSearchDto dto);

    BigDecimal calculateCosts(PackCommonSearchDto dto);

    /**
     * 批量添加
     * @param dto
     */
    void batchInsertProcessCosts(List<PackPricingProcessCostsDto> dto);



// 自定义方法区 不替换的区域【other_end】


}
