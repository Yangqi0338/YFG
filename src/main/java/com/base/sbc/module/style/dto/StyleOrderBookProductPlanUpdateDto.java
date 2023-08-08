package com.base.sbc.module.style.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月12日
 */
@Data
public class StyleOrderBookProductPlanUpdateDto {
	private String id;
//	/** 商品企划状态 */
//	@ApiModelProperty(value = "商品企划状态")
//	private String productPlanStatus;
	/** 投产日期 */
	@ApiModelProperty(value = "投产日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date productionDate;
	/** 预计入仓日期 */
	@ApiModelProperty(value = "预计入仓日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date warehousDate;
	/** 紧急程度 */
	@ApiModelProperty(value = "紧急程度")
	private String urgency;
	/** 紧急程度名称 */
	@ApiModelProperty(value = "紧急程度名称")
	private String urgencyName;
	/** 倍率 */
	@ApiModelProperty(value = "倍率")
	private BigDecimal magnification;
	/** 总投产 */
	@ApiModelProperty(value = "总投产")
	private BigDecimal productionTotalNum;
	/** 尺码数量明细 */
	@ApiModelProperty(value = "尺码数量明细")
	private String sizeNums;
	/** 合计 */
	@ApiModelProperty(value = "合计")
	private BigDecimal totalNum;
}
