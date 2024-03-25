package com.base.sbc.module.patternlibrary.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum PatternLibraryStatusEnum {

    NO_PADDED(1, "待补齐"),
    NO_SUBMIT(2, "待提交"),
    NO_REVIEWED(3, "待审核"),
    REVIEWED(4, "已审核"),
    REJECTED(5, "已驳回");

    private Integer code;
    private String value;

    PatternLibraryStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

}
