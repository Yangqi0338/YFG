/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：样衣 实体类
 * @address com.base.sbc.module.sample.entity.Sample
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 16:31:39
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample")
@ApiModel("样衣 Sample")
public class Sample extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 波段企划id */
    @ApiModelProperty(value = "波段企划id"  )
    private String planningBandId;
    /** 品类信息id */
    @ApiModelProperty(value = "品类信息id"  )
    private String planningCategoryId;
    /** 坑位信息id */
    @ApiModelProperty(value = "坑位信息id"  )
    private String planningCategoryItemId;
    /** 品类名称路径:(大类/品类/中类/小类) */
    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)"  )
    private String categoryName;
    /** 品类id路径:(大类/品类/中类/小类) */
    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)"  )
    private String categoryIds;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 旧设计款号 */
    @ApiModelProperty(value = "旧设计款号"  )
    private String hisDesignNo;
    /** 款式名称 */
    @ApiModelProperty(value = "款式名称"  )
    private String styleName;
    /** 款式类型 */
    @ApiModelProperty(value = "款式类型"  )
    private String styleType;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 月份 */
    @ApiModelProperty(value = "月份"  )
    private String month;
    /** 性别 */
    @ApiModelProperty(value = "性别"  )
    private String sex;
    /** 波段(编码) */
    @ApiModelProperty(value = "波段(编码)"  )
    private String bandCode;
    /** 生产模式 */
    @ApiModelProperty(value = "生产模式"  )
    private String devtType;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 主题 */
    @ApiModelProperty(value = "主题"  )
    private String subject;
    /** 版型 */
    @ApiModelProperty(value = "版型"  )
    private String plateType;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String sizeRange;
    /** 开发分类 */
    @ApiModelProperty(value = "开发分类"  )
    private String devClass;
    /** Default尺码 */
    @ApiModelProperty(value = "Default尺码"  )
    private String defaultSize;
    /** Default颜色 */
    @ApiModelProperty(value = "Default颜色"  )
    private String defaultColor;
    /** 目标成本 */
    @ApiModelProperty(value = "目标成本"  )
    private BigDecimal productCost;
    /** 主材料 */
    @ApiModelProperty(value = "主材料"  )
    private String mainMaterials;
    /** 研发材料 */
    @ApiModelProperty(value = "研发材料"  )
    private String rdMat;
    /** 廓形 */
    @ApiModelProperty(value = "廓形"  )
    private String silhouette;
    /** 打板难度 */
    @ApiModelProperty(value = "打板难度"  )
    private String patDiff;
    /** 尺码 */
    @ApiModelProperty(value = "尺码"  )
    private String productSizes;
    /** 设计师名称 */
    @ApiModelProperty(value = "设计师名称"  )
    private String designer;
    /** 设计师id */
    @ApiModelProperty(value = "设计师id"  )
    private String designerId;
    /** 状态:0未开款，1已开款，2已下发打板 */
    @ApiModelProperty(value = "状态:0未开款，1已开款，2已下发打板"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

