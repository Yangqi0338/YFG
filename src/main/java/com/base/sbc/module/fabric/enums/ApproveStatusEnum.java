package com.base.sbc.module.fabric.enums;

public enum ApproveStatusEnum {
    NOT_SUBMIT("1", "未提交"),
    UNDER_REVIEW("2", "审核中"),
    APPROVED("3", "审核通过"),
    AUDIT_FAILED("4", "审核失败"),
    ;
    private final String K;
    private final String V;

    ApproveStatusEnum(String k, String v) {
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
