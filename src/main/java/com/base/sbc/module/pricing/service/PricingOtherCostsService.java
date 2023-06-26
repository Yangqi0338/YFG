/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pricing.dto.PricingOtherCostsDTO;
import com.base.sbc.module.pricing.entity.PricingOtherCosts;
import com.base.sbc.module.pricing.vo.PricingOtherCostsVO;

import java.util.List;

/**
 * 类描述：其他费用 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingOtherCostsService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:28
 */
public interface PricingOtherCostsService extends BaseService<PricingOtherCosts> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过核价单号获取
     *
     * @param pricingCode
     * @param userCompany
     * @return
     */
    List<PricingOtherCostsVO> getByPricingCode(String pricingCode, String userCompany);

    /**
     * 通过核价单号删除
     *
     * @param pricingCode
     * @param userCompany
     */
    void delByPricingCode(String pricingCode, String userCompany);

    /**
     * 新增
     *
     * @param pricingOtherCostsDTOS
     * @param pricingCode
     * @param userCompany
     */
    void insert(List<PricingOtherCostsDTO> pricingOtherCostsDTOS, String pricingCode, String userCompany);

// 自定义方法区 不替换的区域【other_end】


}
