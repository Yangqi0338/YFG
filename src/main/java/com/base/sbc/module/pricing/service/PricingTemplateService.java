/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pricing.dto.*;
import com.base.sbc.module.pricing.entity.PricingTemplate;
import com.base.sbc.module.pricing.vo.PricingTemplateItemVO;
import com.base.sbc.module.pricing.vo.PricingTemplateVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：核价模板 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.PricingTemplateService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:21:48
 */
public interface PricingTemplateService extends BaseService<PricingTemplate> {

    // 自定义方法区 不替换的区域【other_start】

    /**
     * 分页获取模板
     *
     * @param pricingTemplateSearchDTO
     * @param userCompany
     * @return
     */
    PageInfo<PricingTemplateVO> queryPageInfo(PricingTemplateSearchDTO pricingTemplateSearchDTO, String userCompany);

    /**
     * 通过id查询核价模板详情
     *
     * @param id
     * @param userCompany
     * @return
     */
    PricingTemplateVO getDetailsById(String id, String userCompany);

    /**
     * 保存
     *
     * @param pricingTemplateDTO
     * @return
     */
    String save(PricingTemplateDTO pricingTemplateDTO, String userCompany);

    /**
     * 删除
     *
     * @param pricingDelDTO
     * @param userCompany
     */
    void delById(PricingDelDTO pricingDelDTO);

    /**
     * 默认设置
     *
     * @param id
     * @param userCompany
     */
    void defaultSetting(String id, String userCompany);

    /**
     * 修改状态
     *
     * @param dto
     * @param userCompany
     */
    void updateStatus(PricingUpdateStatusDTO dto);


    /**
     * 核价公式模板计算
     *
     * @param formulaCountDTO
     * @param userCompany
     * @return
     */
    List<PricingTemplateItemVO> formulaCount(FormulaCountDTO formulaCountDTO, String userCompany);

    /**
     * 获取默认模板
     * @param devtType
     * @param userCompany
     * @return
     */
    PricingTemplateVO getDefaultPricingTemplate(String brand, String devtType,String userCompany);

    // 自定义方法区 不替换的区域【other_end】


}

