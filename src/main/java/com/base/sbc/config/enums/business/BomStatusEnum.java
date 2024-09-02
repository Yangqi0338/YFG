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
public enum BomStatusEnum {
    /**/
    DESIGN("样品"),

    STYLE("大货"),
    ;


    @EnumValue
    private final String code;
    private final String text;

    BomStatusEnum(String text) {
        this.code = this.ordinal() + "";
        this.text = text;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public BomStatusEnum reverse() {
        return this == BomStatusEnum.STYLE ? BomStatusEnum.DESIGN : (this == BomStatusEnum.DESIGN ? BomStatusEnum.STYLE : null);
    }

    public static BomStatusEnum findByCode(String code) {
        return Arrays.stream(BomStatusEnum.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
    }

}
