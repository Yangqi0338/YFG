package com.base.sbc.module.basicsdatum.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年8月2日
 */
@Data
public class BasicsdatumMaterialWidthsSaveDto {
	@NotBlank(message = "物料编码必填")
	@ApiModelProperty(value = "物料编码", required = true)
	private String materialCode;
	@ApiModelProperty(value = "规格编码", required = true)
	private String width;
	@ApiModelProperty(value = "门幅/规格名称")
	private String widthName;
}
