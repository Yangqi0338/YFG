package com.base.sbc.module.sample.dto;

import java.math.BigDecimal;

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
public class SampleStyleOrderBookDesignUpdateDto {

	private String id;
//	/** 设计状态 */
//	@ApiModelProperty(value = "订货本状态(0未分配 1已分配)")
//	private String orderBookStatus;
//	/** 设计状态 */
//	@ApiModelProperty(value = "设计状态")
//	private String designStatus;
	/** 4倍价 */
	@ApiModelProperty(value = "4倍价")
	private BigDecimal quadruplePrice;
	/** 设计说明 */
	@ApiModelProperty(value = "设计说明")
	private String designSay;

}
