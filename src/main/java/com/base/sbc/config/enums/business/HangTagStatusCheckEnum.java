package com.base.sbc.config.enums.business;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum HangTagStatusCheckEnum {
    DESIGN_CHECK("待工艺员确认"),
    TECH_CHECK("待技术员确认"),
    QC_CHECK("待品控确认"),
    TRANSLATE_CHECK("待翻译确认"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    @JsonValue
    public String getCode() {
        return code;
    }

    HangTagStatusCheckEnum(String text) {
        this.code = this.ordinal()+"";
        this.text = text;

    }

}
