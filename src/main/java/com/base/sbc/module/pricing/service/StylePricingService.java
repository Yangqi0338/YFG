/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pricing.dto.StylePricingSaveDTO;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.github.pagehelper.PageInfo;

import java.security.Principal;
import java.util.List;

/**
 * 类描述：款式定价 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.StylePricingService
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 */
public interface StylePricingService extends BaseService<StylePricing> {

    // 自定义方法区 不替换的区域【other_start】

    /**
     * 获取款式定价列表
     *
     * @param stylePricingSearchDTO
     * @return
     */
    PageInfo<StylePricingVO> getStylePricingList(Principal user,StylePricingSearchDTO stylePricingSearchDTO);
    /**
     * 获取款式定价列头查询
     *
     * @param stylePricingSearchDTO
     * @return
     */
    PageInfo<StylePricingVO> getStylePricingByLine(StylePricingSearchDTO stylePricingSearchDTO);

    /**
     * 通过资料包id获取
     *
     * @param packId
     * @param companyCode
     * @return
     */
    StylePricingVO getByPackId(String packId, String companyCode);

    /**
     * 保存
     *
     * @param stylePricingSaveDTO
     */
    void insertOrUpdate(StylePricingSaveDTO stylePricingSaveDTO, String companyCode);

    /**
     * 批量保存
     *
     * @param stylePricingSaves
     * @param companyCode
     */
    void insertOrUpdateBatch(List<StylePricingSaveDTO> stylePricingSaves, String companyCode);

    void unAuditStatus(List<String> ids);


// 自定义方法区 不替换的区域【other_end】


}
