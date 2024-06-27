/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.taskassignment.dto.TaskAssignmentRecordsDTO;
import com.base.sbc.module.taskassignment.entity.TaskAssignmentRecords;
import com.base.sbc.module.taskassignment.vo.TaskAssignmentRecordsVO;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：任务分配-触发记录 service类
 * @author XHTE
 * @create 2024/6/27
 */
public interface TaskAssignmentRecordsService extends BaseService<TaskAssignmentRecords>{

    PageInfo<TaskAssignmentRecordsVO> queryTaskAssignmentRecordsPage(TaskAssignmentRecordsDTO queryTaskAssignmentRecords);


}