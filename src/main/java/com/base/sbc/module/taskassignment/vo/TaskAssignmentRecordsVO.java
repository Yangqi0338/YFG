/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import com.base.sbc.module.taskassignment.entity.TaskAssignmentRecords;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：任务分配-触发记录返回值
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务分配-触发记录返回值")
public class TaskAssignmentRecordsVO extends TaskAssignmentRecords {

	private static final long serialVersionUID = 1L;

}
