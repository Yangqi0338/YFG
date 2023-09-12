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
	/** 类别名称（物料分类名称） */
	@ApiModelProperty(value = "类别名称（物料分类名称）")
	private String categoryName;

	@ApiModelProperty(value = "材料名称")
	private String materialCodeName;

	@ApiModelProperty(value = "物料颜色")
	private String materialColor;

	@ApiModelProperty(value = "供应商id")
	private String supplierId;

	@ApiModelProperty(value = "审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过）")
	private String confirmStatus;

	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;

	/** 下发状态(0:未发，1：下发，2失败，3重开) */
	@ApiModelProperty(value = "下发状态(0:未发，1：下发，2失败，3重开)")
	private String	distribute;
	/**
	 * 数据来源：1.新增、2.面料企划、3.其他
	 */
	@ApiModelProperty(value = "数据来源：1.新增、2.面料企划、3.其他")
	private String source;
}
