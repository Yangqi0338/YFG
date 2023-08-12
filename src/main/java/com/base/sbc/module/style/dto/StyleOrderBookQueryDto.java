package com.base.sbc.module.style.dto;

import com.base.sbc.config.common.base.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月4日
 */
@Data
public class StyleOrderBookQueryDto extends Page {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "关键字")
	private String search;
	@ApiModelProperty(value = "企业编码")
	private String companyCode;
	@ApiModelProperty(value = "类型：all/design/plan/productPlan")
	private String type;
	@ApiModelProperty(value = "当前用户ID")
	private String userId;
	@ApiModelProperty(value = "编号")
	private String orderBookCode;
	@ApiModelProperty(value = "款号")
	private String styleNo;
	@ApiModelProperty(value = "设计款号")
	private String designNo;
}
