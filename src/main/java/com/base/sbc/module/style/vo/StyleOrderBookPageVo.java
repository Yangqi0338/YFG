package com.base.sbc.module.style.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 订货本列表返回数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月10日
 */
@Data
public class StyleOrderBookPageVo {
	@ApiModelProperty(value = "id")
	private String id;
	@ApiModelProperty(value = "订货Id")
	private String bookId;
	@ApiModelProperty(value = "订货本编号")
	private String orderBookCode;
	@ApiModelProperty(value = "款号")
	private String styleNo;
	@ApiModelProperty(value = "款式图片")
	private String sampleDesignPic;

	@ApiModelProperty(value = "颜色名称")
	private String colorName;
	@ApiModelProperty(value = "是否搭配")
	private String groupFlag;
	@ApiModelProperty(value = "搭配编码")
	private String groupCode;
	/** 设计状态 */
	@ApiModelProperty(value = "订货本状态(0未分配 1已分配)")
	private String orderBookStatus;
	/** 设计状态 */
	@ApiModelProperty(value = "设计状态")
	private String designStatus;
	/** 4倍价 */
	@ApiModelProperty(value = "4倍价")
	private BigDecimal quadruplePrice;
	/** 设计说明 */
	@ApiModelProperty(value = "设计说明")
	private String designSay;
	/** 企划状态 */
	@ApiModelProperty(value = "企划状态")
	private String planStatus;
	/** 企划人员 */
	@ApiModelProperty(value = "企划人员")
	private String planUserId;
	/** 企划人员 */
	@ApiModelProperty(value = "企划人员")
	private String planUserName;
	/** 面料吊牌价 */
	@ApiModelProperty(value = "面料吊牌价")
	private BigDecimal fabricTagPrice;
	/** 商品企划状态 */
	@ApiModelProperty(value = "商品企划状态")
	private String productPlanStatus;
	/** 商企人员 */
	@ApiModelProperty(value = "商企人员")
	private String productPlanUserId;
	/** 商企人员 */
	@ApiModelProperty(value = "商企人员")
	private String productPlanUserName;
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

	@ApiModelProperty(value = "订货本图片")
	private String imageUrl;
	@ApiModelProperty(value = "吊牌价（套装-配套/单款-配色）")
	private String tagPrice;
	@ApiModelProperty(value = "上会状态（0下会 1上会）")
	private String meetFlag;
	@ApiModelProperty(value = "锁定状态（0未锁 1锁定）")
	private String lockFlag;

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
	/* 品类名称*/
	@ApiModelProperty(value = "品类名称")
	private String prodCategoryName;
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
	@ApiModelProperty(value = "波段(编码)")
	private String bandCode;
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
