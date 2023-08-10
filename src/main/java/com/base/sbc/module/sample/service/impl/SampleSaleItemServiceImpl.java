/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleSalePageDto;
import com.base.sbc.module.sample.entity.SampleSaleItem;
import com.base.sbc.module.sample.enums.SampleLogOperationType;
import com.base.sbc.module.sample.mapper.SampleSaleItemMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleSaleItemService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleSaleItemVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：样衣销售明细 service类
 */
@Service
public class SampleSaleItemServiceImpl extends BaseServiceImpl<SampleSaleItemMapper, SampleSaleItem> implements SampleSaleItemService {
    @Autowired
    private SampleItemLogService sampleItemLogService;
    @Autowired
    private SampleService sampleService;

    @Override
    public List<SampleSaleItemVo> getList(SampleSalePageDto dto) {
        return super.getBaseMapper().getList(dto);
    }

    @Override
    public void saveSaleItem(List<SampleSaleItem> sampleItemList, String saleId, String saleCode, String companyCode) {
        IdGen idGen = new IdGen();
        List<String> ids = this.getIds(saleId);
        if (CollectionUtils.isEmpty(sampleItemList)) {
            super.getBaseMapper().deleteBatchIds(ids);
            return;
        }
        List<String> delIds = sampleItemList.stream().map(SampleSaleItem::getId)
                .filter(StringUtils::isNotEmpty)
                .filter(x -> !ids.contains(x))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(delIds)) {
            super.getBaseMapper().deleteBatchIds(delIds);
        }
        sampleItemList.forEach(item -> {
            if (StringUtils.isEmpty(item.getId())) {
                item.setId(idGen.nextIdStr());
                item.setCompanyCode(getCompanyCode());
                item.setSampleSaleId(saleId);
                item.insertInit();
            } else {
                item.updateInit();
            }
            super.saveOrUpdate(item);
            sampleService.sampleSale(item.getSampleItemId());
            String remarks = "样衣销售 销售单号：" + saleCode + ", 数量：" + item.getCount();
            sampleItemLogService.save(item.getId(), SampleLogOperationType.SALES.getK(), remarks);
        });
    }

    private List<String> getIds(String saleId) {
        QueryWrapper<SampleSaleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sample_sale_id", saleId);
        queryWrapper.eq("del_flag", '0');
        queryWrapper.select("id");
        List<SampleSaleItem> sampleSaleItems = super.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sampleSaleItems)) {
            return Lists.newArrayList();
        }
        return sampleSaleItems.stream()
                .map(SampleSaleItem::getId)
                .collect(Collectors.toList());
    }
}