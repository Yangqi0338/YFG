/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleSalePageDto;
import com.base.sbc.module.sample.dto.SampleSaleSaveDto;
import com.base.sbc.module.sample.entity.SampleSale;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：样衣销售 service类
 * @address com.base.sbc.module.sample.service.SampleSaleService
 */
public interface SampleSaleService extends BaseService<SampleSale> {

    /** 分页查询 */
    PageInfo queryPageInfo(SampleSalePageDto dto);

    /** 保存样衣及明细 */
    SampleSaleVo save(SampleSaleSaveDto dto);

    /** 查询明细数据 */
    SampleSaleVo getDetail(String id);
}

