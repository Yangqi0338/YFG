/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-物料档案-供应商报价 实体类
 *
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPrice
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-26 17:57:25
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_material_price")
@ApiModel("基础资料-物料档案-供应商报价 BasicsdatumMaterialPrice")
public class BasicsdatumMaterialPrice extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 供应商id */
	@ApiModelProperty(value = "供应商id")
	private String supplierId;
	/** 供应商名称 */
	@ApiModelProperty(value = "供应商名称")
	private String supplierName;
	/** 颜色 */
	@ApiModelProperty(value = "颜色")
	private String color;
	/** 规格 */
	@ApiModelProperty(value = "规格")
	private String width;
	/** 采购报价 */
	@ApiModelProperty(value = "采购报价")
	private BigDecimal quotationPrice;
	/** 币种 */
	@ApiModelProperty(value = "币种")
	private String currency;
	/** 币种 */
	@ApiModelProperty(value = "币种名称")
	private String currencyName;
	/** 订货周期 */
	@ApiModelProperty(value = "订货周期")
	private String orderDay;
	/** 生产周期 */
	@ApiModelProperty(value = "生产周期")
	private String productionDay;
	/** 起订量 */
	@ApiModelProperty(value = "起订量")
	private Integer minimumOrderQuantity;
	/** 每色起订量 */
	@ApiModelProperty(value = "每色起订量")
	private Integer minimumOrderQuantityColor;
	/** 颜色 */
	@ApiModelProperty(value = "颜色")
	private String colorName;
	/** 规格 */
	@ApiModelProperty(value = "规格")
	private String widthName;
	/** 供应商料号 */
	@ApiModelProperty(value = "供应商料号")
	private String supplierMaterialCode;
	/** FOB */
	@ApiModelProperty(value = "FOB")
	private String fob;
	/** 是否默认 */
	@ApiModelProperty(value = "是否默认")
	private Boolean selectFlag;

	/**
	 * 下发状态:0未下发,1已下发
	 */
	@ApiModelProperty(value = "下发状态:0未下发,1已下发")
	private String scmStatus;


	@TableField(exist = false)
	private Set<String> indexList;

	@TableField(exist = false)
	private String index;
}
