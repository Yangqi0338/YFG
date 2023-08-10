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
 * 面料开发供应商信息保存
 */
@Data
@ApiModel("面料开发供应商信息保存")
public class FabricDevSupplerInfoSaveDTO {

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String bizId;
    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;
    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String width;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String colorName;
    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String widthName;
    /**
     * 采购报价
     */
    @ApiModelProperty(value = "采购报价")
    private BigDecimal quotationPrice;
    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;
    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    private String currencyName;
    /**
     * 订货周期
     */
    @ApiModelProperty(value = "订货周期")
    private BigDecimal orderDay;
    /**
     * 生产周期
     */
    @ApiModelProperty(value = "生产周期")
    private BigDecimal productionDay;
    /**
     * 起订量
     */
    @ApiModelProperty(value = "起订量")
    private Integer minimumOrderQuantity;
    /**
     * 每色起订量
     */
    @ApiModelProperty(value = "每色起订量")
    private Integer minimumOrderQuantityColor;
    /**
     * 供应商料号
     */
    @ApiModelProperty(value = "供应商料号")
    private String supplierMaterialCode;
    /**
     * 是否默认(0不默认，1默认)
     */
    @ApiModelProperty(value = "是否默认(0不默认，1默认)")
    private String selectFlag;
    /**
     * fob
     */
    @ApiModelProperty(value = "fob")
    private String fob;
}
