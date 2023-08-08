package com.base.sbc.module.style.dto;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 款式订货本列表:创建选择集合、添加新款到集合(订货本编码)
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月4日
 */
@Data
public class StyleOrderBookSaveDto {
	@ApiModelProperty(value = "ID，新增为-1")
	@NotBlank(message = "ID必填")
	private String id;
	@NotBlank(message = "款式编号必填")
	@ApiModelProperty(value = "款式编号")
	private String styleNo;
}
