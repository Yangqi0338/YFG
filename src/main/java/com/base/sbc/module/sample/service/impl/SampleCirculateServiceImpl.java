/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleBorrowDto;
import com.base.sbc.module.sample.dto.SampleCirculatePageDto;
import com.base.sbc.module.sample.dto.SampleReturnDTO;
import com.base.sbc.module.sample.entity.SampleCirculate;
import com.base.sbc.module.sample.entity.SampleCirculateItem;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.enums.SampleBorrowTypeEnum;
import com.base.sbc.module.sample.enums.SampleItemStatusEnum;
import com.base.sbc.module.sample.mapper.SampleCirculateMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.service.SampleCirculateItemService;
import com.base.sbc.module.sample.service.SampleCirculateService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleCirculateItemVo;
import com.base.sbc.module.sample.vo.SampleCirculateVo;
import com.base.sbc.module.sample.vo.SampleReturnDetailsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：样衣借还 service类
 *
 * @address com.base.sbc.module.sample.service.SampleCirulateServiceImpl
 */
@Service
public class SampleCirculateServiceImpl extends BaseServiceImpl<SampleCirculateMapper, SampleCirculate> implements SampleCirculateService {
    @Autowired
    SampleCirculateMapper mapper;
    @Autowired
    SampleCirculateItemService sampleCirculateItemService;
    @Autowired
    SampleService sampleService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;

    @Override
    public SampleCirculateVo save(SampleCirculatePageDto dto) {
        return null;
    }

