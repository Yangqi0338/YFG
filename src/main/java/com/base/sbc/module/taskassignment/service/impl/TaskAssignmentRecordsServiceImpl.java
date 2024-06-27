/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentRecordsDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignmentRecords;
import com.base.sbc.module.taskassignment.mapper.TaskAssignmentRecordsMapper;
import com.base.sbc.module.taskassignment.service.TaskAssignmentRecordsService;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentRecordsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 类描述：任务分配-触发记录 service类
 * @author XHTE
 * @create 2024/6/27
 */
@Service
public class TaskAssignmentRecordsServiceImpl extends BaseServiceImpl<TaskAssignmentRecordsMapper, TaskAssignmentRecords> implements TaskAssignmentRecordsService {

    @Override
    public PageInfo<TaskAssignmentRecordsVO> queryTaskAssignmentRecordsPage(TaskAssignmentRecordsDTO queryTaskAssignmentRecords) {
       Page<Object> objects = PageHelper.startPage(queryTaskAssignmentRecords);
       BaseQueryWrapper<TaskAssignmentRecords> qw = new BaseQueryWrapper<>();
       List<TaskAssignmentRecords> list = list(qw);
       return new PageInfo<>(BeanUtil.copyToList(list, TaskAssignmentRecordsVO.class));
    }

}
