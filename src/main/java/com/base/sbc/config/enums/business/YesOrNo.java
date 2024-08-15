package com.base.sbc.config.enums.business;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author xhj
 * @Date 2023/6/16 17:26
 */
@Getter
public enum YesOrNo {
    /**/
    NO("否"),

    YES("是"),
    ;


    @EnumValue
    private final String code;
    private final String text;

    YesOrNo(String text) {
        this.code = this.ordinal() + "";
        this.text = text;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public YesOrNo reverse() {
        return this == YesOrNo.YES ? YesOrNo.NO : (this == YesOrNo.NO ? YesOrNo.YES : null);
    }

    public static YesOrNo findByValue(String value) {
        return Arrays.stream(YesOrNo.values()).filter(it -> it.getCode().equals(value)).findFirst().orElse(null);
    }

    public static YesOrNo findByValue(boolean value) {
        return value ? YesOrNo.YES : YesOrNo.NO;
    }

}
