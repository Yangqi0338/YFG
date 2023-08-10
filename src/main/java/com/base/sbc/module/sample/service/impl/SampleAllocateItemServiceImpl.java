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
import com.base.sbc.module.sample.dto.SampleAllocatePageDto;
import com.base.sbc.module.sample.entity.SampleAllocateItem;
import com.base.sbc.module.sample.enums.SampleLogOperationType;
import com.base.sbc.module.sample.mapper.SampleAllocateItemMapper;
import com.base.sbc.module.sample.service.SampleAllocateItemService;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleAllocateItemVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：样衣调拨 service类
 *
 * @address com.base.sbc.module.sample.service.SampleAllocateServiceImpl
 */
@Service
public class SampleAllocateItemServiceImpl extends BaseServiceImpl<SampleAllocateItemMapper, SampleAllocateItem> implements SampleAllocateItemService {

    @Autowired
    private SampleItemLogService sampleItemLogService;
    @Autowired
    private SampleService sampleService;

    @Override
    public List<SampleAllocateItemVo> getList(SampleAllocatePageDto dto) {
        return super.getBaseMapper().getList(dto);
    }

    @Override
    public void save(List<SampleAllocateItem> sampleAllocateItems, String sampleAllocateId,
                     String sampleAllocateCode, String position, String positionId) {
        IdGen idGen = new IdGen();
        List<String> ids = this.getIds(sampleAllocateId);
        if (CollectionUtils.isEmpty(sampleAllocateItems)) {
            super.getBaseMapper().deleteBatchIds(ids);
            return;
        }
        List<String> delIds = sampleAllocateItems.stream().map(SampleAllocateItem::getId)
                .filter(StringUtils::isNotEmpty)
                .filter(x -> !ids.contains(x))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(delIds)) {
            super.getBaseMapper().deleteBatchIds(delIds);
        }

        sampleAllocateItems.forEach(item -> {
            if (StringUtils.isEmpty(item.getId())) {
                item.setId(idGen.nextIdStr());
                item.setCompanyCode(getCompanyCode());
                item.setSampleAllocateId(sampleAllocateId);
                item.insertInit();
            } else {
                item.updateInit();
            }
            super.saveOrUpdate(item);
            String remarks = "样衣调拨：调拨单号：" + sampleAllocateCode + ", 数量：" + item.getCount();
            sampleItemLogService.save(item.getSampleItemId(), SampleLogOperationType.ALLOCATION.getK(), remarks);
        });
        List<String> sampleItemId = sampleAllocateItems.stream()
                .map(SampleAllocateItem::getSampleItemId)
                .collect(Collectors.toList());
        sampleService.sampleAllocate(sampleItemId, position, positionId);


    }

    private List<String> getIds(String allocateId) {
        QueryWrapper<SampleAllocateItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sample_allocate_id", allocateId);
        queryWrapper.eq("del_flag", '0');
        queryWrapper.select("id");
        List<SampleAllocateItem> sampleSaleItems = super.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sampleSaleItems)) {
            return Lists.newArrayList();
        }
        return sampleSaleItems.stream()
                .map(SampleAllocateItem::getId)
                .collect(Collectors.toList());
    }
}