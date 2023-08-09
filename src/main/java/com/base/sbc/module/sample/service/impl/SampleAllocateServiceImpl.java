/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleAllocatePageDto;
import com.base.sbc.module.sample.dto.SampleAllocateSaveDto;
import com.base.sbc.module.sample.entity.SampleAllocate;
import com.base.sbc.module.sample.mapper.SampleAllocateMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.service.SampleAllocateItemService;
import com.base.sbc.module.sample.service.SampleAllocateService;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.vo.SampleAllocateItemVo;
import com.base.sbc.module.sample.vo.SampleAllocateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：样衣调拨 service类
 *
 * @address com.base.sbc.module.sample.service.SampleAllocateServiceImpl
 */
@Service
public class SampleAllocateServiceImpl extends BaseServiceImpl<SampleAllocateMapper, SampleAllocate> implements SampleAllocateService {
    @Autowired
    SampleAllocateMapper mapper;
    @Autowired
    SampleAllocateItemService sampleAllocateItemService;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;

    private IdGen idGen = new IdGen();

    @Override
    public void save(SampleAllocateSaveDto dto) {
        SampleAllocate allocate = CopyUtil.copy(dto, SampleAllocate.class);

        if (StringUtil.isEmpty(allocate.getId())) {
            allocate.setId(idGen.nextIdStr());
            allocate.setCode("DB" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
            allocate.setCompanyCode(this.getCompanyCode());
            allocate.insertInit();
        } else {
            allocate.updateInit();
        }

        super.saveOrUpdate(allocate);

        sampleAllocateItemService.save(dto.getSampleItemList(), allocate.getId(), allocate.getCode(), allocate.getToPosition(), allocate.getToPositionId());
    }

    @Override
    public SampleAllocateVo getDetail(String id) {
        SampleAllocateVo vo = mapper.getDetail(id);

        SampleAllocatePageDto dto = new SampleAllocatePageDto();
        dto.setSampleAllocateId(id);
        List<SampleAllocateItemVo> list = sampleAllocateItemService.getList(dto);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleAllocatePageDto dto) {
        QueryWrapper<SampleAllocateVo> qw = new QueryWrapper<>();
        qw.eq("sa.company_code", getCompanyCode());
        qw.orderByDesc("sa.create_date");

        Page<SampleAllocateVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(qw);

        return objects.toPageInfo();
    }
}