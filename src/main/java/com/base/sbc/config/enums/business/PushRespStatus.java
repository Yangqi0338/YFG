package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code 描述：推送请求状态}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/4/11
 */
@Getter
@AllArgsConstructor
public enum PushRespStatus {
    /** 推送请求状态 */
    PROCESS("处理中"),
    FAILURE("失败"),
    SUCCESS("成功"),
    ;

    /** 编码 */
    private final String code;

    @EnumValue
    /** 文本 */
    private final String text;


    @JsonValue
    public String getText() {
        return text;
    }

    PushRespStatus(String text) {
        String code = this.name().toLowerCase();
        if (code.contains("_")) code = StrUtil.toCamelCase(code);
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
    }
}
