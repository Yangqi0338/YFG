/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.taskassignment.entity.TaskAssignmentRecords;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：任务分配-触发记录列表分页入参
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务分配-触发记录列表分页入参")
public class QueryTaskAssignmentRecordsDTO extends Page {

	private static final long serialVersionUID = 1L;

	/**
	 * 任务分配 ID
	 */
	@ApiModelProperty("任务分配 ID")
	private String taskAssignmentId;

	/**
	 * 触发菜单（产品季总览，技术中心看板）
	 */
	@ApiModelProperty(value = "触发菜单（产品季总览，技术中心看板）")
	private String triggerMenu;

}
