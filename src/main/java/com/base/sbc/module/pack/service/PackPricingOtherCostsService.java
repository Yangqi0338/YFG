/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.config.enums.business.PackPricingOtherCostsItemType;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.dto.OtherCostsPageDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackPricingOtherCostsDto;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
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
public interface PackPricingOtherCostsService extends PackBaseService<PackPricingOtherCosts> {


// 自定义方法区 不替换的区域【other_start】

    PageInfo<PackPricingOtherCostsVo> pageInfo(OtherCostsPageDto dto);

//    PackPricingOtherCostsVo saveByDto(PackPricingOtherCostsDto dto);

    /**
     * 批量保存他费用
     * @param dto
     * @return
     */
    void batchOtherCosts(List<PackPricingOtherCostsDto> dto);

    Map<String, BigDecimal> statistics(PackCommonSearchDto dto);

//    /**
//     * 通过主id统计
//     *
//     * @param foreignIds
//     * @param isPackType
//     * @return
//     */
//    List<PackPricingOtherCosts> getPriceSumByForeignIds(List<String> foreignIds, String packType);

    /**
     * 生成费用明细单
     * @param itemType 字典编码 多个用,分割
     * @param foreignId 父级id
     * @param packType
     * @return
     */
    boolean createCostDetail(List<PackPricingOtherCostsItemType> itemType, String foreignId, String packType, String productType);

    /**
     * 生成外辅工艺PDF
     *
     * @return
     */
    AttachmentVo generateWfgyPdf(OtherCostsPageDto dto);
// 自定义方法区 不替换的区域【other_end】


}
