package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.config.common.base.Page;

import com.base.sbc.config.dto.QueryFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 物料列表页面查询条件dto
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料列表查询")
public class BasicsdatumMaterialQueryDto  extends QueryFieldDto {
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
	 * 数据来源：1.新增、2.设计面料、
	 */
	@ApiModelProperty(value = "数据来源：1.物料档案、2.设计面料")
	private String source;
	/**
	 * 附件
	 */
	private String attachment;

	/**
	 * 附件名称
	 */
	private String attachmentName;
	/**
	 * 产品季id
	 */
	@ApiModelProperty(value = "产品季id")
	private String planningSeasonId;
	/**
	 * 是否查询库存数量 0或者为空不查询 1查询
	 */
	@ApiModelProperty(value = "是否查询库存数量")
	private String isStock;

	@ApiModelProperty(value = "供应商物料编号")
	private String	supplierFabricCode;


	@ApiModelProperty(value = "供应商物料编号")
	private String	supplierMaterialCode;

	@ApiModelProperty(value = "颜色")
	private String colorName;

	@ApiModelProperty(value = "颜色编码")
	private String colorCode;

	@ApiModelProperty(value = "供应商色号")
	private String supplierColorCode;

	@ApiModelProperty(value = "品牌")
	private List<String> brandList;

	/** 物料编号 */
	@ApiModelProperty(value = "物料编号--非模糊查询")
	private String materialCodeNoLike;


	@ApiModelProperty(value = "合并物料颜色 : 1：合并，0：不合并, 默认不合并")
	private String mergeMaterialColor = "0";
}
