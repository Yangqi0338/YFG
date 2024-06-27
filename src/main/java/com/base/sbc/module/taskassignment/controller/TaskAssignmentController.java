/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.taskassignment.service.TaskAssignmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // @ApiOperation(value = "分页查询")
    // @GetMapping
    // public PageInfo<TaskAssignmentVo> page(TaskAssignmentQueryDTO dto) {
    // 	return taskAssignmentService.findPage(dto);
    // }

}































