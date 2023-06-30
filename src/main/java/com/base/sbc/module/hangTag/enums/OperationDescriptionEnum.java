package com.base.sbc.module.hangTag.enums;

import java.util.Arrays;

/**
 * @Author xhj
 * @Date 2023/6/27 10:21
 */
public enum OperationDescriptionEnum {
    SAVE("1", "保存信息"),
    SUBMIT("2", "提交审核"),
    TECHNICIANS_CONFIRMED("3", "工艺员确认"),
    TECHNOLOGIST_CONFIRMED("4", "技术员确认"),
    QUALITY_CONTROL_CONFIRMED("5", "品控确认"),
    ;
    private final String k;
    private final String v;

    OperationDescriptionEnum(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }

    public static String getV(String k) {
        return Arrays.stream(OperationDescriptionEnum.values())
                .filter(x -> x.getK().equals(k))
                .map(OperationDescriptionEnum::getV)
                .findFirst().orElse("未定义记录");

    }
}
