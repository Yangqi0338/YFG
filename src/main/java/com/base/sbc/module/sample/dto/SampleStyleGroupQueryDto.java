package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 款式搭配列表搜索条件
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月3日
 */
@Data
public class SampleStyleGroupQueryDto extends Page {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String companyCode;
	/** 搭配编码 */
	@ApiModelProperty(value = "搭配编码")
	private String groupCode;
	/** 搭配名称 */
	@ApiModelProperty(value = "搭配名称")
	private String groupName;
	/** 款式(大货款号) */
	@ApiModelProperty(value = "款式(大货款号)")
	private String styleNo;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
}
