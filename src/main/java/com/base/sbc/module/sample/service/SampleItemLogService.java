/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleItemLog;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：样衣明细日志 service类
 * @address com.base.sbc.module.sample.service.SampleItemLogService
 */
public interface SampleItemLogService extends IServicePlus<SampleItemLog> {

    /** 查询样衣明细数据 */
    List<SampleItemLog> getListBySampleItemId(String sampleItemId);
}

