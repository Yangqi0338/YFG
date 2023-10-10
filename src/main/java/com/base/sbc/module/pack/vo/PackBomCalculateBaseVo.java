package com.base.sbc.module.pack.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackBomCalculateBaseVo {
    /**
     * 大货单件用量
     */
    @ApiModelProperty(value = "大货单件用量")
    private BigDecimal bulkUnitUse;
    /**
     * 大货单价
     */
    @ApiModelProperty(value = "大货单价")
    private BigDecimal bulkPrice;

    /**
     * 大货单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 损耗%
     */
    @ApiModelProperty(value = "损耗%")
    private BigDecimal lossRate;

    /**
     * 计控损耗%
     */
    @ApiModelProperty(value = "计控损耗%")
    private BigDecimal  planningLoossRate;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    /**
     * 单价税点
     */
    @ApiModelProperty(value = "单价税点")
    private BigDecimal priceTax;
    private String foreignId;
    private String packType;
}
