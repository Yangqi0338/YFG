package com.base.sbc.module.sample.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存款式搭配
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月3日
 */
@Data
public class SampleStyleGroupSaveDto {
	/** id */
	@ApiModelProperty(value = "id")
	@NotBlank(message = "ID必填")
	private String id;
	/** 搭配名称 */
	@ApiModelProperty(value = "搭配名称")
	@NotBlank(message = "名称必填")
	private String groupName;
	/** 款式(大货款号) */
	@NotBlank(message = "款式必填")
	@ApiModelProperty(value = "款式(大货款号)")
	private String styleNo;
	/** 吊牌价 */
	@NotNull(message = "吊牌价必填")
	@ApiModelProperty(value = "吊牌价")
	private BigDecimal tagPrice;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
}
