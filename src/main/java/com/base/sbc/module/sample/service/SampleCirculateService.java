/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.dto.SampleCirculateSaveDto;
import com.base.sbc.module.sample.entity.SampleCirculate;
import com.base.sbc.module.sample.vo.SampleCirculateVo;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：样衣借还 service类
 * @address com.base.sbc.module.sample.service.SampleCirulateService
 */
public interface SampleCirculateService extends BaseService<SampleCirculate> {

    /** 分页查询 */
    PageInfo queryPageInfo(SampleCirculatePageDto dto);

    /** 保存 */
    SampleCirculateVo save(SampleCirculatePageDto dto);

    /** 查询明细数据 */
    SampleCirculateVo getDetail(String id);

    /** 借样 */
    SampleCirculateVo borrow(SampleCirculateSaveDto dto);

    /** 还样 */
    SampleCirculateVo return1(SampleCirculateSaveDto dto);
}

