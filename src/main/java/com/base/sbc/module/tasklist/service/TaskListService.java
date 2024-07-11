/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.TaskListDTO;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageInfo;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;

/**
 * 任务列表 Service
 *
 * @author XHTE
 * @create 2024/7/10
 */
public interface TaskListService extends BaseService<TaskList> {

    /**
     * 新增任务列表
     * 所有任务通过此接口进行新增
     *
     * @param taskList 任务列表信息
     * @return 新增结果
     */
    Boolean saveTaskList(TaskListDTO taskList);

    /**
     * 查询任务列表列表分页
     *
     * @param queryPageTaskList 查询条件
     * @return 任务列表列表分页
     */
    PageInfo<TaskListVO> queryTaskListPage(QueryPageTaskListDTO queryPageTaskList);

    /**
     * 导出任务列表 Excel
     *
     * @param queryPageTaskList 查询条件
     * @param response 响应数据
     * @return 导出结果
     */
    void exportTaskListExcel(QueryPageTaskListDTO queryPageTaskList, HttpServletResponse response);

}