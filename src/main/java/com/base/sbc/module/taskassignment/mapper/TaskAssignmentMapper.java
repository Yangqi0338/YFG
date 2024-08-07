/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.taskassignment.dto.QueryTaskAssignmentDTO;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentRecordsVO;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：任务分配 dao类
 *
 * @author XHTE
 * @create 2024/6/27
 */
@Mapper
public interface TaskAssignmentMapper extends BaseMapper<TaskAssignment> {

    /**
     * 查询任务分配列表分页
     *
     * @param queryTaskAssignment 查询条件
     * @return 任务分配列表分页
     */
    List<TaskAssignmentVO> queryTaskAssignmentPage(
            @Param(Constants.WRAPPER) BaseQueryWrapper<TaskAssignmentVO> qw,
            @Param("queryTaskAssignment") QueryTaskAssignmentDTO queryTaskAssignment);

    /**
     * 查询任务分配对应的触发记录数量
     *
     * @param taskAssignmentIdList 任务分配 ID 集合
     * @return 数量
     */
    List<TaskAssignmentRecordsVO> queryTaskAssignmentRecordsCount(
            @Param("taskAssignmentIdList") List<String> taskAssignmentIdList);

}