    @Override
    public SampleCirculateVo getDetail(String id) {
        SampleCirculateVo vo = mapper.getDetail(id);

        SampleCirculatePageDto dto = new SampleCirculatePageDto();
        dto.setSampleCirculateId(id);
        List<SampleCirculateItem> list = sampleCirculateItemService.getList(dto);
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
    @Transactional
    public String borrow(SampleBorrowDto dto) {
        List<SampleItem> sampleItems = this.getSampleItemsByItemIds(dto.getSampleItemIds());
        boolean notInLibraryFlag = sampleItems.stream()
                .anyMatch(e -> !SampleItemStatusEnum.IN_LIBRARY.getK().equals(e.getStatus()) || e.getCount() - e.getBorrowCount() < 1);
        if (notInLibraryFlag) {
            throw new OtherException("存在 不在库中样衣，不可借出");
        }
        String companyCode = super.getCompanyCode();

        // 保存主表
        SampleCirculate sc = CopyUtil.copy(dto, SampleCirculate.class);
        IdGen idGen = new IdGen();
        sc.setId(idGen.nextIdStr());
        sc.setCompanyCode(companyCode);
        sc.setBorrowCode("B" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
        sc.preInsert();
        if (Objects.isNull(sc.getBorrowDate())) {
            sc.setBorrowDate(new Date());
        }
        mapper.insert(sc);

        sampleCirculateItemService.save(sampleItems, companyCode, sc.getId(), dto.getDesignNo(), sc.getBorrowCode());
        sampleService.sampleBatchBorrow(sampleItems);
        return sc.getId();
    }

    private List<SampleItem> getSampleItemsByItemIds(List<String> sampleItemIds) {
        List<SampleItem> sampleItems = sampleItemService.listByIds(sampleItemIds);
        if (CollectionUtils.isEmpty(sampleItems)) {
            throw new OtherException("样衣数据获取为空");
        }
        return sampleItems;
    }

    @Override
    @Transactional
    public void sampleReturn(SampleReturnDTO dto) {
        List<SampleCirculateItem> sampleCirculateItems = sampleCirculateItemService.getByIds(dto.getSampleCirculateItemIds());
        Map<String, Integer> objectObjectHashMap = new HashMap<>();
        List<String> sampleItemIds = new ArrayList<>();

        Map<String, Integer> sampleIdMap = new HashMap<>();

        sampleCirculateItems.forEach(sampleCirculateItem -> {
            sampleCirculateItem.setReturnFlag("1");
            sampleCirculateItem.setReturnDate(new Date());
            sampleCirculateItem.updateInit();
            Integer returnCount = objectObjectHashMap.get(sampleCirculateItem.getSampleCirculateId());
            if (Objects.isNull(returnCount)) {
                objectObjectHashMap.put(sampleCirculateItem.getSampleCirculateId(), sampleCirculateItem.getCount());
            } else {
                objectObjectHashMap.put(sampleCirculateItem.getSampleCirculateId(), returnCount + sampleCirculateItem.getCount());
            }
            Integer sampleReturnCount = sampleIdMap.get(sampleCirculateItem.getSampleId());
            if (Objects.isNull(sampleReturnCount)) {
                sampleIdMap.put(sampleCirculateItem.getSampleId(), sampleCirculateItem.getCount());
            } else {
                sampleIdMap.put(sampleCirculateItem.getSampleId(), sampleReturnCount + sampleCirculateItem.getCount());
            }
            sampleItemIds.add(sampleCirculateItem.getSampleItemId());
        });
        // 修改借还明细
        sampleCirculateItemService.updateBatchById(sampleCirculateItems);
        // 修改借还主表数据
        this.updateReturnCount(objectObjectHashMap);
        // 处理样衣管理
        sampleService.sampleReturn(sampleItemIds, sampleIdMap);
    }

    @Override
    public List<SampleReturnDetailsVO> getSampleReturnDetailsVO(List<String> sampleItemIds) {
        List<SampleReturnDetailsVO> sampleReturnDetailsVO = mapper.getSampleReturnDetailsVO(sampleItemIds);
        if (CollectionUtils.isEmpty(sampleReturnDetailsVO)) {
            throw new OtherException("样衣归还明细获取为空");
        }
        sampleReturnDetailsVO.forEach(e -> {
            e.setBorrowDays(DateUtils.getDistanceOfTwoDate(e.getBorrowDate(), e.getExpectReturnDate()) + "天");
            e.setBorrowTypeName(SampleBorrowTypeEnum.getV(e.getBorrowType()));
        });
        return sampleReturnDetailsVO;
    }


    /**
     * 修改归还信息
     *
     * @param objectObjectHashMap
     */
    private void updateReturnCount(Map<String, Integer> objectObjectHashMap) {
        List<SampleCirculate> sampleCirculates = mapper.selectBatchIds(objectObjectHashMap.values());
        if (CollectionUtils.isEmpty(sampleCirculates)) {
            return;
        }
        List<SampleCirculate> updateSampleCirculate = sampleCirculates.stream()
                .map(e -> {
                    Integer returnCount = objectObjectHashMap.get(e.getId());
                    SampleCirculate sampleCirculate = new SampleCirculate();
                    sampleCirculate.setId(e.getId());
                    sampleCirculate.setReturnCount(e.getBorrowCount() - returnCount);
                    sampleCirculate.updateInit();
                    return sampleCirculate;
                }).collect(Collectors.toList());
        super.updateBatchById(updateSampleCirculate);
    }

//    @Override
//    public SampleCirculateVo return1(SampleBorrowDto dto) {
////        // 保存主表
////        SampleCirculate sc = CopyUtil.copy(dto, SampleCirculate.class);
////        sc.setReturnDate(new Date());
////
////        mapper.updateById(sc);
////
////        // 保存明细
////        for (SampleCirculateItem item : dto.getItemList()) {
////            itemMapper.updateById(item);
////
////            // 处理样衣
////            sampleItemService.updateCount(dto.getSampleItemId(), 2, item.getCount(), "", "");
////
////            // 处理日志
////            String remarks = "归还：id-" + item.getId() + ", 借出单号：" + sc.getBorrowCode() + ", 归还数量：" + item.getCount();
////            sampleItemLogService.save(dto.getSampleItemId(), 2, remarks);
////        }
////        return mapper.getDetail(sc.getId());
//        return null;
//    }
}