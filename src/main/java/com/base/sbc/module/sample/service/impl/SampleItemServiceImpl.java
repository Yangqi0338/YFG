/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.vo.SamplePageByItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * 类描述：样衣明细 service类
 * @address com.base.sbc.module.sample.service.SampleService
 */
@Service
public class SampleItemServiceImpl extends BaseServiceImpl<SampleItemMapper, SampleItem> implements SampleItemService {
    private IdGen idGen = new IdGen();

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SamplePageByItemVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListByItemVo(dto);

        return objects.toPageInfo();
    }
}

