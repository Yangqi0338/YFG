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
 * 类描述：其他费用 实体类
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:28
 */
@Data
@ApiModel("其他费用")
public class PricingOtherCostsDTO {

    private static final long serialVersionUID = -1155327995524575345L;

    /**
     *
     * 费用类型id
     */
    @ApiModelProperty(value = "费用类型Id")
    private String costsTypeId;

    /**
     * 费用类型
     */
    @ApiModelProperty(value = "费用类型")
    private String costsType;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
}
