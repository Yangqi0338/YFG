package com.base.sbc.config.enums.business.style;

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
public enum BomStatus {
    SAMPLE("样品", PackType.PACK_DESIGN),

    BULK("大货", PackType.PACK_BIG_GOODS),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    private final PackType packType;

    @JsonValue
    public String getCode() {
        return code;
    }

    BomStatus(String text, PackType packType) {
        this.code = ordinal() + "";
        this.text = text;
        this.packType = packType;
    }

}
