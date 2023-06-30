/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleAllocatePageDto;
import com.base.sbc.module.sample.dto.SampleAllocateSaveDto;
import com.base.sbc.module.sample.entity.SampleAllocate;
import com.base.sbc.module.sample.vo.SampleAllocateVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：样衣销售 service类
 * @address com.base.sbc.module.sample.service.SampleAllocateService
 */
public interface SampleAllocateService extends BaseService<SampleAllocate> {

    /** 分页查询 */
    PageInfo queryPageInfo(SampleAllocatePageDto dto);

    /** 保存样衣及明细 */
    SampleAllocateVo save(SampleAllocateSaveDto dto);

    /** 查询明细数据 */
    SampleAllocateVo getDetail(String id);
}

