package com.base.sbc.module.sample.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 款式订货本列表:修改吊牌价（套装-配套/单款-配色）
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月4日
 */
@Data
public class SampleStyleOrderBookPriceUpdateDto {
	@NotBlank(message = "大货款号必填")
	@ApiModelProperty(value = "大货款号")
	private String styleNo;
	@NotBlank(message = "是否套装必填")
	@ApiModelProperty(value = "是否套装(0单款，1套装)")
	private String groupFlag;
	@ApiModelProperty(value = "套装编码")
	private String groupCode;
	@NotNull(message = "吊牌价必填")
	@ApiModelProperty(value = "吊牌价")
	private BigDecimal tagPrice;
}
