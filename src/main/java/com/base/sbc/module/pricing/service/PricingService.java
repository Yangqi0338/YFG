/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service;

import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.pricing.dto.PricingDTO;
import com.base.sbc.module.pricing.dto.PricingDelDTO;
import com.base.sbc.module.pricing.dto.PricingSearchDTO;
import com.base.sbc.module.pricing.entity.Pricing;
import com.base.sbc.module.pricing.vo.PricingListVO;
import com.base.sbc.module.pricing.vo.PricingVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：核价表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
public interface PricingService extends IServicePlus<Pricing> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询
     *
     * @param pricingSearchDTO
     * @param userCompany
     * @return
     */
    PageInfo<PricingListVO> queryPageInfo(PricingSearchDTO pricingSearchDTO, String userCompany);

    /**
     * 新增或修改
     *
     * @param pricingDTO
     * @param userCompany
     * @return
     */
    String save(PricingDTO pricingDTO, String userCompany);

    /**
     * 批量删除
     *
     * @param pricingDelDTO
     * @param userCompany
     */
    void delByIds(PricingDelDTO pricingDelDTO, String userCompany);


    /**
     * 详情
     *
     * @param id
     * @param userCompany
     * @return
     */
    PricingVO getDetailsById(String id, String userCompany);

    /**
     * 提交审核
     *
     * @param id
     * @param userCompany
     */
    void submitApprove(String id, String userCompany);

    // TODO 反审核

    // TODO 费用计算

    // TODO
    // TODO


// 自定义方法区 不替换的区域【other_end】


}

