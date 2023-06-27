/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.entity.*;
import com.base.sbc.module.sample.mapper.SampleCirculateItemMapper;
import com.base.sbc.module.sample.mapper.SampleCirculateMapper;
import com.base.sbc.module.sample.service.SampleCirculateItemService;
import com.base.sbc.module.sample.vo.SampleCirculateItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类描述：样衣借还明细 service类
 * @address com.base.sbc.module.sample.service.SampleCirculateItemServiceImpl
 */
@Service
public class SampleCirculateItemServiceImpl extends ServicePlusImpl<SampleCirculateItemMapper, SampleCirculateItem> implements SampleCirculateItemService {
    @Autowired
    SampleCirculateMapper mapper;
    @Autowired
    SampleCirculateItemMapper itemMapper;

    private IdGen idGen = new IdGen();

    @Override
    public PageInfo queryPageInfo(SampleCirculatePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SampleCirculateItemVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(dto);

        return objects.toPageInfo();
    }
}