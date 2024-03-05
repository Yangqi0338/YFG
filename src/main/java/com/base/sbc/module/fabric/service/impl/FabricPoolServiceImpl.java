/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricPlanningItemSaveDTO;
import com.base.sbc.module.fabric.dto.FabricPlanningSearchDTO;
import com.base.sbc.module.fabric.dto.FabricPoolSaveDTO;
import com.base.sbc.module.fabric.entity.FabricPool;
import com.base.sbc.module.fabric.entity.FabricPoolItem;
import com.base.sbc.module.fabric.enums.ApproveStatusEnum;
import com.base.sbc.module.fabric.enums.SourceEnum;
import com.base.sbc.module.fabric.mapper.FabricPoolMapper;
import com.base.sbc.module.fabric.service.FabricPlanningItemService;
import com.base.sbc.module.fabric.service.FabricPoolItemService;
import com.base.sbc.module.fabric.service.FabricPoolService;
import com.base.sbc.module.fabric.vo.FabricPoolListVO;
import com.base.sbc.module.fabric.vo.FabricPoolVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：面料池 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPoolService
 * @email your email
 * @date 创建时间：2023-8-23 11:02:50
 */
@Service
public class FabricPoolServiceImpl extends BaseServiceImpl<FabricPoolMapper, FabricPool> implements FabricPoolService {
    // 自定义方法区 不替换的区域【other_start】

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FabricPoolItemService fabricPoolItemService;
    @Autowired
    private FabricPlanningItemService fabricPlanningItemService;

    @Override
    public PageInfo<FabricPoolListVO> getFabricPoolList(FabricPlanningSearchDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        dto.setCompanyCode(super.getCompanyCode());
        List<FabricPoolListVO> fabricPoolList = super.getBaseMapper().getFabricPoolList(dto);
        return new PageInfo<>(fabricPoolList);
    }

    @Override
    public void save(FabricPoolSaveDTO dto) {
        FabricPool fabricPool = CopyUtil.copy(dto, FabricPool.class);
        if (StringUtils.isEmpty(fabricPool.getId()) || "-".equals(fabricPool.getId())) {
            fabricPool.setId(new IdGen().nextIdStr());
            fabricPool.setCompanyCode(super.getCompanyCode());
            fabricPool.insertInit();
        } else {
            fabricPool.updateInit();
        }
        super.saveOrUpdate(fabricPool);
        fabricPoolItemService.saveItem(dto.getFabricPoolItemSaves(), fabricPool.getId());
        if (ApproveStatusEnum.UNDER_REVIEW.getK().equals(dto.getApproveStatus())) {
            this.sendApproval(fabricPool.getId(), fabricPool);
        }
    }

    @Override
    public FabricPoolVO getDetail(String id) {
        FabricPoolVO fabricPoolVO = super.getBaseMapper().getDetail(id);
        if (Objects.isNull(fabricPoolVO)) {
            throw new OtherException("数据不存在");
        }
        fabricPoolVO.setFabricPoolItems(fabricPoolItemService.getByFabricPoolId(fabricPoolVO.getId()));
        return fabricPoolVO;
    }

    @Override
    public boolean approval(AnswerDto dto) {
        FabricPool fabricPool = super.getById(dto.getBusinessKey());
        if (Objects.isNull(fabricPool)) {
            throw new OtherException("数据不存在");
        }
        fabricPool.setApproveStatus(StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS) ? "3" : "4");
        fabricPool.setApproveDate(new Date());
        fabricPool.setApproveUserId(super.getUserId());
        fabricPool.setApproveUserName(super.getUserName());
        fabricPool.updateInit();
        super.updateById(fabricPool);
        return true;
    }


    @Override
    public void fabricPlanningSync(String fabricPlanningId, List<FabricPlanningItemSaveDTO> fabricPlanningItems) {
        if (CollUtil.isEmpty(fabricPlanningItems)) {
            return;
        }
        Map<String, List<String>> sourceIds = fabricPoolItemService.getSourceIdByFabricPlanningId(fabricPlanningId);
        if (Objects.isNull(sourceIds)) {
            return;
        }
        String companyCode = super.getCompanyCode();
        List<FabricPoolItem> fabricPoolItems = new ArrayList<>();
        IdGen idGen = new IdGen();
        sourceIds.forEach((k, v) -> {
            fabricPlanningItems.forEach(e -> {
                if ((CollUtil.isNotEmpty(v) && v.contains(e.getSourceId())) || !SourceEnum.MATERIAL.getK().equals(e.getSource())) {
                    return;
                }
                FabricPoolItem fabricPoolItem = this.getFabricPoolItem(companyCode, k, idGen, e);
                fabricPoolItems.add(fabricPoolItem);
            });
        });
        if (CollUtil.isNotEmpty(fabricPoolItems)) {
            fabricPoolItemService.saveBatch(fabricPoolItems);
        }

    }

    @Override
    public void materialReviewPassedSync(String fabricLibraryMaterialCode, String materialId, String materialCode) {
        if (StringUtils.isEmpty(materialId)) {
            return;
        }
        Map<String, List<String>> fabricPlanningId = fabricPlanningItemService.getFabricPlanningId(fabricLibraryMaterialCode);
        if (Objects.isNull(fabricPlanningId) || fabricPlanningId.size() < 1) {
            return;
        }

        LambdaQueryWrapper<FabricPool> qw = new QueryWrapper<FabricPool>()
                .lambda()
                .in(FabricPool::getFabricPlanningId, fabricPlanningId.keySet());
        List<FabricPool> list = super.list(qw);

        if (CollUtil.isEmpty(list)) {
            return;
        }

        String companyCode = super.getCompanyCode();
        List<FabricPoolItem> fabricPoolItems = list.stream()
                .map(e -> {
                    FabricPoolItem fabricPoolItem = new FabricPoolItem();
                    fabricPoolItem.setCompanyCode(companyCode);
                    fabricPoolItem.insertInit();
                    fabricPoolItem.setSource(SourceEnum.MATERIAL.getK());
                    fabricPoolItem.setSourceId(materialId);
                    fabricPoolItem.setFabricPoolId(e.getId());
                    fabricPoolItem.setMaterialCode(materialCode);
                    return fabricPoolItem;
                }).collect(Collectors.toList());
        fabricPoolItemService.saveBatch(fabricPoolItems);
    }

    @Override
    public void delete(String id) {
        super.getBaseMapper().deleteById(id);
    }

    private FabricPoolItem getFabricPoolItem(String companyCode, String k, IdGen idGen, FabricPlanningItemSaveDTO e) {
        FabricPoolItem fabricPoolItem = CopyUtil.copy(e, FabricPoolItem.class);
        fabricPoolItem.setFabricPoolId(k);
        fabricPoolItem.setCompanyCode(companyCode);
        fabricPoolItem.insertInit();
        fabricPoolItem.setId(idGen.nextIdStr());
        fabricPoolItem.setSource(SourceEnum.MATERIAL.getK());
        return fabricPoolItem;
    }

    /**
     * 发起审批
     *
     * @param id
     * @param fabricPool
     */
    private void sendApproval(String id, FabricPool fabricPool) {
        flowableService.start(FlowableService.FABRIC_POOL,
                FlowableService.FABRIC_POOL, id,
                "/pdm/api/saas/fabricPlanning/approval",
                "/pdm/api/saas/fabricPlanning/approval",
                "/pdm/api/saas/fabricPlanning/approval",
                "/pdm/api/saas/fabricPlanning/getDetail?id=" + id, BeanUtil.beanToMap(fabricPool));
    }

// 自定义方法区 不替换的区域【other_end】

}
