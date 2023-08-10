/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.entity.SampleCirculateItem;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.enums.SampleLogOperationType;
import com.base.sbc.module.sample.mapper.SampleCirculateItemMapper;
import com.base.sbc.module.sample.mapper.SampleCirculateMapper;
import com.base.sbc.module.sample.service.SampleCirculateItemService;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.vo.SampleCirculateItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：样衣借还明细 service类
 *
 * @address com.base.sbc.module.sample.service.SampleCirculateItemServiceImpl
 */
@Service
public class SampleCirculateItemServiceImpl extends BaseServiceImpl<SampleCirculateItemMapper, SampleCirculateItem> implements SampleCirculateItemService {
    @Autowired
    SampleCirculateMapper mapper;
    @Autowired
    SampleCirculateItemMapper itemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;

    @Override
    public PageInfo queryPageInfo(SampleCirculatePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SampleCirculateItemVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(dto);

        return objects.toPageInfo();
    }

    @Override
    public List<SampleCirculateItem> getList(SampleCirculatePageDto dto) {
        return itemMapper.getList(dto);
    }

    @Override
    public void save(List<SampleItem> sampleItems, String companyCode, String sampleCirculateId, String designNo, String borrowCode) {
        IdGen idGen = new IdGen();
        List<SampleCirculateItem> sampleCirculateItems = sampleItems.stream()
                .map(sampleItem -> {
                    SampleCirculateItem sampleAllocateItem = new SampleCirculateItem();
                    sampleAllocateItem.setId(idGen.nextIdStr());
                    sampleAllocateItem.setCompanyCode(companyCode);
                    sampleAllocateItem.setSampleCirculateId(sampleCirculateId);
                    sampleAllocateItem.setSampleId(sampleItem.getSampleId());
                    sampleAllocateItem.setSampleItemId(sampleItem.getId());
                    sampleAllocateItem.setCount(sampleItem.getCount());
                    sampleAllocateItem.setDesignNo(designNo);
                    sampleAllocateItem.preInsert();
                    sampleItemLogService.save(sampleItem.getId(), SampleLogOperationType.BORROW.getK(), "样衣借出：, 借出单号：" + borrowCode + ", 借出数量：" + sampleAllocateItem.getCount());
                    return sampleAllocateItem;
                }).collect(Collectors.toList());
        super.saveBatch(sampleCirculateItems);


    }

    @Override
    public List<SampleCirculateItem> getByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new OtherException("获取未归还数据失败，参数为空");
        }
        QueryWrapper<SampleCirculateItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        queryWrapper.eq("return_flag", "0");
        List<SampleCirculateItem> sampleCirculateItems = itemMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sampleCirculateItems)) {
            throw new OtherException("获取未归还数据为空");
        }
        return sampleCirculateItems;
    }
}