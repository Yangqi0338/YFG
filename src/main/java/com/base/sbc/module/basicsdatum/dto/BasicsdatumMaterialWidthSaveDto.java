package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * 物料档案规格
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料档案规格实体")
public class BasicsdatumMaterialWidthSaveDto {
	@NotBlank(message = "ID必填,新增-1")
	@ApiModelProperty(value = "id", required = true)
	private String id;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 规格代码 */
	@ApiModelProperty(value = "规格代码")
	private String widthCode;
}
