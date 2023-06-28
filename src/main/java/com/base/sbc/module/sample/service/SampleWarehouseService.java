/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleWarehousePageDto;
import com.base.sbc.module.sample.entity.SampleWarehouse;
import com.base.sbc.module.sample.vo.SampleWarehouseVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：样衣仓库 service类
 * @address com.base.sbc.module.sample.service.SampleWarehouseService
 */
public interface SampleWarehouseService extends BaseService<SampleWarehouse> {

    /** 分页查询 */
    PageInfo queryPageInfo(SampleWarehousePageDto dto);

    /** 保存 */
    SampleWarehouseVo save(SampleWarehousePageDto dto);

    /** 查询明细数据 */
    SampleWarehouseVo getDetail(String id);
}

