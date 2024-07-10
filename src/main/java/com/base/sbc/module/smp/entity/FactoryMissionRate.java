/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.smp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 类描述：BI 投产单
 *
 * @author KC
 * @version 1.0
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@TableName(value = "DW_FACTORY_MISSION_RATE")
@ApiModel("BI 投产单 FactoryMissionRate")
public class FactoryMissionRate implements Serializable {

    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** id */
    @ApiModelProperty(value = "id")
    @TableId(value = "GUID", type = IdType.ASSIGN_UUID)
    private String id;

    /** 编号 */
    @ApiModelProperty(value = "编号")
    @TableField("GOODS_NO")
    private String code;

    /** 供应商id */
    @ApiModelProperty(value = "供应商id")
    @TableField("SUPPLIER_ID")
    private String supplierId;

    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称")
    @TableField("SUPPLIER_NAME")
    private String supplierName;

    /** 大货款 */
    @ApiModelProperty(value = "大货款")
    @TableField("PROD_CODE")
    private String bulkStyleNo;

    /** 投产单号 */
    @ApiModelProperty(value = "投产单号")
    @TableField("FAC_PRD_ORDER_NO")
    private String orderNo;

    /** sku编码 */
    @ApiModelProperty(value = "sku编码")
    @TableField("SKU_CODE")
    private String skuCode;

    /** 紧急程度 */
    @ApiModelProperty(value = "紧急程度")
    @TableField("EMERGENCY_DEGREE")
    private String urgency;

    /** 投产类型 */
    @ApiModelProperty(value = "投产类型")
    @TableField("XDLX")
    private String productionType;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    @TableField("STATUS")
    private String status;

    /** 下单时间 */
    @ApiModelProperty(value = "下单时间")
    @TableField("PLACEORDER_AT")
    private LocalDate orderDate;

    /** 仓库交付时间 */
    @ApiModelProperty(value = "仓库交付时间")
    @TableField("DELIVERY_AT")
    private LocalDate deliveryDate;

    /** 单价 */
    @ApiModelProperty(value = "单价")
    @TableField("UNIT_PRICE")
    private BigDecimal unitPrice;

    /** 投产件数 */
    @ApiModelProperty(value = "投产件数")
    @TableField("GD_NUM")
    private BigDecimal orderNum;

    /** 仓库交付件数 */
    @ApiModelProperty(value = "仓库交付件数")
    @TableField("JRK_NUM")
    private BigDecimal deliveryNum;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
