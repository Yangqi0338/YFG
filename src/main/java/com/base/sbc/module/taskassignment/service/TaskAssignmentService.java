/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.taskassignment.dto.QueryTaskAssignmentDTO;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 类描述：任务分配 service类
 *
 * @author XHTE
 * @create 2024/6/27
 */
public interface TaskAssignmentService extends BaseService<TaskAssignment> {

    /**
     * 保存任务分配信息
     *
     * @param saveTaskAssignment 保存任务分配信息
     * @return 保存结果
     */
    Boolean saveTaskAssignment(TaskAssignmentDTO saveTaskAssignment);

    /**
     * 删除任务分配信息
     *
     * @param taskAssignmentId 任务分配 ID
     * @return 删除结果
     */
    Boolean removeTaskAssignment(String taskAssignmentId);

    /**
     * 更新任务分配信息
     *
     * @param updateTaskAssignment 更新后的任务分配信息
     * @return 更新结果
     */
    Boolean updateTaskAssignment(TaskAssignmentDTO updateTaskAssignment);

    /**
     * 查询任务分配列表分页
     *
     * @param queryTaskAssignment 查询条件
     * @return 任务分配列表分页
     */
    PageInfo<TaskAssignmentVO> queryTaskAssignmentPage(QueryTaskAssignmentDTO queryTaskAssignment);

    /**
     * 根据任务分配 ID 查询任务分配详情
     *
     * @param taskAssignmentId 任务分配 ID
     * @return 任务分配详情
     */
    TaskAssignmentVO queryTaskAssignmentDetail(String taskAssignmentId);

    /**
     * 启用/禁用任务分配信息
     *
     * @param enableDisableTaskAssignment 启用/禁用任务分配信息
     * @return 启用/禁用结果
     */
    Boolean enableDisableTaskAssignment(TaskAssignmentDTO enableDisableTaskAssignment);

    /**
     * 查询任务分配筛选条件
     *
     * @param queryTaskAssignment 查询条件
     * @return 筛选条件
     */
    List<TaskAssignmentVO> queryTaskAssignmentFilterCriteria(TaskAssignmentDTO queryTaskAssignment);

    /**
     * 执行启用任务
     *
     * @param runTaskAssignment 执行时查询条件
     */
    void runTaskAssignment(TaskAssignmentDTO runTaskAssignment);
}