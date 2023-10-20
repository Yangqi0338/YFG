package com.base.sbc.module.hangtag.enums;

/**
 * @Author xhj
 * @Date 2023/6/27 10:21
 */
public enum HangTagStatusEnum {
    UNWRITTEN("0", "未填写"),
    NOT_SUBMIT("1", "未提交"),
    TO_TECHNICIANS_CONFIRMED("2", "待工艺员确认"),
    TO_TECHNOLOGIST_CONFIRMED("3", "待技术员确认"),
    TO_QUALITY_CONTROL_CONFIRMED("4", "待品控确认"),
    CONFIRMED("5", "已确认"),
    AUDIT_FAILED("6", "审核失败"),
    ;
    private final String k;
    private final String v;

    HangTagStatusEnum(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }
}
