package com.base.sbc.module.fabric.enums;

public enum FabricLibraryApproveStatusEnum {
    NOT_SUBMIT("0", "未提交"),
    TO_BE_REVIEWED("1", "待审核"),
    UNDER_REVIEW("2", "审核中"),
    APPROVED("3", "审核通过"),
    AUDIT_FAILED("4", "审核失败"),
    ;
    private final String K;
    private final String V;

    FabricLibraryApproveStatusEnum(String k, String v) {
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
