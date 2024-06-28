/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.controller;

import brave.internal.Nullable;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.taskassignment.constants.ResultConstant;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import com.base.sbc.module.taskassignment.service.TaskAssignmentService;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 类描述：任务分配 Controller类
 *
 * @author XHTE
 * @version 1.0
 * @email 2632964533@qq.com
 * @date 创建时间：2024-6-27 15:44:44
 */
@RestController
@Api(tags = "任务分配")
@RequestMapping(value = BaseController.SAAS_URL + "/taskAssignment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    /**
     * 保存任务分配信息
     *
     * @param saveTaskAssignment 保存任务分配信息
     * @return 保存结果
     */
    @ApiOperation(value = "保存任务分配信息")
    @PostMapping("/saveTaskAssignment")
    public ApiResult<String> saveTaskAssignment(@RequestBody @Validated TaskAssignmentDTO saveTaskAssignment) {
        taskAssignmentService.saveTaskAssignment(saveTaskAssignment);
        return ApiResult.success();
    }

    /**
     * 删除任务分配信息
     *
     * @param taskAssignmentId 任务分配 ID
     * @return 删除结果
     */
    @ApiOperation(value = "删除任务分配信息")
    @PostMapping("/removeTaskAssignment")
    public ApiResult<String> removeTaskAssignment(String taskAssignmentId) {
        taskAssignmentService.removeTaskAssignment(taskAssignmentId);
        return ApiResult.success();
    }

    /**
     * 更新任务分配信息
     *
     * @param updateTaskAssignment 更新后的任务分配信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新任务分配信息")
    @PostMapping("/updateTaskAssignment")
    public ApiResult<String> updateTaskAssignment(@RequestBody @Validated TaskAssignmentDTO updateTaskAssignment) {
        taskAssignmentService.updateTaskAssignment(updateTaskAssignment);
        return ApiResult.success();
    }

    /**
     * 查询任务分配列表分页
     *
     * @param queryTaskAssignment 查询条件
     * @return 任务分配列表分页
     */
    @ApiOperation(value = "查询任务分配列表分页")
    @PostMapping("/queryTaskAssignmentPage")
    public ApiResult<PageInfo<TaskAssignmentVO>> queryTaskAssignmentPage(@RequestBody TaskAssignmentDTO queryTaskAssignment) {
        PageInfo<TaskAssignmentVO> taskAssignmentVOPageInfo = taskAssignmentService.queryTaskAssignmentPage(queryTaskAssignment);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, taskAssignmentVOPageInfo);
    }

    /**
     * 根据任务分配 ID 查询任务分配详情
     *
     * @param taskAssignmentId 任务分配 ID
     * @return 任务分配详情
     */
    @ApiOperation(value = "根据任务分配 ID 查询任务分配详情")
    @PostMapping("/queryTaskAssignmentDetail")
    public ApiResult<TaskAssignmentVO> queryTaskAssignmentDetail(String taskAssignmentId) {
        taskAssignmentService.queryTaskAssignmentDetail(taskAssignmentId);
        return ApiResult.success();
    }

    /**
     * 启用/禁用任务分配信息
     *
     * @param enableDisableTaskAssignment 启用/禁用任务分配信息
     * @return 启用/禁用结果
     */
    @ApiOperation(value = "启用/禁用任务分配信息")
    @PostMapping("/enableDisableTaskAssignment")
    public ApiResult<String> enableDisableTaskAssignment(@RequestBody TaskAssignmentDTO enableDisableTaskAssignment) {
        taskAssignmentService.enableDisableTaskAssignment(enableDisableTaskAssignment);
        return ApiResult.success();
    }


    // @ApiOperation(value = "分页查询")
    // @GetMapping
    // public PageInfo<TaskAssignmentVo> page(TaskAssignmentQueryDTO dto) {
    // 	return taskAssignmentService.findPage(dto);
    // }

}































