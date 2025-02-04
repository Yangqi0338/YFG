package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.base.sbc.module.hangtag.enums.HangTagDeliverySCMStatusEnum;
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
public enum SystemSource {
    SYSTEM("内部"),
    PDM("PDM系统"),
    INTERNAL_LINE("无实际意义,仅内外部系统分割"),
    BCS("BCS系统"),
    ESCM("ESCM系统"),
    PRINT("打印系统"),

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

    SystemSource(String text) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
    }

    public boolean greatThan(SystemSource sourceEnum){
        return this.code.compareTo(sourceEnum.code) > 0;
    }

}
