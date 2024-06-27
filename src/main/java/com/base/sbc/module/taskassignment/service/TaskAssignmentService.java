/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentVO;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：任务分配 service类
 * @author XHTE
 * @create 2024/6/27
 */
public interface TaskAssignmentService extends BaseService<TaskAssignment>{

    PageInfo<TaskAssignmentVO> queryTaskAssignmentPage(TaskAssignmentDTO queryTaskAssignment);

}