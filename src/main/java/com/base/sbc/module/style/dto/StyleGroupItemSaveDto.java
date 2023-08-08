package com.base.sbc.module.style.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存搭配明细
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月3日
 */
@Data
public class StyleGroupItemSaveDto {
	/** 搭配名称 */
	@ApiModelProperty(value = "搭配编码")
	@NotBlank(message = "搭配编码不可为空")
	private String groupCode;
	/** 款式(大货款号) */
	@NotBlank(message = "款式不可为空")
	@ApiModelProperty(value = "款式(大货款号)")
	private String styleNo;
}
