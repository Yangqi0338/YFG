/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.fabric.dto.FabricPlanningSaveDTO;
import com.base.sbc.module.fabric.dto.FabricPlanningSearchDTO;
import com.base.sbc.module.fabric.entity.FabricPlanning;
import com.base.sbc.module.fabric.enums.ApproveStatusEnum;
import com.base.sbc.module.fabric.mapper.FabricPlanningMapper;
import com.base.sbc.module.fabric.service.FabricPlanningItemService;
import com.base.sbc.module.fabric.service.FabricPlanningService;
import com.base.sbc.module.fabric.service.FabricPoolService;
import com.base.sbc.module.fabric.vo.FabricPlanningListVO;
import com.base.sbc.module.fabric.vo.FabricPlanningVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：面料企划 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPlanningService
 * @email your email
 * @date 创建时间：2023-8-23 11:03:00
 */
@Service
public class FabricPlanningServiceImpl extends BaseServiceImpl<FabricPlanningMapper, FabricPlanning> implements FabricPlanningService {
    // 自定义方法区 不替换的区域【other_start】

    @Autowired
    private FabricPlanningItemService fabricPlanningItemService;
    @Autowired
    private FlowableService flowableService;
    @Autowired
    private FabricPoolService fabricPoolService;

    @Override
    public PageInfo<FabricPlanningListVO> getFabricPlanningList(FabricPlanningSearchDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        dto.setCompanyCode(super.getCompanyCode());
        List<FabricPlanningListVO> fabricPlanningList = super.getBaseMapper().getFabricPlanningList(dto);
        return new PageInfo<>(fabricPlanningList);
    }

    @Override
    @Transactional
    public void save(FabricPlanningSaveDTO dto) {
        FabricPlanning fabricPlanning = CopyUtil.copy(dto, FabricPlanning.class);
        if (StringUtils.isEmpty(fabricPlanning.getId()) || "-".equals(fabricPlanning.getId())) {
            fabricPlanning.setId(new IdGen().nextIdStr());
            fabricPlanning.setCompanyCode(super.getCompanyCode());
            fabricPlanning.insertInit();
        } else {
            fabricPlanning.updateInit();
        }
        super.saveOrUpdate(fabricPlanning);
        fabricPlanningItemService.saveItem(dto.getFabricPlanningItems(), fabricPlanning.getId());
        if (ApproveStatusEnum.UNDER_REVIEW.getK().equals(dto.getApproveStatus())) {
            this.sendApproval(fabricPlanning.getId(), fabricPlanning);
        }
        fabricPoolService.fabricPlanningSync(fabricPlanning.getId(), dto.getFabricPlanningItems());
    }

    @Override
    public FabricPlanningVO getDetail(String id) {
        FabricPlanning fabricPlanning = super.getById(id);
        if (Objects.isNull(fabricPlanning)) {
            throw new OtherException("数据不存在");
        }
        FabricPlanningVO fabricPlanningVO = CopyUtil.copy(fabricPlanning, FabricPlanningVO.class);
        fabricPlanningVO.setFabricPlanningItems(fabricPlanningItemService.getByFabricPlanningId(fabricPlanningVO.getId(), null));
        return fabricPlanningVO;
    }

    @Override
    public boolean approval(AnswerDto dto) {
        FabricPlanning fabricPlanning = super.getById(dto.getBusinessKey());
        if (Objects.isNull(fabricPlanning)) {
            throw new OtherException("数据不存在");
        }
        fabricPlanning.setApproveStatus(StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS) ? "3" : "4");
        fabricPlanning.setApproveDate(new Date());
        fabricPlanning.setApproveUserId(super.getUserId());
        fabricPlanning.setApproveUserName(super.getUserName());
        fabricPlanning.updateInit();
        super.updateById(fabricPlanning);
        return true;
    }

    /**
     * 发起审批
     *
     * @param id
     * @param fabricPlanning
     */
    private void sendApproval(String id, FabricPlanning fabricPlanning) {
        flowableService.start(FlowableService.FABRIC_PLANNING,
                FlowableService.FABRIC_PLANNING, id,
                "/pdm/api/saas/fabricPlanning/approval",
                "/pdm/api/saas/fabricPlanning/approval",
                "/pdm/api/saas/fabricPlanning/approval",
                "/pdm/api/saas/fabricPlanning/getDetail?id=" + id, BeanUtil.beanToMap(fabricPlanning));
    }

// 自定义方法区 不替换的区域【other_end】

}
