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
public class BasicsdatumMaterialOldSaveDto {
	/** 物料编号 */
	@NotBlank(message = "物料编号必填")
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 旧物料编号 */
	@NotBlank(message = "旧物料编号必填")
	@ApiModelProperty(value = "旧物料编号")
	private String oldMaterialCode;
}
