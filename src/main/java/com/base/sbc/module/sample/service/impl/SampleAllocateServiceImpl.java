/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleAllocatePageDto;
import com.base.sbc.module.sample.dto.SampleAllocateSaveDto;
import com.base.sbc.module.sample.entity.SampleAllocate;
import com.base.sbc.module.sample.entity.SampleAllocateItem;
import com.base.sbc.module.sample.mapper.SampleAllocateItemMapper;
import com.base.sbc.module.sample.mapper.SampleAllocateMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
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
 * @address com.base.sbc.module.sample.service.SampleAllocateServiceImpl
 */
@Service
public class SampleAllocateServiceImpl extends BaseServiceImpl<SampleAllocateMapper, SampleAllocate> implements SampleAllocateService {
    @Autowired
    SampleAllocateMapper mapper;
    @Autowired
    SampleAllocateItemMapper sampleAllocateItemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleAllocateVo save(SampleAllocateSaveDto dto) {
        String id = "";
        SampleAllocate allocate = CopyUtil.copy(dto, SampleAllocate.class);

        if (allocate != null){
            if (StringUtil.isEmpty(allocate.getId())) {
                allocate.setId(idGen.nextIdStr());
                allocate.setCode("DB" + System.currentTimeMillis() + (int)((Math.random()*9+1)*1000));

                id = allocate.getId();
            } else {
                id = allocate.getId();
            }

            Integer count = 0, borrowCount = 0;
            for (SampleAllocateItem item : dto.getSampleItemList()){
                // 新增
                if (StringUtil.isEmpty(item.getId())){
                    item.setCompanyCode(getCompanyCode());
                    item.setId(idGen.nextIdStr());
                    item.setSampleAllocateId(id);

                    sampleAllocateItemMapper.insert(item);
                // 修改
                } else {

                }

                // 处理样衣
                sampleItemService.updateCount(item.getSampleItemId(), 4, item.getCount(),
                        dto.getToPositionId(), dto.getToPosition());

                // 日志
                String remarks = "样衣调拨：id-" + item.getSampleItemId() + ", 调拨单号：" + allocate.getCode() + ", 数量：" + item.getCount();
                sampleItemLogService.save(item.getId(), 2, remarks);
            }

            if (StringUtil.isEmpty(dto.getId())) {
                allocate.setCompanyCode(getCompanyCode());
                mapper.insert(allocate);
            } else {
                mapper.updateById(allocate);
            }
        }

        return mapper.getDetail(id);
    }

    @Override
    public SampleAllocateVo getDetail(String id) {
        SampleAllocateVo vo = mapper.getDetail(id);

        SampleAllocatePageDto dto = new SampleAllocatePageDto();
        dto.setSampleAllocateId(id);
        List<SampleAllocateItemVo> list = sampleAllocateItemMapper.getList(dto);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleAllocatePageDto dto) {
        QueryWrapper<SampleAllocateVo> qw = new QueryWrapper<>();
        qw.eq("ss.company_code", getCompanyCode());
        qw.ge(StrUtil.isNotEmpty(dto.getStartDate().toString()),"ss.sale_date", dto.getStartDate());
        qw.le(StrUtil.isNotEmpty(dto.getEndDate().toString()),"ss.sale_date", dto.getEndDate());
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch()).
                or().like(StrUtil.isNotBlank(dto.getSearch()), "si.code", dto.getSearch()).
                or().like(StrUtil.isNotBlank(dto.getSearch()), "ss.code", dto.getSearch()).
                or().like(StrUtil.isNotBlank(dto.getSearch()), "ss.custmer_name", dto.getSearch());
        qw.orderByDesc("ss.create_date");

        Page<SampleAllocateVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(qw);

        return objects.toPageInfo();
    }
}