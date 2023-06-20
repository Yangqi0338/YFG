package com.base.sbc.module.pricing.enums;

/**
 * @Author xhj
 * @Date 2023/6/17 9:36
 */
public enum PricingSourceTypeEnum {
    PLATE_MAKING("plateMaking", "制版核价"),
    PRODUCT("product", "商品核价"),
    OTHER("other", "其他核价"),
    ;

    private final String k;
    private final String v;

    PricingSourceTypeEnum(String k, String v) {
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
