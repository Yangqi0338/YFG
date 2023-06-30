package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 物料颜色查询
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料颜色查询")
public class BasicsdatumMaterialColorQueryDto extends Page {
	private static final long serialVersionUID = 1L;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;

}