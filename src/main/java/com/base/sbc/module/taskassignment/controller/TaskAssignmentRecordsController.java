/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.taskassignment.constants.ResultConstant;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentDTO;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentRecordsDTO;
import com.base.sbc.module.taskassignment.service.TaskAssignmentRecordsService;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentRecordsVO;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：任务分配-触发记录 Controller类
 *
 * @author XHTE
 * @version 1.0
 * @email 2632964533@qq.com
 * @date 创建时间：2024-6-27 15:44:45
 */
@RestController
@Api(tags = "任务分配-触发记录")
@RequestMapping(value = BaseController.SAAS_URL + "/taskAssignmentRecords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class TaskAssignmentRecordsController {

    @Autowired
    private TaskAssignmentRecordsService taskAssignmentRecordsService;

    /**
     * 根据任务分配 ID 查询触发记录列表分页
     *
     * @param queryTaskAssignmentRecords 查询条件
     * @return 触发记录列表分页
     */
    @ApiOperation(value = "根据任务分配 ID 查询触发记录列表分页")
    @PostMapping("/queryTaskAssignmentRecordsPage")
    public ApiResult<PageInfo<TaskAssignmentRecordsVO>> queryTaskAssignmentRecordsPage(@RequestBody TaskAssignmentRecordsDTO queryTaskAssignmentRecords) {
        PageInfo<TaskAssignmentRecordsVO> taskAssignmentRecordsPageInfo = taskAssignmentRecordsService.queryTaskAssignmentRecordsPage(queryTaskAssignmentRecords);
        return ApiResult.success(ResultConstant.OPERATION_SUCCESS, taskAssignmentRecordsPageInfo);
    }
}































