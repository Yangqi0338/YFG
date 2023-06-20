/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.pricing.dto.PricingDelDTO;
import com.base.sbc.module.pricing.entity.PricingMaterialCosts;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：物料费用 dao类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.dao.PricingMaterialCostsDao
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:25
 */
@Mapper
public interface PricingMaterialCostsMapper extends BaseMapper<PricingMaterialCosts> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过核价单号查询
     *
     * @param pricingCode
     * @param userCompany
     * @return
     */
    List<PricingMaterialCostsVO> getByPricingCode(@Param("pricingCode") String pricingCode, @Param("userCompany") String userCompany);


// 自定义方法区 不替换的区域【other_end】
}