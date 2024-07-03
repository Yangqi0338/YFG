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
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.taskassignment.constants.ResultConstant;
import com.base.sbc.module.taskassignment.dto.QueryTaskAssignmentDTO;
import com.base.sbc.module.taskassignment.dto.QueryTaskAssignmentRecordsDTO;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentRecordsDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignmentRecords;
import com.base.sbc.module.taskassignment.enums.TriggerMenuEnum;
import com.base.sbc.module.taskassignment.mapper.TaskAssignmentRecordsMapper;
import com.base.sbc.module.taskassignment.service.TaskAssignmentRecordsService;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentRecordsVO;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：任务分配-触发记录 service类
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Service
public class TaskAssignmentRecordsServiceImpl extends BaseServiceImpl<TaskAssignmentRecordsMapper, TaskAssignmentRecords> implements TaskAssignmentRecordsService {

    @Override
    public PageInfo<TaskAssignmentRecordsVO> queryTaskAssignmentRecordsPage(
            QueryTaskAssignmentRecordsDTO queryTaskAssignmentRecords) {
        if (ObjectUtil.isEmpty(queryTaskAssignmentRecords)
                || ObjectUtil.isEmpty(queryTaskAssignmentRecords.getTaskAssignmentId())) {
            throw new OtherException(ResultConstant.PLEASE_SELECT_DATA);
        }
        String triggerMenu = queryTaskAssignmentRecords.getTriggerMenu();
        if (ObjectUtil.isEmpty(triggerMenu)) {
            throw new OtherException(ResultConstant.PLEASE_CHOOSE_TRIGGER_MENU);
        }
        if (ObjectUtil.isEmpty(TriggerMenuEnum.checkValue(triggerMenu))) {
            throw new OtherException("「" + triggerMenu + "」" + ResultConstant.TRIGGER_MENU_DOES_NOT_EXIST);
        }
        PageHelper.startPage(queryTaskAssignmentRecords.getPageNum(), queryTaskAssignmentRecords.getPageSize());
        LambdaQueryWrapper<TaskAssignmentRecords> taskAssignmentRecordsLambdaQueryWrapper = new LambdaQueryWrapper<>();

        taskAssignmentRecordsLambdaQueryWrapper
                .eq(TaskAssignmentRecords::getTaskAssignmentId, queryTaskAssignmentRecords.getTaskAssignmentId())
                .eq(TaskAssignmentRecords::getTriggerMenu, queryTaskAssignmentRecords.getTriggerMenu());

        List<TaskAssignmentRecords> taskAssignmentRecordsList = list(taskAssignmentRecordsLambdaQueryWrapper);
        PageInfo<TaskAssignmentRecords> taskAssignmentRecordsPageInfo = new PageInfo<>(taskAssignmentRecordsList);
        PageInfo<TaskAssignmentRecordsVO> newTaskAssignmentRecordsPageInfo = new PageInfo<>();
        BeanUtil.copyProperties(taskAssignmentRecordsPageInfo, newTaskAssignmentRecordsPageInfo);
        if (ObjectUtil.isNotEmpty(taskAssignmentRecordsList)) {
            newTaskAssignmentRecordsPageInfo.setList(BeanUtil.copyToList(taskAssignmentRecordsList, TaskAssignmentRecordsVO.class));
        }
        return newTaskAssignmentRecordsPageInfo;
    }

}
