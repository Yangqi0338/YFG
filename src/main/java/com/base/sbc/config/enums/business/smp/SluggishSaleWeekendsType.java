package com.base.sbc.config.enums.business.smp;

import cn.hutool.core.util.StrUtil;
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
public enum SluggishSaleWeekendsType {
    /**/
    N04W("4周"),
    N08W("8周"),
    N012W("12周"),
    N016W("16周"),

    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    SluggishSaleWeekendsType(String text) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
    }

    public static SluggishSaleWeekendsType findByText(String text) {
        return Arrays.stream(SluggishSaleWeekendsType.values()).filter(it -> it.text.equals(text)).findFirst().orElse(null);
    }

    @JsonValue
    public String getText() {
        return text;
    }

}
