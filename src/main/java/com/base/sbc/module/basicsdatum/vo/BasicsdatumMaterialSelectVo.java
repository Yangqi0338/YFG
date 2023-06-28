package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 物料分页数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料档案下拉数据")
public class BasicsdatumMaterialSelectVo {

	/** 物料id */
	@ApiModelProperty(value = "物料id")
	private String id;
	/** 物料名称 */
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;

}
