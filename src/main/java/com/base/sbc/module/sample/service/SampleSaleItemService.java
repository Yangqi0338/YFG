/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleSalePageDto;
import com.base.sbc.module.sample.entity.SampleSaleItem;
import com.base.sbc.module.sample.vo.SampleSaleItemVo;

import java.util.List;

/**
 * 类描述：样衣销售 service类
 *
 * @address com.base.sbc.module.sample.service.SampleSaleService
 */
public interface SampleSaleItemService extends BaseService<SampleSaleItem> {
    List<SampleSaleItemVo> getList(SampleSalePageDto dto);

    /**
     * 保存销售明细
     *
     * @param sampleItemList
     * @param saleId
     * @param saleCode
     * @param companyCode
     */
    void saveSaleItem(List<SampleSaleItem> sampleItemList, String saleId, String saleCode, String companyCode);


}

