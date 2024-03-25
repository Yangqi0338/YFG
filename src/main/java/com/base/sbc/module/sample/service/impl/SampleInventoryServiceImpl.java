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
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleInventoryPageDto;
import com.base.sbc.module.sample.dto.SampleInventorySaveDto;
import com.base.sbc.module.sample.entity.SampleInventory;
import com.base.sbc.module.sample.entity.SampleInventoryItem;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.enums.SampleInventoryStatusEnum;
import com.base.sbc.module.sample.enums.SampleItemStatusEnum;
import com.base.sbc.module.sample.mapper.SampleInventoryMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.service.*;
import com.base.sbc.module.sample.vo.SampleInventoryItemVo;
import com.base.sbc.module.sample.vo.SampleInventoryVo;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：样衣盘点 service类
 *
 * @address com.base.sbc.module.sample.service.SampleInventoryServiceImpl
 */
@Service
public class SampleInventoryServiceImpl extends BaseServiceImpl<SampleInventoryMapper, SampleInventory> implements SampleInventoryService {
    @Autowired
    SampleInventoryMapper mapper;
    @Autowired
    SampleInventoryItemService sampleInventoryItemService;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;
    @Autowired
    SampleService sampleService;

    private IdGen idGen = new IdGen();

    @Override
    @Transactional
    public String save(SampleInventorySaveDto dto) {
        SampleInventory inventory = CopyUtil.copy(dto, SampleInventory.class);

        if (StringUtil.isEmpty(inventory.getId())) {
            inventory.setId(idGen.nextIdStr());
            inventory.setCode("PD" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
            inventory.insertInit();
            inventory.setCompanyCode(super.getCompanyCode());
        } else {
            inventory.updateInit();
        }
        super.saveOrUpdate(inventory);
        List<String> sampleItemIds = dto.getSampleItemList().stream()
                .map(SampleInventoryItem::getSampleItemId)
                .collect(Collectors.toList());
        sampleInventoryItemService.save(dto.getSampleItemList(), inventory.getId(), inventory.getCode());
        if (SampleInventoryStatusEnum.INVENTORY_IN_PROGRESS.getK().equals(dto.getInventoryStatus())) {

            this.sampleInventoryUpdateStatus(sampleItemIds);
        }
        if (SampleInventoryStatusEnum.COMPLETE.getK().equals(dto.getInventoryStatus())) {
            endInventory(Lists.newArrayList(inventory.getId()));
        }
        return inventory.getId();
    }

    private void sampleInventoryUpdateStatus(List<String> sampleItemIds) {
        sampleItemService.checkSampleStatus(sampleItemIds, Lists.newArrayList(SampleItemStatusEnum.IN_LIBRARY.getK(), SampleItemStatusEnum.INVENTORY_IN_PROGRESS.getK()));
        List<SampleItem> sampleItems = sampleItemIds.stream()
                .map(e -> {
                    SampleItem sampleItem = new SampleItem();
                    sampleItem.setId(e);
                    sampleItem.setStatus(SampleItemStatusEnum.INVENTORY_IN_PROGRESS.getK());
                    return sampleItem;
                }).collect(Collectors.toList());
        sampleItemService.updateBatchById(sampleItems);
    }

    @Override
    @Transactional
    public void startInventory(List<String> inventoryIds) {
        this.updateStatus(inventoryIds, SampleInventoryStatusEnum.INVENTORY_IN_PROGRESS.getK());
        Map<String, Integer> sampleItemIdsByInventoryIds = sampleInventoryItemService.getSampleItemIdsByInventoryIds(inventoryIds);
        this.sampleInventoryUpdateStatus(new ArrayList<>(sampleItemIdsByInventoryIds.keySet()));
    }

    private void updateStatus(List<String> inventoryIds, String inventoryStatus) {
        List<SampleInventory> sampleInventories = mapper.selectBatchIds(inventoryIds);
        if (CollectionUtils.isEmpty(sampleInventories)) {
            throw new OtherException("盘点数据不存在");
        }

        sampleInventories.forEach(e -> {
            e.setInventoryStatus(inventoryStatus);
            e.updateInit();
        });
        super.updateBatchById(sampleInventories);
    }

    @Override
    public void endInventory(List<String> inventoryIds) {
        this.updateStatus(inventoryIds, SampleInventoryStatusEnum.COMPLETE.getK());
        Map<String, Integer> sampleItemIdsByInventoryIds = sampleInventoryItemService.getSampleItemIdsByInventoryIds(inventoryIds);
        // 校验样衣是否都是在盘点中
        sampleItemService.checkSampleStatus(new ArrayList<>(sampleItemIdsByInventoryIds.keySet()), Lists.newArrayList(SampleItemStatusEnum.IN_LIBRARY.getK(), SampleItemStatusEnum.INVENTORY_IN_PROGRESS.getK()));
        // 更新样衣
        sampleService.sampleInventory(sampleItemIdsByInventoryIds);
    }



    @Override
    public void cancel(List<String> inventoryIds) {
        List<SampleInventory> sampleInventories = mapper.selectBatchIds(inventoryIds);
        if (CollectionUtils.isEmpty(sampleInventories)) {
            throw new OtherException("盘点数据不存在");
        }
        boolean b = sampleInventories.stream().anyMatch(e -> !SampleInventoryStatusEnum.NOT_STARTED.getK().equals(e.getInventoryStatus()));
        if (b) {
            throw new OtherException("存在已开始数据，不可作废");
        }
        sampleInventories.forEach(e -> {
            e.setStatus(2);
            e.updateInit();
        });
        super.updateBatchById(sampleInventories);
    }

    @Override
    public SampleInventoryVo getDetail(String id) {
        SampleInventoryVo vo = mapper.getDetail(id);

        QueryWrapper<SampleInventoryVo> qw = new QueryWrapper<>();
        qw.eq("sii.company_code", getCompanyCode());
        qw.eq("si2.id", id);
        List<SampleInventoryItemVo> list = sampleInventoryItemService.getList(qw);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleInventoryPageDto dto) {
        QueryWrapper<SampleInventoryVo> qw = new QueryWrapper<>();
        qw.eq("si2.company_code", getCompanyCode());

        if (StrUtil.isNotEmpty(dto.getStatus()) && !StrUtil.equals(dto.getStatus(), "0")) {
            qw.eq("si2.status", dto.getStatus());
        }
        if (null != dto.getStartDate()) {
            qw.ge("si2.start_date", dto.getStartDate());
        }
        if (null != dto.getEndDate()) {
            qw.le("si2.end_date", dto.getEndDate());
        }
//        if (null != dto.getEndDate())
//            qw.le("si2.code", dto.getCode());
        if (null != dto.getSearch()) {
            qw.like("si2.name", dto.getSearch()).
                    or().like("si2.code", dto.getSearch());
        }

        qw.orderByDesc("si2.create_date");
        dto.setCompanyCode(getCompanyCode());
        PageHelper.startPage(dto);
        List<SampleInventoryVo> list = getBaseMapper().getList(dto);
        list.forEach(e -> {
            if (SampleInventoryStatusEnum.NOT_STARTED.getK().equals(e.getInventoryStatus()) && (Objects.nonNull(e.getEndDate()) && e.getEndDate().before(new Date()))) {
                e.setInventoryStatus(SampleInventoryStatusEnum.OVERDUE.getK());
            }
        });
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo getListBySampleItem(SampleInventoryPageDto dto) {
        QueryWrapper<SampleInventoryVo> qw = new QueryWrapper<>();
        qw.eq("si2.company_code", getCompanyCode());
        if (null != dto.getStatus()) {
            qw.eq("si2.status", dto.getStatus());
        }
        qw.orderByDesc("si2.create_date");

        Page<SampleSaleVo> objects = PageHelper.startPage(dto);
        sampleInventoryItemService.getList(qw);

        return objects.toPageInfo();
    }

    @Override
    public SampleInventoryVo updateStatus(SampleInventorySaveDto dto) {
//        SampleInventory si = mapper.selectById(dto.getId());
//        QueryWrapper qw = new QueryWrapper<>();
//        qw.eq("si2.id", dto.getId());
//        List<SampleInventoryItemVo> siiList = sampleInventoryItemMapper.getList(qw);
//
//        // 样衣明细
//        List<SampleItem> itemList = null;
//        for (SampleInventoryItemVo itemVo : siiList) {
//            SampleItem item = sampleItemMapper.selectById(itemVo.getSampleItemId());
//            itemList.add(item);
//        }
//
//        // 盘点单作废
//        if (dto.getStatus() != null && dto.getStatus() == 2) {
//            si.setStatus(2);
//
//            // 处理样衣
//            for (SampleItem sampleItem : itemList) {
//                if (sampleItem.getStatus() == "1") sampleItem.setStatus("1");
//            }
//            // 盘点状态
//        } else if (dto.getInventoryStatus() != null && dto.getInventoryStatus() > 0) {
//            si.setInventoryStatus(dto.getInventoryStatus());
//
//            // 盘点中
//            if (dto.getInventoryStatus() == 1) {
//                // 处理样衣
//                for (SampleItem sampleItem : itemList) {
//                    if (sampleItem.getStatus() == "1") sampleItem.setStatus("5");
//                }
//                // 完成
//            } else if (dto.getInventoryStatus() == 2) {
//                // 处理样衣
//                for (SampleItem sampleItem : itemList) {
//                    if (sampleItem.getStatus() == "5") sampleItem.setStatus("1");
//                }
//            }
//            // 审核
//        } else if (dto.getExamineStatus() != null && dto.getExamineStatus() > 0) {
//            si.setExamineStatus(dto.getExamineStatus());
//        }
//
//        mapper.updateById(si);
//        sampleItemService.updateBatchById(itemList);
//
//        QueryWrapper<SampleInventoryVo> qw2 = new QueryWrapper<>();
//        qw2.eq("sii.company_code", getCompanyCode());
//        qw2.eq("si2.id", dto.getId());
//        List<SampleInventoryItemVo> list = sampleInventoryItemMapper.getList(qw2);
//        SampleInventoryVo vo = mapper.getDetail(dto.getId());
//        vo.setSampleItemList(list);

        return null;
    }
}