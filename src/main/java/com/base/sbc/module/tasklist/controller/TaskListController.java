/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.controller;

import com.base.sbc.client.flowable.vo.FlowQueryVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.task.vo.FlowTaskDto;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.TaskListDTO;
import com.base.sbc.module.tasklist.service.TaskListService;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务列表 Controller
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Api(tags = "任务列表")
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/taskList")
@RequiredArgsConstructor
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    /**
     * 新增任务列表
     * 所有任务通过此接口进行新增
     *
     * @param taskList 任务列表信息
     * @return 新增结果
     */
    @PostMapping(value = "/saveTaskList")
    @ApiOperation(value = "新增任务列表")
    public ApiResult<String> saveTaskList(@RequestBody TaskListDTO taskList) {
        taskListService.saveTaskList(taskList);
        return ApiResult.success("操作成功");
    }

    /**
     * 修改任务列表
     *
     * @param taskList 任务列表信息
     * @return 修改结果
     */
    @PostMapping(value = "/updateTaskList")
    @ApiOperation(value = "修改任务列表")
    public ApiResult<String> updateTaskList(@RequestBody TaskListDTO taskList) {
        taskListService.updateTaskList(taskList);
        return ApiResult.success("操作成功");
    }

    /**
     * 查询任务列表列表分页
     *
     * @param queryPageTaskList 查询条件
     * @return 任务列表列表分页
     */
    @PostMapping(value = "/queryTaskListPage")
    @ApiOperation(value = "查询任务列表列表分页")
    public ApiResult<PageInfo<TaskListVO>> queryTaskListPage(@RequestBody QueryPageTaskListDTO queryPageTaskList) {
        PageInfo<TaskListVO> taskListPageInfo = taskListService.queryTaskListPage(queryPageTaskList);
        return ApiResult.success("操作成功", taskListPageInfo);
    }

    /**
     * 导出任务列表 Excel
     *
     * @param queryPageTaskList 查询条件
     * @param response          响应数据
     * @return 导出结果
     */
    @GetMapping(value = "/exportTaskListExcel")
    @ApiOperation(value = "导出任务列表 Excel")
    public ApiResult<String> exportTaskListExcel(QueryPageTaskListDTO queryPageTaskList, HttpServletResponse response) {
        taskListService.exportTaskListExcel(queryPageTaskList, response);
        return ApiResult.success("导出成功");
    }

}
