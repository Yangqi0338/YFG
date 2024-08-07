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
public enum ProcessDatabaseType {
    /**/
    bjk("部件库", "C8_SpecCategory"),
    jcgy("基础工艺", "C8_SewingType"),
    wfgy("外辅工艺"),
    cjgy("裁剪工艺"),
    zysx("注意事项"),
    ztbz("整烫包装", "C8_SewingType"),
    mbbj("模板部件", "C8_SpecCategory,C8_Brand"),

    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String dict;
    private final String text;


    ProcessDatabaseType(String text) {
        this(text, "");
    }

    ProcessDatabaseType(String text, String dict) {
        this.code = (this.ordinal() + 1) + "";
        this.text = text;
        this.dict = dict;
    }

    public static ProcessDatabaseType findByCode(String code) {
        return Arrays.stream(ProcessDatabaseType.values()).filter(it -> it.code.equals(code)).findFirst().orElse(null);
    }

    public static ProcessDatabaseType findByText(String text) {
        return Arrays.stream(ProcessDatabaseType.values()).filter(it -> it.text.equals(text)).findFirst().orElse(null);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

}
