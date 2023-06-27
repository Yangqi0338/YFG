package com.base.sbc.module.basicsdatum.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存打印模板实体
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("保存打印模板实体")
public class AddRevampConfigPrintDto {
	@NotBlank(message = "ID必填,新增-1")
	@ApiModelProperty(value = "id", required = true)
	private String id;
	/** 顺序 */
	@ApiModelProperty(value = "顺序")
	private Integer sort;
	/** 单据类别 */
	@NotBlank(message = "单据类别必填")
	@ApiModelProperty(value = "单据类别", required = true)
	private String billType;
	/** 编码 */
	@NotBlank(message = "编码必填")
	@ApiModelProperty(value = "编码", required = true)
	private String code;
	/** 名称 */
	@NotBlank(message = "名称必填")
	@ApiModelProperty(value = "名称", required = true)
	private String name;
	/** 类型(ureport) */
	@NotBlank(message = "类型必填")
	@ApiModelProperty(value = "类型(ureport)", required = true)
	private String printType;
	/** 示例数据 */
	@NotBlank(message = "示例数据必填")
	@ApiModelProperty(value = "示例数据", required = true)
	private String dataJson;
	/** 是否默认选中(0否,1是) */
	@NotBlank(message = "是否默认必填")
	@ApiModelProperty(value = "是否默认选中(0否,1是)", required = true)
	private String selectFlag;
	/** 是否停用(0否,1是) */
	@NotBlank(message = "否停用必填")
	@ApiModelProperty(value = "是否停用(0否,1是)", required = true)
	private String stopFlag;
	/** 备注 */
	@ApiModelProperty(value = "备注")
	private String remarks;
}
