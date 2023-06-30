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
@ApiModel("当前物料报价规格下拉")
public class BasicsdatumMaterialWidthSelectVo {

	@ApiModelProperty(value = "规格代码")
	private String code;

	@ApiModelProperty(value = "名称")
	private String name;
}
