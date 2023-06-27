package com.base.sbc.module.basicsdatum.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * 物料供应商报价数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料供应商报价列表查询")
public class BasicsdatumMaterialPricePageVo extends BaseDataEntity<String> {
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
	/** 订货周期 */
	@ApiModelProperty(value = "订货周期")
	private BigDecimal orderDay;
	/** 生产周期 */
	@ApiModelProperty(value = "生产周期")
	private BigDecimal productionDay;
	/** 起订量 */
	@ApiModelProperty(value = "起订量")
	private Integer minimumOrderQuantity;
	/** 每色起订量 */
	@ApiModelProperty(value = "每色起订量")
	private Integer minimumOrderQuantityColor;
}
