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
import com.base.sbc.module.sample.entity.SampleInventoryItem;
import com.base.sbc.module.sample.mapper.SampleInventoryItemMapper;
import com.base.sbc.module.sample.service.SampleInventoryItemService;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleInventoryItemVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：样衣盘点明细 service类
 */
@Service
public class SampleInventoryItemServiceImpl extends BaseServiceImpl<SampleInventoryItemMapper, SampleInventoryItem> implements SampleInventoryItemService {

    @Autowired
    private SampleItemLogService sampleItemLogService;
    @Autowired
    private SampleService sampleService;

    @Override
    public List<SampleInventoryItemVo> getList(QueryWrapper qw) {
        return super.getBaseMapper().getList(qw);
    }

    @Override
    public void save(List<SampleInventoryItem> sampleInventoryItems, String inventoryId, String inventoryCode) {
        IdGen idGen = new IdGen();
        List<String> ids = this.getIdsByInventoryId(inventoryId);
        if (CollectionUtils.isEmpty(sampleInventoryItems)) {
            super.getBaseMapper().deleteBatchIds(ids);
            return;
        }
        List<String> delIds = sampleInventoryItems.stream().map(SampleInventoryItem::getId)
                .filter(StringUtils::isNotEmpty)
                .filter(x -> !ids.contains(x))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(delIds)) {
            super.getBaseMapper().deleteBatchIds(delIds);
        }

        sampleInventoryItems.forEach(item -> {
            if (StringUtils.isEmpty(item.getId())) {
                item.setId(idGen.nextIdStr());
                item.setCompanyCode(getCompanyCode());
                item.setSampleInventoryId(inventoryId);
                item.insertInit();
            } else {
                item.updateInit();
            }
            super.saveOrUpdate(item);
            // 日志
            String remarks = "样衣盘点：id-" + item.getId() + ", 盘点单号：" + inventoryCode + ", 数量：" + item.getNewCount();
            sampleItemLogService.save(item.getSampleItemId(), 2, remarks);
        });
    }

    @Override
    public Map<String, Integer> getSampleItemIdsByInventoryIds(List<String> inventoryIds) {
        QueryWrapper<SampleInventoryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sample_inventory_id", inventoryIds);
        queryWrapper.eq("del_flag", '0');
        queryWrapper.select("sample_item_id", "new_count");

        List<SampleInventoryItem> sampleInventoryItems = super.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sampleInventoryItems)) {
            return new HashMap<>();
        }
        return sampleInventoryItems.stream()
                .collect(Collectors.toMap(SampleInventoryItem::getSampleItemId, SampleInventoryItem::getNewCount, (k1, k2) -> k2));
    }


    private List<String> getIdsByInventoryId(String inventoryId) {
        QueryWrapper<SampleInventoryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sample_inventory_id", inventoryId);
        queryWrapper.eq("del_flag", '0');
        queryWrapper.select("id");
        List<SampleInventoryItem> sampleInventoryItems = super.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sampleInventoryItems)) {
            return Lists.newArrayList();
        }
        return sampleInventoryItems.stream()
                .map(SampleInventoryItem::getId)
                .collect(Collectors.toList());
    }
}