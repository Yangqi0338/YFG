package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum RFIDType {
    ALL("洗唛&吊牌RFID"),
    WASHING("洗唛RFID"),
    HANG_TAG("吊牌RFID"),

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

    RFIDType(String text) {
        String code = this.name().toLowerCase();
        if (code.contains("_")) code = StrUtil.toCamelCase(code);
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
    }

}
