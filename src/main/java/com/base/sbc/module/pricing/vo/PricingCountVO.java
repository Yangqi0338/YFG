package com.base.sbc.module.pricing.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PricingCountVO {

    /**
     * 合计价格
     */
    private BigDecimal totalPrice;
    /**
     * 合计用量
     */
    private BigDecimal totalUsage;

}
