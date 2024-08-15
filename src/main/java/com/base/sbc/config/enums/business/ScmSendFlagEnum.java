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
public enum ScmSendFlagEnum {
    /**/
    NOT_SEND("未发送"),

    SUCCESS("发送成功"),
    FAILURE("发送失败"),
    REOPEN("重新打开"),
    ;


    @EnumValue
    private final String code;
    private final String text;

    ScmSendFlagEnum(String text) {
        this.code = this.ordinal() + "";
        this.text = text;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public static ScmSendFlagEnum findByCode(String code) {
        return Arrays.stream(ScmSendFlagEnum.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
    }

}
