package com.base.sbc.module.fabric.enums;

public enum DevApplyAllocationStatusEnum {
    TO_BE_ALLOCATED("1", "待分配"),
    IN_PROGRESS("2", "进行中"),
    COMPLETED("3", "已完成"),
    ;
    private final String K;
    private final String V;

    DevApplyAllocationStatusEnum(String k, String v) {
        K = k;
        V = v;
    }

    public String getK() {
        return K;
    }

    public String getV() {
        return V;
    }
}
