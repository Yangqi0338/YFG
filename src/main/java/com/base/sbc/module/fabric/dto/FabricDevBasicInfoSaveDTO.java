/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

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
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String bizId;
    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；
     */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；")
    private String fabricLabel;
    /**
     * 面料分类编码
     */
    @ApiModelProperty(value = "面料分类编码")
    private String fabricClassifCode;
    /**
     * 面料分类
     */
    @ApiModelProperty(value = "面料分类")
    private String fabricClassif;
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
     * 物料属性编码
     */
    @ApiModelProperty(value = "物料属性编码")
    private String materialAttributeCode;
    /**
     * 物料属性
     */
    @ApiModelProperty(value = "物料属性")
    private String materialAttribute;
    /**
     * 来源1.新增、2.其他
     */
    @ApiModelProperty(value = "来源1.新增、2.其他")
    private String source;
    /**
     * 起订量（米）
     */
    @ApiModelProperty(value = "起订量（米）")
    private Integer moq;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brand;
    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;
    /**
     * 物料类别编码
     */
    @ApiModelProperty(value = "物料类别编码")
    private String materialCategoryCode;
    /**
     * 物料类别
     */
    @ApiModelProperty(value = "物料类别")
    private String materialCategory;
}
