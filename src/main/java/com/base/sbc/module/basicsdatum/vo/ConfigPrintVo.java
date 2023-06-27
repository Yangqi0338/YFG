package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConfigPrintVo extends BaseDataEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/** 顺序 */
	@ApiModelProperty(value = "顺序")
	private Integer sort;
	/** 单据类别 */
	@ApiModelProperty(value = "单据类别")
	private String billType;
	/** 编码 */
	@ApiModelProperty(value = "编码")
	private String code;
	/** 名称 */
	@ApiModelProperty(value = "名称")
	private String name;
	/** 类型(ureport) */
	@ApiModelProperty(value = "类型(ureport)")
	private String printType;
	/** 示例数据 */
	@ApiModelProperty(value = "示例数据")
	private String dataJson;

	/** 是否默认选中(0否,1是) */
	@ApiModelProperty(value = "是否默认选中(0否,1是)")
	private String selectFlag;
	/** 是否停用(0否,1是) */
	@ApiModelProperty(value = "是否停用(0否,1是)")
	private String stopFlag;
	/** 备注 */
	@ApiModelProperty(value = "备注")
	private String remarks;
}
