/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.enums.PatternLibraryStatusEnum;
import com.base.sbc.module.patternlibrary.vo.ExcelExportVO;
import com.base.sbc.module.tasklist.constants.ResultConstant;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.TaskListDTO;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import com.base.sbc.module.tasklist.enums.TaskListTaskTypeEnum;
import com.base.sbc.module.tasklist.mapper.TaskListMapper;
import com.base.sbc.module.tasklist.service.TaskListDetailService;
import com.base.sbc.module.tasklist.service.TaskListService;
import com.base.sbc.module.tasklist.vo.ExportTaskListExcelVO;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务列表 ServiceImpl
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Service
@Slf4j
public class TaskListServiceImpl extends BaseServiceImpl<TaskListMapper, TaskList> implements TaskListService {

    @Autowired
    private TaskListDetailService taskListDetailService;

    @Override
    public Boolean saveTaskList(TaskListDTO taskList) {
        if (!save(taskList)) {
            throw new OtherException(StrUtil.format("「{}」{}", taskList.getTaskName(), ResultConstant.TASK_LIST_SAVE_FAILED));
        }
        List<TaskListDetail> taskListDetailList = taskList.getTaskListDetailList();
        if (!taskListDetailService.saveBatch(taskListDetailList)) {
            throw new OtherException(StrUtil.format("「{}」{}", taskList.getTaskName(), ResultConstant.TASK_LIST_DETAIL_SAVE_FAILED));
        }
        return Boolean.TRUE;
    }

    @Override
    public PageInfo<TaskListVO> queryTaskListPage(QueryPageTaskListDTO queryPageTaskList) {
        if (queryPageTaskList.getExcelFlag().equals(0)) {
            // 不是导出，则需要分页
            PageHelper.startPage(queryPageTaskList);
        }
        LambdaQueryWrapper<TaskList> taskListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TaskList> taskListList = list(taskListLambdaQueryWrapper);
        PageInfo<TaskList> taskListPageInfo = new PageInfo<>(taskListList);
        return CopyUtil.copy(taskListPageInfo, TaskListVO.class);
    }

    public void exportTaskListExcel(QueryPageTaskListDTO queryPageTaskList, HttpServletResponse response) {
        // 设置为导出的类型
        queryPageTaskList.setExcelFlag(1);
        PageInfo<TaskListVO> taskListPageInfo = queryTaskListPage(queryPageTaskList);
        List<TaskListVO> taskListList = taskListPageInfo.getList();
        if (ObjectUtil.isEmpty(taskListList)) {
            throw new OtherException(ResultConstant.NO_DATA_EXPORT);
        }
        try {
            // 转成导出任务列表的对象
            List<ExportTaskListExcelVO> exportTaskListExcelList = new ArrayList<>(taskListList.size());
            for (TaskListVO taskList : taskListList) {
                ExportTaskListExcelVO exportTaskListExcel = BeanUtil.copyProperties(taskList, ExportTaskListExcelVO.class);
                exportTaskListExcel.setTaskType(TaskListTaskTypeEnum.getValueByCode(taskList.getTaskType()));
                exportTaskListExcel.setTaskStatus(TaskListTaskTypeEnum.getValueByCode(taskList.getTaskStatus()));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                exportTaskListExcel.setReceiveDate(simpleDateFormat.format(exportTaskListExcel.getReceiveDate()));
                exportTaskListExcelList.add(exportTaskListExcel);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("任务列表数据", "utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelExportVO.class).sheet("任务列表数据").doWrite(exportTaskListExcelList);
        } catch (IOException e) {
            log.error("导出任务列表 Excel 失败，失败原因：{}", e.getMessage());
        }
    }

}
