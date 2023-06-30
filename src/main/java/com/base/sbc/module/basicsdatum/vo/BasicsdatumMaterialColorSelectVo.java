package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 物料规格列表数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("当前物料报价颜色 下拉查询")
public class BasicsdatumMaterialColorSelectVo {
	/** 颜色代码 */
	@ApiModelProperty(value = "颜色代码")
	private String colorCode;
	/** 颜色代码 */
	@ApiModelProperty(value = "颜色")
	private String color;
}
