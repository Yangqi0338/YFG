/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDetailDTO;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import com.base.sbc.module.tasklist.mapper.TaskListDetailMapper;
import com.base.sbc.module.tasklist.service.TaskListDetailService;
import com.base.sbc.module.tasklist.vo.TaskListDetailVO;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务列表详情 Service
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Service
public class TaskListDetailServiceImpl extends BaseServiceImpl<TaskListDetailMapper, TaskListDetail> implements TaskListDetailService {

    @Override
    public PageInfo<TaskListDetailVO> queryTaskListDetailPage(QueryPageTaskListDetailDTO queryPageTaskListDetail) {
        PageHelper.startPage(queryPageTaskListDetail);
        LambdaQueryWrapper<TaskListDetail> taskListDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<TaskListDetail> taskListDetailList = list(taskListDetailLambdaQueryWrapper);
        PageInfo<TaskListDetail> taskListDetailPageInfo = new PageInfo<>(taskListDetailList);
        return CopyUtil.copy(taskListDetailPageInfo, TaskListDetailVO.class);
    }

}
