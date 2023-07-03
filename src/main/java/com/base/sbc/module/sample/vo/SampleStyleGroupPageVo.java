package com.base.sbc.module.sample.vo;

import java.math.BigDecimal;

import com.base.sbc.config.common.base.BaseDataEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 款式搭配列表分页数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月3日
 */
@Data
public class SampleStyleGroupPageVo extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/** 搭配编码 */
	@ApiModelProperty(value = "搭配编码")
	private String groupCode;
	/** 搭配名称 */
	@ApiModelProperty(value = "搭配名称")
	private String groupName;
	/** 款式(大货款号) */
	@ApiModelProperty(value = "款式(大货款号)")
	private String styleNo;
	/** 吊牌价 */
	@ApiModelProperty(value = "吊牌价")
	private BigDecimal tagPrice;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;

	/********************************************
	 * 左关联表************** /** 样衣图片
	 */
	@ApiModelProperty(value = "样衣图片")
	private String sampleDesignPic;
	/** 品牌 */
	@ApiModelProperty(value = "品牌")
	private String bandCode;
	/** 颜色 */
	@ApiModelProperty(value = "颜色")
	private String colorName;

}

