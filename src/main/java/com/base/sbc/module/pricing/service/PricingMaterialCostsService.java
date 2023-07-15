/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pricing.dto.PricingMaterialCostsDTO;
import com.base.sbc.module.pricing.entity.PricingMaterialCosts;
import com.base.sbc.module.pricing.vo.PricingMaterialCostsVO;

import java.util.List;
import java.util.Map;

/**
 * 类描述：物料费用 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingMaterialCostsService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:25
 */
public interface PricingMaterialCostsService extends BaseService<PricingMaterialCosts> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过核价编码获取
     *
     * @param pricingCode
     * @param userCompany
     * @return
     */
    Map<String, List<PricingMaterialCostsVO>> getByPricingCode(String pricingCode, String userCompany);

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
     * @param pricingMaterialCostsDTOS
     * @param pricingCode
     * @param userCompany
     */
    void insert(List<PricingMaterialCostsDTO> pricingMaterialCostsDTOS, Map<String, String> pricingColorMap,
                String pricingCode, String userCompany);


// 自定义方法区 不替换的区域【other_end】


}
