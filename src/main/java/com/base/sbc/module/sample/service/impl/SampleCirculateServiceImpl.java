/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.dto.SampleCirculateSaveDto;
import com.base.sbc.module.sample.entity.SampleCirculate;
import com.base.sbc.module.sample.entity.SampleCirculateItem;
import com.base.sbc.module.sample.mapper.SampleCirculateItemMapper;
import com.base.sbc.module.sample.mapper.SampleCirculateMapper;
import com.base.sbc.module.sample.service.SampleCirculateService;
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
        SampleCirculate sc = new SampleCirculate();

        sc.setId(idGen.nextIdStr());
        sc.setCompanyCode(getCompanyCode());
        sc.setType(dto.getType());
        sc.setBorrowCode("B" + sc.getId());
        sc.setBorrowType(dto.getBorrowType());
        sc.setExpectReturnDate(dto.getExpectReturnDate());
        sc.setBorrowReason(dto.getBorrowReason());
        sc.setBorrowDate(dto.getBorrowDate());
        sc.setBorrowId(dto.getBorrowId());
        sc.setBorrowName(dto.getBorrowName());
        sc.setBorrowCount(dto.getBorrowCount());
        sc.setBorrowRemarks(dto.getBorrowRemarks());
        sc.setOperateId(dto.getOperateId());
        sc.setOperateName(dto.getOperateName());

        mapper.insert(sc);

        // 保存明细
        for (SampleCirculateItem item : dto.getItemList()){
            item.setSampleCirculateId(sc.getId());
            item.setCompanyCode(sc.getCompanyCode());
            itemMapper.insert(item);

            // 处理库存

            // 处理日志
        }

        return null;
    }

    @Override
    public SampleCirculateVo return1(SampleCirculateSaveDto dto) {
        // 保存主表
        SampleCirculate sc = new SampleCirculate();

        sc.setType(dto.getType());
        sc.setReturnRemarks(dto.getReturnRemarks());
        sc.setReturnDate(new Date());

        mapper.updateById(sc);

        // 保存明细
        for (SampleCirculateItem item : dto.getItemList()){
            itemMapper.updateById(item);

            // 处理库存

            // 处理日志
        }

        return null;
    }
}