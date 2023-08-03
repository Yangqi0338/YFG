package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;

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
public class BasicsdatumMaterialOldQueryDto extends Page {
	private static final long serialVersionUID = 1L;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;

}

