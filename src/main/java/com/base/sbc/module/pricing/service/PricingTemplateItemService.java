/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pricing.dto.PricingTemplateItemDTO;
import com.base.sbc.module.pricing.entity.PricingTemplateItem;
import com.base.sbc.module.pricing.vo.PricingTemplateItemVO;

import java.util.List;

/**
 * 类描述：核价模板明细表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingTemplateItemService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:36
 */
public interface PricingTemplateItemService extends BaseService<PricingTemplateItem> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过模板id获取
     *
     * @param pricingTemplateId
     * @param userCompany
     * @return
     */
    List<PricingTemplateItemVO> getByPricingTemplateId(String pricingTemplateId, String userCompany);

    /**
     * 通过模板id获取显示及有排序的字段
     *
     * @param pricingTemplateId
     * @return
     */
    List<PricingTemplateItemVO> getByPricingTemplateId(String pricingTemplateId);
    /**
     * 删除
     *
     * @param pricingTemplateId
     * @param userCompany
     */
    void delByPricingTemplateId(String pricingTemplateId, String userCompany);

    /**
     * 批量保存
     *  @param pricingTemplateItems
     * @param pricingTemplateId
     * @param userCompany
     */
    void saveBatch(List<PricingTemplateItemDTO> pricingTemplateItems, String pricingTemplateId, String userCompany);


// 自定义方法区 不替换的区域【other_end】


}
