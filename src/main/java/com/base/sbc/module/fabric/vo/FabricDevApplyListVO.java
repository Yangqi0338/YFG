/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 面料开发申请保存
 */
@Data
@ApiModel("面料开发申请")
public class FabricDevApplyListVO {
    private String id;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;
    @ApiModelProperty(value = "修改人")
    private String updateName;
    @ApiModelProperty(value = "创建人")
    private String createName;
    /**
     * 开发申请单号
     */
    @ApiModelProperty(value = "开发申请单号")
    private String devApplyCode;
    /**
     * 分配状态:1.待分配、2.进行中、3.已完成
     */
    @ApiModelProperty(value = "分配状态:1.待分配、2.进行中、3.已完成")
    private String allocationStatus;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requiredArrivalDate;
    /**
     * 起订量（米）
     */
    @ApiModelProperty(value = "起订量（米）")
    private Integer moq;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;


    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String category1Name;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String category2Name;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    private String category3Name;
    /**
     * 类别名称第4级名称
     */
    @ApiModelProperty(value = "类别名称第4级名称")
    private String categoryName;
}
