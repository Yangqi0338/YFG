/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.taskassignment.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.module.common.vo.BasePageInfo;
import com.base.sbc.module.taskassignment.entity.TaskAssignment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

/**
 * 类描述：任务分配入参
 * @author XHTE
 * @create 2024/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务分配入参")
@Validated
public class TaskAssignmentDTO extends TaskAssignment {

	private static final long serialVersionUID = 1L;

	/**
	 * 分页数据
	 */
	@ApiModelProperty("分页数据")
	private Page page;

}
