/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternlibrary.vo.ExcelExportVO;
import com.base.sbc.module.tasklist.constants.ResultConstant;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDetailDTO;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import com.base.sbc.module.tasklist.enums.TaskListTaskTypeEnum;
import com.base.sbc.module.tasklist.mapper.TaskListDetailMapper;
import com.base.sbc.module.tasklist.service.TaskListDetailService;
import com.base.sbc.module.tasklist.vo.ExportStyleMarkingIssuedExcelVO;
import com.base.sbc.module.tasklist.vo.ExportTaskListExcelVO;
import com.base.sbc.module.tasklist.vo.TaskListDetailVO;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务列表详情 Service
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Service
@Slf4j
public class TaskListDetailServiceImpl extends BaseServiceImpl<TaskListDetailMapper, TaskListDetail> implements TaskListDetailService {

    @Override
    public PageInfo<TaskListDetailVO> queryTaskListDetailPage(QueryPageTaskListDetailDTO queryPageTaskListDetail) {
        if (queryPageTaskListDetail.getExcelFlag().equals(0)) {
            PageHelper.startPage(queryPageTaskListDetail);
        }
        LambdaQueryWrapper<TaskListDetail> taskListDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        taskListDetailLambdaQueryWrapper.eq(TaskListDetail::getTaskListId, queryPageTaskListDetail.getTaskListId());
        taskListDetailLambdaQueryWrapper.orderByDesc(TaskListDetail::getCreateDate);
        List<TaskListDetail> taskListDetailList = list(taskListDetailLambdaQueryWrapper);
        PageInfo<TaskListDetail> taskListDetailPageInfo = new PageInfo<>(taskListDetailList);
        return CopyUtil.copy(taskListDetailPageInfo, TaskListDetailVO.class);
    }

    @Override
    public void exportTaskListDetailExcel(QueryPageTaskListDetailDTO queryPageTaskListDetail, HttpServletResponse response) {
        // 设置为导出的类型
        queryPageTaskListDetail.setExcelFlag(1);
        PageInfo<TaskListDetailVO> taskListDetailPageInfo = queryTaskListDetailPage(queryPageTaskListDetail);
        List<TaskListDetailVO> taskListDetailList = taskListDetailPageInfo.getList();
        if (ObjectUtil.isEmpty(taskListDetailList)) {
            throw new OtherException(ResultConstant.NO_DATA_EXPORT);
        }
        try {
            // 转成导出任务列表的对象
            List<ExportStyleMarkingIssuedExcelVO> exportStyleMarkingIssuedExcelList = new ArrayList<>(taskListDetailList.size());
            for (TaskListDetailVO taskListDetail : taskListDetailList) {
                ExportStyleMarkingIssuedExcelVO exportStyleMarkingIssuedExcel = BeanUtil.copyProperties(taskListDetail, ExportStyleMarkingIssuedExcelVO.class);
                exportStyleMarkingIssuedExcel.setSyncResult(TaskListTaskTypeEnum.getValueByCode(taskListDetail.getSyncResult()));
                exportStyleMarkingIssuedExcelList.add(exportStyleMarkingIssuedExcel);
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("任务列表详情数据", "utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExportStyleMarkingIssuedExcelVO.class).sheet("任务列表数据").doWrite(exportStyleMarkingIssuedExcelList);
        } catch (IOException e) {
            log.error("导出任务列表 Excel 失败，失败原因：{}", e.getMessage());
        }
    }

}
