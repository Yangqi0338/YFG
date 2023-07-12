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
public class SampleStyleOrderBookPlanUpdateDto {
	private String id;
//	/** 企划状态 */
//	@ApiModelProperty(value = "企划状态")
//	private String planStatus;
	/** 面料吊牌价 */
	@ApiModelProperty(value = "面料吊牌价")
	private BigDecimal fabricTagPrice;

}
