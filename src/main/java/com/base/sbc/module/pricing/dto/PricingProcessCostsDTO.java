/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：加工费用 实体类
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:31
 */
@Data
@ApiModel("加工费用 ")
public class PricingProcessCostsDTO {

    private static final long serialVersionUID = -5299387355896455301L;
    private String id;
    /**
     * 工序编号
     */
    @ApiModelProperty(value = "工序编号")
    private String processCode;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String processName;
    /**
     * 工时
     */
    @ApiModelProperty(value = "工时")
    private String workingHours;
    /**
     * 标准单价
     */
    @ApiModelProperty(value = "标准单价")
    private BigDecimal standardUnitPrices;
    /**
     * 倍数
     */
    @ApiModelProperty(value = "倍数")
    private BigDecimal multiple;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
    /**
     * 报价货币编码
     */
    @ApiModelProperty(value = "报价货币编码")
    private String quotationPriceCurrencyCode;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;

}
