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
import com.base.sbc.module.sample.dto.SampleSalePageDto;
import com.base.sbc.module.sample.dto.SampleSaleSaveDto;
import com.base.sbc.module.sample.entity.SampleSale;
import com.base.sbc.module.sample.entity.SampleSaleItem;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.mapper.SampleSaleItemMapper;
import com.base.sbc.module.sample.mapper.SampleSaleMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleSaleService;
import com.base.sbc.module.sample.vo.SampleSaleItemVo;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：样衣销售 service类
 * @address com.base.sbc.module.sample.service.SampleSaleServiceImpl
 */
@Service
public class SampleSaleServiceImpl extends BaseServiceImpl<SampleSaleMapper, SampleSale> implements SampleSaleService {
    @Autowired
    SampleSaleMapper mapper;
    @Autowired
    SampleSaleItemMapper sampleSaleItemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleSaleVo save(SampleSaleSaveDto dto) {
        String id = "";
        SampleSale sale = CopyUtil.copy(dto, SampleSale.class);

        if (sale != null){
            if (StringUtil.isEmpty(sale.getId())) {
                sale.setId(idGen.nextIdStr());
                sale.setCode("XS" + System.currentTimeMillis() + (int)((Math.random()*9+1)*1000));

                id = sale.getId();
            } else {
                id = sale.getId();
            }

            Integer count = 0, borrowCount = 0;
            for (SampleSaleItem item : dto.getSampleItemList()){
                // 处理明细
                // 新增
                if (StringUtil.isEmpty(item.getId())){
                    item.setId(idGen.nextIdStr());
                    item.setCompanyCode(getCompanyCode());
                    item.setSampleSaleId(id);

                    sampleSaleItemMapper.insert(item);
                    // 修改
                } else {

                }

                // 处理样衣
                sampleItemService.updateCount(item.getSampleItemId(), 3, item.getCount(), "", "");

                // 日志
                String remarks = "样衣销售：id-" + item.getSampleItemId() + ", 销售单号：" + sale.getCode() + ", 数量：" + item.getCount();
                sampleItemLogService.save(item.getId(), 2, remarks);
            }

            if (StringUtil.isEmpty(dto.getId())) {
                sale.setCompanyCode(getCompanyCode());
                mapper.insert(sale);
            } else {
                mapper.updateById(sale);
            }
        }

        return mapper.getDetail(id);
    }

    @Override
    public SampleSaleVo getDetail(String id) {
        SampleSaleVo vo = mapper.getDetail(id);

        SampleSalePageDto dto = new SampleSalePageDto();
        dto.setSampleSaleId(id);
        List<SampleSaleItemVo> list = sampleSaleItemMapper.getList(dto);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleSalePageDto dto) {
        QueryWrapper<SampleSaleVo> qw = new QueryWrapper<>();
        qw.eq("ss.company_code", getCompanyCode());
        if (null != dto.getStartDate())
            qw.ge("ss.sale_date", dto.getStartDate());
        if (null != dto.getEndDate())
            qw.le("ss.sale_date", dto.getEndDate());
        if (null != dto.getSearch())
            qw.like("s.design_no", dto.getSearch()).
                or().like(StrUtil.isNotBlank(dto.getSearch()), "si.code", dto.getSearch()).
                or().like(StrUtil.isNotBlank(dto.getSearch()), "ss.code", dto.getSearch()).
                or().like(StrUtil.isNotBlank(dto.getSearch()), "ss.custmer_name", dto.getSearch());
        qw.orderByDesc("ss.create_date");

        Page<SampleSaleVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(qw);

        return objects.toPageInfo();
    }

    @Override
    public PageInfo getListBySampleItem(SampleSalePageDto dto) {
        QueryWrapper<SampleSaleVo> qw = new QueryWrapper<>();
        qw.eq("ss.company_code", getCompanyCode());
        if (null != dto.getStatus())
            qw.ge("si.status", dto.getStatus());
        qw.orderByDesc("ss.create_date");

        Page<SampleSaleVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListBySampleItem(qw);

        return objects.toPageInfo();
    }
}