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
import com.base.sbc.module.sample.dto.SampleSalePageDto;
import com.base.sbc.module.sample.dto.SampleSaleSaveDto;
import com.base.sbc.module.sample.entity.SampleSale;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.mapper.SampleSaleMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleSaleItemService;
import com.base.sbc.module.sample.service.SampleSaleService;
import com.base.sbc.module.sample.vo.SampleSaleItemVo;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：样衣销售 service类
 *
 * @address com.base.sbc.module.sample.service.SampleSaleServiceImpl
 */
@Service
public class SampleSaleServiceImpl extends BaseServiceImpl<SampleSaleMapper, SampleSale> implements SampleSaleService {
    @Autowired
    SampleSaleMapper mapper;
    @Autowired
    SampleSaleItemService sampleSaleItemService;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;

    private IdGen idGen = new IdGen();

    @Override
    public String save(SampleSaleSaveDto dto) {
        SampleSale sale = CopyUtil.copy(dto, SampleSale.class);
        sale.setTotalCount(CollectionUtils.size(dto.getSampleItemList()));
        if (StringUtil.isEmpty(sale.getId())) {
            sale.setId(idGen.nextIdStr());
            sale.setCode("XS" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
            sale.setCompanyCode(getCompanyCode());
            sale.insertInit();
        } else {
            sale.updateInit();
        }
        super.saveOrUpdate(sale);
        sampleSaleItemService.saveSaleItem(dto.getSampleItemList(), sale.getId(), sale.getCode(), super.getCompanyCode());
        return sale.getId();
    }

    @Override
    public SampleSaleVo getDetail(String id) {
        SampleSaleVo vo = mapper.getDetail(id);

        SampleSalePageDto dto = new SampleSalePageDto();
        dto.setSampleSaleId(id);
        List<SampleSaleItemVo> list = sampleSaleItemService.getList(dto);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleSalePageDto dto) {
        QueryWrapper<SampleSaleVo> qw = new QueryWrapper<>();
        qw.eq("ss.company_code", getCompanyCode());
        qw.ge(Objects.nonNull(dto.getStartDate()), "ss.sale_date", dto.getStartDate());
        qw.ge(Objects.nonNull(dto.getEndDate()), "ss.sale_date", dto.getStartDate());
        if (StringUtils.isNotEmpty(dto.getSearch())) {
            qw.like("s.design_no", dto.getSearch()).
                    or().like("si.code", dto.getSearch()).
                    or().like("ss.code", dto.getSearch()).
                    or().like("ss.custmer_name", dto.getSearch());
        }
        qw.orderByDesc("ss.create_date");
        qw.groupBy("ss.id");

        Page<SampleSaleVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(qw);

        return objects.toPageInfo();
    }

    @Override
    public PageInfo getListBySampleItem(SampleSalePageDto dto) {
        QueryWrapper<SampleSaleVo> qw = new QueryWrapper<>();
        qw.eq("ss.company_code", getCompanyCode());
        if (null != dto.getStatus())
            qw.eq("si.status", dto.getStatus());
        if (null != dto.getSampleSaleItemId())
            qw.eq("ssi.id", dto.getSampleSaleItemId());
        if (null != dto.getSampleSaleId())
            qw.eq("ssi.sample_sale_id", dto.getSampleSaleId());
        qw.orderByDesc("ss.create_date");

        Page<SampleSaleVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListBySampleItem(qw);

        return objects.toPageInfo();
    }
}