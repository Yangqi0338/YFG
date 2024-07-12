package com.base.sbc.config.enums.business.smp;

import cn.hutool.core.util.StrUtil;
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
public enum SluggishSaleLevelEnum {
    /**/
    S,
    A,
    B,
    C,
    ;

    private final String code;
    /** 文本 */
    private final String text;

    SluggishSaleLevelEnum() {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = code;
    }

    public static SluggishSaleLevelEnum startByCode(String code) {
        if (StrUtil.isBlank(code)) return null;
        return Arrays.stream(SluggishSaleLevelEnum.values()).filter(it -> code.startsWith(it.code)).findFirst().orElse(null);
    }

    @JsonValue
    public String getText() {
        return text;
    }

}
