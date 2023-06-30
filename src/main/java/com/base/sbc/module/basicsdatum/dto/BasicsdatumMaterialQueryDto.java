package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料列表页面查询条件dto
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料列表查询")
public class BasicsdatumMaterialQueryDto extends Page {
	private static final long serialVersionUID = 1L;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 物料名称 */
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	/** 默认供应商名称 */
	@ApiModelProperty(value = "默认供应商名称")
	private String supplierName;
	/** 类别（物料分类id） */
	@ApiModelProperty(value = "类别（物料分类id）")
	private String categoryId;
	/** 类别（物料分类ids） */
	@ApiModelProperty(value = "类别（物料分类ids）")
	private String categoryIds;
	/** 类别名称（物料分类名称） */
	@ApiModelProperty(value = "类别名称（物料分类名称）")
	private String categoryName;
}
