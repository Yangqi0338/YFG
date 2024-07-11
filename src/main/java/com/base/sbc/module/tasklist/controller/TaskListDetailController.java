/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDetailDTO;
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import com.base.sbc.module.tasklist.service.TaskListDetailService;
import com.base.sbc.module.tasklist.service.TaskListService;
import com.base.sbc.module.tasklist.vo.TaskListDetailVO;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 任务列表详情 Controller
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Api(tags = "任务列表详情")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/taskListDetail")
@RequiredArgsConstructor
public class TaskListDetailController {

    @Autowired
    private TaskListDetailService taskListDetailService;

    /**
     * 查询任务列表详情列表分页
     *
     * @param queryPageTaskListDetail 查询条件
     * @return 查询任务列表详情列表分页
     */
    @PostMapping(value = "/queryTaskListDetailPage")
    @ApiOperation(value = "查询任务列表详情列表分页")
    public ApiResult<PageInfo<TaskListDetailVO>> queryTaskListDetailPage(@RequestBody QueryPageTaskListDetailDTO queryPageTaskListDetail) {
        PageInfo<TaskListDetailVO> taskListDetailPageInfo = taskListDetailService.queryTaskListDetailPage(queryPageTaskListDetail);
        return ApiResult.success("操作成功", taskListDetailPageInfo);
    }

    /**
     * 导出任务列表详情 Excel
     *
     * @param queryPageTaskListDetail 查询条件
     * @param response 响应数据
     * @return 导出结果
     */
    @PostMapping(value = "/exportTaskListDetailExcel")
    @ApiOperation(value = "导出任务列表详情 Excel")
    public ApiResult<String> exportTaskListDetailExcel(@RequestBody QueryPageTaskListDetailDTO queryPageTaskListDetail, HttpServletResponse response) {
        taskListDetailService.exportTaskListDetailExcel(queryPageTaskListDetail, response);
        return ApiResult.success("导出成功");
    }

}
