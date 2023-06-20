package com.base.sbc.module.pricing.enums;

/**
 * @Author xhj
 * @Date 2023/6/17 16:00
 */
public enum PricingQueryDimensionEnum {
    ITEM("item", "明细维度"),
    SUMMARY("summary", "汇总维度"),
    ;

    private final String k;
    private final String v;

    PricingQueryDimensionEnum(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }
}
