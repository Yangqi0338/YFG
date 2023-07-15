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
 * 类描述：核价工艺费用 实体类
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:22
 */
@Data
@ApiModel("核价工艺费用")
public class PricingCraftCostsDTO {
    private static final long serialVersionUID = -8339989056778611115L;

    private String id;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id")
    private String craftId;
    /**
     * 工艺名称
     */
    @ApiModelProperty(value = "工艺名称")
    private String craftName;
    /**
     * 是否委外0.否, 1.是
     */
    @ApiModelProperty(value = "是否委外0.否, 1.是")
    private String outsource;
    /**
     * 加工商id
     */
    @ApiModelProperty(value = "加工商id")
    private String processorId;
    /**
     * 加工商名称
     */
    @ApiModelProperty(value = "加工商名称")
    private String processorName;
    /**
     * 币种编码
     */
    @ApiModelProperty(value = "币种编码")
    private String currencyCode;
    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;

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
    /**
     * 汇率
     */
    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal num;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal sumPrice;
}
