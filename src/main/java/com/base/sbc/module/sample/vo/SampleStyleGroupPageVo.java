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
	@ApiModelProperty(value = "波段(编码)")
	private String bandCode;
	/** 颜色 */
	@ApiModelProperty(value = "颜色")
	private String colorName;

	/** 颜色规格 */
	@ApiModelProperty(value = "颜色规格")
	private String colorSpecification;
	/** 销售类型 */
	@ApiModelProperty(value = "销售类型")
	private String salesType;
	/** 是否是内饰款(0否,1:是) */
	@ApiModelProperty(value = "是否是内饰款(0否,1:是)")
	private String isTrim;
	/** 厂家 */
	@ApiModelProperty(value = "厂家")
	private String manufacturer;
	/** 厂家款号 */
	@ApiModelProperty(value = "厂家款号")
	private String manufacturerNo;
	/** 厂家颜色 */
	@ApiModelProperty(value = "厂家颜色")
	private String manufacturerColor;

	/** 款式名称 */
	@ApiModelProperty(value = "款式名称")
	private String styleName;
	/** 设计款号 */
	@ApiModelProperty(value = "设计款号")
	private String designNo;
	/* 品类名称路径:(大类/品类/中类/小类)' */
	@ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
	private String categoryName;
	/** 大类id */
	@ApiModelProperty(value = "大类id")
	private String prodCategory1st;
	/** 品类id */
	@ApiModelProperty(value = "品类id")
	private String prodCategory;
	/** 中类id */
	@ApiModelProperty(value = "中类id")
	private String prodCategory2nd;
	/** 小类 */
	@ApiModelProperty(value = "小类")
	private String prodCategory3rd;
	/** 品牌 */
	@ApiModelProperty(value = "品牌")
	private String brand;
	/** 年份 */
	@ApiModelProperty(value = "年份")
	private String year;
	/** 季节 */
	@ApiModelProperty(value = "季节")
	private String season;
	/** 月份 */
	@ApiModelProperty(value = "月份")
	private String month;
	/** 性别 */
	@ApiModelProperty(value = "性别")
	private String sex;
	/** 生产模式 */
	@ApiModelProperty(value = "生产模式")
	private String devtType;
	/** 主材料 */
	@ApiModelProperty(value = "主材料")
	private String mainMaterials;
	/** 研发材料 */
	@ApiModelProperty(value = "研发材料")
	private String rdMat;
	/** 设计师名称 */
	@ApiModelProperty(value = "设计师名称")
	private String designer;
	/** 设计师id */
	@ApiModelProperty(value = "设计师id")
	private String designerId;

}

