/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.dto.SampleCirculateSaveDto;
import com.base.sbc.module.sample.entity.*;
import com.base.sbc.module.sample.mapper.SampleCirculateItemMapper;
import com.base.sbc.module.sample.mapper.SampleCirculateMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.service.SampleCirculateService;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.vo.SampleCirculateItemVo;
import com.base.sbc.module.sample.vo.SampleCirculateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 类描述：样衣借还 service类
 * @address com.base.sbc.module.sample.service.SampleCirulateServiceImpl
 */
@Service
public class SampleCirculateServiceImpl extends BaseServiceImpl<SampleCirculateMapper, SampleCirculate> implements SampleCirculateService {
    @Autowired
    SampleCirculateMapper mapper;
    @Autowired
    SampleCirculateItemMapper itemMapper;
    @Autowired
    SampleMapper sampleMapper;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemService sampleItemService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleCirculateVo save(SampleCirculatePageDto dto) {
        return null;
    }

    @Override
    public SampleCirculateVo getDetail(String id) {
        SampleCirculateVo vo = mapper.getDetail(id);

        SampleCirculatePageDto dto = new SampleCirculatePageDto();
        dto.setSampleCirculateId(id);
        List<SampleCirculateItem> list = itemMapper.getList(dto);
        vo.setItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleCirculatePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SampleCirculateItemVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(dto);

        return objects.toPageInfo();
    }

    @Override
    public SampleCirculateVo borrow(SampleCirculateSaveDto dto) {
        // 保存主表
        SampleCirculate sc = CopyUtil.copy(dto, SampleCirculate.class);

        sc.setId(idGen.nextIdStr());
        sc.setCompanyCode(getCompanyCode());
        sc.setBorrowCode("B" + System.currentTimeMillis() + (int)((Math.random() * 9 + 1) * 1000));

        mapper.insert(sc);

        // 保存明细
        for (SampleCirculateItem item : dto.getItemList()){
            item.setSampleCirculateId(sc.getId());
            item.setCompanyCode(sc.getCompanyCode());
            itemMapper.insert(item);

            // 处理样衣
            sampleItemService.updateCount(dto.getSampleItemId(), 1, item.getCount());

            // 处理日志
            String remarks = "借出：id-" + item.getId() + ", 借出单号：" + sc.getBorrowCode() + ", 借出数量：" + item.getCount();
            sampleItemLogService.save(dto.getSampleItemId(), 2, remarks);
        }

        return mapper.getDetail(sc.getId());
    }

    @Override
    public SampleCirculateVo return1(SampleCirculateSaveDto dto) {
        // 保存主表
        SampleCirculate sc = CopyUtil.copy(dto, SampleCirculate.class);
        sc.setReturnDate(new Date());

        mapper.updateById(sc);

        // 保存明细
        for (SampleCirculateItem item : dto.getItemList()){
            itemMapper.updateById(item);

            // 处理样衣
            sampleItemService.updateCount(dto.getSampleItemId(), 2, item.getCount());

            // 处理日志
            String remarks = "归还：id-" + item.getId() + ", 借出单号：" + sc.getBorrowCode() + ", 归还数量：" + item.getCount();
            sampleItemLogService.save(dto.getSampleItemId(), 2, remarks);
        }
        return mapper.getDetail(sc.getId());
    }
}