package com.base.sbc.module.style.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分配任务
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月11日
 */
@Data
public class StyleOrderBookUserUpdateDto {
	@NotBlank(message = "ID必填")
	@ApiModelProperty(value = "ID,多个逗号隔开")
	private String id;
	@NotBlank(message = "企划人员必填")
	@ApiModelProperty(value = "企划人员")
	private String planUserId;
	@NotBlank(message = "企划人员必填")
	@ApiModelProperty(value = "企划人员")
	private String planUserName;
	@NotBlank(message = "商企人员必填")
	@ApiModelProperty(value = "商企人员")
	private String productPlanUserId;
	@NotBlank(message = "商企人员必填")
	@ApiModelProperty(value = "商企人员")
	private String productPlanUserName;
}
