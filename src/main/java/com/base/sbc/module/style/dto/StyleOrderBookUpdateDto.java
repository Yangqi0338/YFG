package com.base.sbc.module.style.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 款式订货本列表:修改图片/修改状态：锁定、解锁/修改状态：上会、撤销
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月4日
 */
@Data
public class StyleOrderBookUpdateDto {
	/** 订货本编号 */
	@NotBlank(message = "ID必填")
	@ApiModelProperty(value = "ID")
	private String id;
	/** 图片地址 */
	@ApiModelProperty(value = "图片地址")
	private String imageUrl;
	/** 上会标记(0未上会，1已上会) */
	@ApiModelProperty(value = "上会标记(0未上会，1已上会)")
	private String meetFlag;
	/** 锁定标记(0未锁定，1已锁定) */
	@ApiModelProperty(value = "锁定标记(0未锁定，1已锁定)")
	private String lockFlag;
}
