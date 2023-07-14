package com.base.sbc.module.basicsdatum.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月14日
 */
@Data
public class BasicsdatumMaterialWidthGroupSaveDto {
	@NotBlank(message = "物料编码必填")
	@ApiModelProperty(value = "物料编码", required = true)
	private String materialCode;
	@NotBlank(message = "规格组编码必填")
	@ApiModelProperty(value = "规格组编码", required = true)
	private String widthGroupCode;
}
