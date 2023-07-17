package com.base.sbc.module.pricing.enums;

import java.util.Arrays;

public enum PricingCountTypeEnum {
    MATERIAL_COSTS("material_costs", "物料费用"),
    PROCESS_COSTS("process_costs", "加工费用"),
    CRAFT_COSTS("craft_costs", "二次加工费用"),
    OTHER_COSTS("other_costs", "其他费用"),
    ;
    private final String k;
    private final String v;

    PricingCountTypeEnum(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }

    public static PricingCountTypeEnum getByK(String k) {
        return Arrays.stream(PricingCountTypeEnum.values())
                .filter(x -> x.getK().equals(k))
                .findFirst()
                .orElse(null);
    }
}
