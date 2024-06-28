/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.enums.GeneralFlagEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.taskassignment.constants.ResultConstant;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import com.base.sbc.module.taskassignment.mapper.TaskAssignmentMapper;
import com.base.sbc.module.taskassignment.service.TaskAssignmentService;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：任务分配 service类
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Service
public class TaskAssignmentServiceImpl extends BaseServiceImpl<TaskAssignmentMapper, TaskAssignment> implements TaskAssignmentService {

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveTaskAssignment(TaskAssignmentDTO saveTaskAssignment) {
        if (ObjectUtil.isEmpty(saveTaskAssignment)) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        // 查询是否存在相同数据，包括：品牌/虚拟部门/品类/人员/触发菜单
        taskAssignmentComparison(saveTaskAssignment, false);
        // 如果没有则保存
        return save(saveTaskAssignment);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTaskAssignment(String taskAssignmentId) {
        if (ObjectUtil.isEmpty(taskAssignmentId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        TaskAssignment taskAssignment = getById(taskAssignmentId);
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        return removeById(taskAssignment);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTaskAssignment(TaskAssignmentDTO updateTaskAssignment) {
        if (ObjectUtil.isEmpty(updateTaskAssignment) || ObjectUtil.isEmpty(updateTaskAssignment.getId())) {
            throw new OtherException(ResultConstant.OPERATION_DATA_NOT_EMPTY);
        }
        // 查询原始数据是否存在
        TaskAssignment taskAssignment = getById(updateTaskAssignment.getId());
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        // 查询是否存在相同数据，包括：品牌/虚拟部门/品类/人员/触发菜单
        taskAssignmentComparison(updateTaskAssignment, true);
        return updateById(updateTaskAssignment);
    }

    @Override
    public PageInfo<TaskAssignmentVO> queryTaskAssignmentPage(TaskAssignmentDTO queryTaskAssignment) {
        PageHelper.startPage(queryTaskAssignment.getPage().getPageNum(), queryTaskAssignment.getPage().getPageSize());
        LambdaQueryWrapper<TaskAssignment> taskAssignmentQueryWrapper = new LambdaQueryWrapper<>();
        taskAssignmentQueryWrapper.eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getBrand()), TaskAssignment::getBrand, queryTaskAssignment.getBrand())
                .eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getVirtualDeptId()), TaskAssignment::getVirtualDeptId, queryTaskAssignment.getVirtualDeptId())
                .eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getProdCategory1st()), TaskAssignment::getProdCategory1st, queryTaskAssignment.getProdCategory1st())
                .eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getProdCategory()), TaskAssignment::getProdCategory, queryTaskAssignment.getProdCategory())
                .eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getProdCategory2nd()), TaskAssignment::getProdCategory2nd, queryTaskAssignment.getProdCategory2nd())
                .eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getProdCategory3rd()), TaskAssignment::getProdCategory3rd, queryTaskAssignment.getProdCategory3rd())
                .eq(ObjectUtil.isNotEmpty(queryTaskAssignment.getUserId()), TaskAssignment::getUserId, queryTaskAssignment.getUserId());
        if (ObjectUtil.isNotEmpty(queryTaskAssignment.getTriggerMenus())) {
            taskAssignmentQueryWrapper.eq(TaskAssignment::getTriggerMenus, queryTaskAssignment.getTriggerMenus().split(","));
        }
        List<TaskAssignment> taskAssignmentList = list(taskAssignmentQueryWrapper);
        PageInfo<TaskAssignment> taskAssignmentPageInfo = new PageInfo<>(taskAssignmentList);
        PageInfo<TaskAssignmentVO> newTaskAssignmentPageInfo = new PageInfo<>();
        BeanUtil.copyProperties(taskAssignmentPageInfo, newTaskAssignmentPageInfo);
        if (ObjectUtil.isNotEmpty(taskAssignmentList)) {
            newTaskAssignmentPageInfo.setList(BeanUtil.copyToList(taskAssignmentList, TaskAssignmentVO.class));
        }
        return newTaskAssignmentPageInfo;
    }

    @Override
    public TaskAssignmentVO queryTaskAssignmentDetail(String taskAssignmentId) {
        if (ObjectUtil.isEmpty(taskAssignmentId)) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        TaskAssignment taskAssignment = getById(taskAssignmentId);
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        return BeanUtil.copyProperties(taskAssignment, TaskAssignmentVO.class);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableDisableTaskAssignment(TaskAssignmentDTO enableDisableTaskAssignment) {
        if (ObjectUtil.isEmpty(enableDisableTaskAssignment) || ObjectUtil.isEmpty(enableDisableTaskAssignment.getId())) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        if (ObjectUtil.isEmpty(GeneralFlagEnum.getValueByCode(enableDisableTaskAssignment.getEnableFlag()))) {
            throw new OtherException(ResultConstant.ENABLE_DISABLED_STATUS_ERROR);
        }
        TaskAssignment taskAssignment = getById(enableDisableTaskAssignment.getId());
        if (ObjectUtil.isEmpty(taskAssignment)) {
            throw new OtherException(ResultConstant.DATA_NOT_EXIST_REFRESH_TRY_AGAIN);
        }
        taskAssignment.setEnableFlag(enableDisableTaskAssignment.getEnableFlag());
        return updateById(taskAssignment);
    }

    /**
     * 比对任务分配是否存在相同的数据
     *
     * @param needComparisonTaskAssignment 需要比对的任务分配数据
     * @param excludeSelfFlag              排除自身状态 true-排除（编辑） false-不排除（新增）
     */
    private void taskAssignmentComparison(TaskAssignmentDTO needComparisonTaskAssignment, Boolean excludeSelfFlag) {
        LambdaQueryWrapper<TaskAssignment> taskAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (excludeSelfFlag) {
            // 编辑的时候排除自身
            String id = needComparisonTaskAssignment.getId();
            taskAssignmentLambdaQueryWrapper.ne(TaskAssignment::getId, id);
        }
        taskAssignmentLambdaQueryWrapper.eq(TaskAssignment::getBrand, needComparisonTaskAssignment.getBrand())
                .eq(TaskAssignment::getBrandName, needComparisonTaskAssignment.getBrandName())
                .eq(TaskAssignment::getVirtualDeptId, needComparisonTaskAssignment.getVirtualDeptId())
                .eq(TaskAssignment::getVirtualDeptName, needComparisonTaskAssignment.getVirtualDeptName())
                .eq(TaskAssignment::getProdCategory1st, needComparisonTaskAssignment.getProdCategory1st())
                .eq(TaskAssignment::getProdCategory1stName, needComparisonTaskAssignment.getProdCategory1stName())
                .eq(TaskAssignment::getProdCategory, needComparisonTaskAssignment.getProdCategory())
                .eq(TaskAssignment::getProdCategoryName, needComparisonTaskAssignment.getProdCategoryName())
                .eq(TaskAssignment::getProdCategory2nd, needComparisonTaskAssignment.getProdCategory2nd())
                .eq(TaskAssignment::getProdCategory2ndName, needComparisonTaskAssignment.getProdCategory2ndName())
                .eq(TaskAssignment::getProdCategory3rd, needComparisonTaskAssignment.getProdCategory3rd())
                .eq(TaskAssignment::getProdCategory3rdName, needComparisonTaskAssignment.getProdCategory3rdName())
                .eq(TaskAssignment::getUserId, needComparisonTaskAssignment.getUserId())
                .eq(TaskAssignment::getUserName, needComparisonTaskAssignment.getUserName())
                .and(item -> {
                    String triggerMenus = needComparisonTaskAssignment.getTriggerMenus();
                    if (ObjectUtil.isEmpty(triggerMenus)) {
                        throw new OtherException(ResultConstant.PLEASE_CHOOSE_TRIGGER_MENU);
                    }
                    String[] triggerMenuArray = triggerMenus.split(",");
                    for (String triggerMenu : triggerMenuArray) {
                        item.or().like(TaskAssignment::getTriggerMenus, triggerMenu);
                    }
                });
        List<TaskAssignment> taskAssignmentList = list(taskAssignmentLambdaQueryWrapper);
        // 存在相同任务分配抛出异常
        if (ObjectUtil.isNotEmpty(taskAssignmentList)) {
            throw new OtherException(ResultConstant.TASK_ASSIGNMENT_EXIST);
        }
    }
}
