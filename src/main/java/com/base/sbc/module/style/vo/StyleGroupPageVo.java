package com.base.sbc.module.style.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * 款式搭配列表分页数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月3日
 */
@Data
public class StyleGroupPageVo extends BaseDataEntity<String> {

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
	@ApiModelProperty(value = "波段(编码)")
	private String bandCode;
	@ApiModelProperty(value = "波段(名称)")
	private String bandName;
	/**
	 * 颜色
	 */
	@ApiModelProperty(value = "颜色")
	private String colorName;

	/**
	 * 颜色规格
	 */
	@ApiModelProperty(value = "颜色规格")
	private String colorSpecification;
	/**
	 * 销售类型
	 */
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
	/**
	 * 厂家颜色
	 */
	@ApiModelProperty(value = "厂家颜色")
	private String manufacturerColor;

	/**
	 * 款式名称
	 */
	@ApiModelProperty(value = "款式名称")
	private String styleName;
	/**
	 * 设计款号
	 */
	@ApiModelProperty(value = "设计款号")
	private String designNo;
	/**
	 * 大类code
	 */
	@ApiModelProperty(value = "大类code")
	private String prodCategory1st;
	/**
	 * 大类名称
	 */
	@ApiModelProperty(value = "大类名称")
	private String prodCategory1stName;
	/**
	 * 品类code
	 */
	@ApiModelProperty(value = "品类code")
	private String prodCategory;
	/**
	 * 品类名称
	 */
	@ApiModelProperty(value = "品类名称")
	private String prodCategoryName;
	/**
	 * 中类code
	 */
	@ApiModelProperty(value = "中类code")
	private String prodCategory2nd;
	/**
	 * 中类名称
	 */
	@ApiModelProperty(value = "中类名称")
	private String prodCategory2ndName;
	/**
	 * 小类code
	 */
	@ApiModelProperty(value = "小类code")
	private String prodCategory3rd;
	/**
	 * 小类名称
	 */
	@ApiModelProperty(value = "小类名称")
	private String prodCategory3rdName;
	/**
	 * 品牌
	 */
	@ApiModelProperty(value = "品牌")
	private String brand;
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	/**
	 * 年份
	 */
	@ApiModelProperty(value = "年份")
	private String year;
	/**
	 * 季节
	 */
	@ApiModelProperty(value = "季节")
	private String season;
	@ApiModelProperty(value = "季节名称")
	private String seasonName;
	/**
	 * 月份
	 */
	@ApiModelProperty(value = "月份")
	private String month;
	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private String sex;
	@ApiModelProperty(value = "性别")
	private String sexName;
	/**
	 * 生产模式
	 */
	@ApiModelProperty(value = "生产模式")
	private String devtType;
	@ApiModelProperty(value = "生产模式")
	private String devtTypeName;
	/**
	 * 主材料
	 */
	@ApiModelProperty(value = "主材料")
	private String mainMaterials;
	/**
	 * 研发材料
	 */
	@ApiModelProperty(value = "研发材料")
	private String rdMat;
	/**
	 * 设计师名称
	 */
	@ApiModelProperty(value = "设计师名称")
	private String designer;
	/** 设计师id */
	@ApiModelProperty(value = "设计师id")
	private String designerId;

}

