/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 面料开发基本信息保存
 */
@Data
@ApiModel("面料开发基本信息保存")
public class FabricDevBasicInfoSaveDTO {
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;

    /**
     * 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；
     */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；")
    private String fabricLabel;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 标准生产周期（天）
     */
    @ApiModelProperty(value = "标准生产周期（天）")
    private Integer prodCycle;
    /**
     * 简码
     */
    @ApiModelProperty(value = "简码")
    private String shortCode;
    /**
     * 询价
     */
    @ApiModelProperty(value = "询价")
    private BigDecimal inquiry;
    /**
     * 是否转至物料档案 0.否、1.是
     */
    @ApiModelProperty(value = "是否转至物料档案 0.否、1.是")
    private String toMaterialFlag;
    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String category;
    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String categoryCode;
    /**
     * 是否物料档案接受 0.否、1.是
     */
    @ApiModelProperty(value = "是否物料档案接受 0.否、1.是")
    private String materialAcceptFlag;
    /**
     * 来源1.新增、2.其他
     */
    @ApiModelProperty(value = "来源1.新增、2.其他")
    private String source;
    /**
     * 要求到料日期
     */
    @ApiModelProperty(value = "要求到料日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date requiredArrivalDate;
    /**
     * 起订量（米）
     */
    @ApiModelProperty(value = "起订量（米）")
    private Integer moq;

    /** 大类编码 */
    @ApiModelProperty(value = "大类编码")
    private String category1Code;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称")
    private String category1Name;
    /** 中类编码 */
    @ApiModelProperty(value = "中类编码")
    private String category2Code;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称")
    private String category2Name;
    /** 小类编码 */
    @ApiModelProperty(value = "小类编码")
    private String category3Code;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称")
    private String category3Name;
    /** 物料类别第4级编码 */
    @ApiModelProperty(value = "物料类别第4级编码")
    private String categoryId;
    /** 类别名称第4级名称 */
    @ApiModelProperty(value = "类别名称第4级名称")
    private String categoryName;
}
