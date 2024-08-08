package com.base.sbc.config.enums.business;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author xhj
 * @Date 2023/6/16 17:26
 */
public enum HasOrNoneEnum {
    /**/
    NONE("无"),
    HAS("有"),
    ;

    @EnumValue
    private final String code;
    @Getter
    private final String text;

    HasOrNoneEnum(String text) {
        this.code = this.ordinal() + "";
        this.text = text;
    }

    public static HasOrNoneEnum findByCode(String code) {
        return Arrays.stream(HasOrNoneEnum.values()).filter(it -> it.getValue().equals(code)).findFirst().orElse(null);
    }

    public static HasOrNoneEnum findByCode(boolean code) {
        return code ? HasOrNoneEnum.HAS : HasOrNoneEnum.NONE;
    }

    @JsonValue
    public String getValue() {
        return code;
    }

    public HasOrNoneEnum reverse() {
        return this == HasOrNoneEnum.HAS ? HasOrNoneEnum.NONE : (this == HasOrNoneEnum.NONE ? HasOrNoneEnum.HAS : null);
    }
}
