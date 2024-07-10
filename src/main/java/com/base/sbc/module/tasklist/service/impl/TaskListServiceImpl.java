/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.mapper.TaskListMapper;
import com.base.sbc.module.tasklist.service.TaskListService;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务列表 ServiceImpl
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Service
public class TaskListServiceImpl extends BaseServiceImpl<TaskListMapper, TaskList> implements TaskListService {

    @Override
    public PageInfo<TaskListVO> findPage(QueryPageTaskListDTO queryPageTaskList) {
       PageHelper.startPage(queryPageTaskList);
       BaseQueryWrapper<TaskList> qw = new BaseQueryWrapper<>();
       List<TaskList> list = list(qw);
       return new PageInfo<>(BeanUtil.copyToList(list, TaskListVO.class));
    }

}
