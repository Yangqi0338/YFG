package com.base.sbc.config.enums.business.smp;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code 描述：全量标准表类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2023/11/30
 */
@Getter
@AllArgsConstructor
public enum SaleFacResultType {
    /**/
    ONLINE_SALE("线上销售"),
    OFFLINE_SALE("线下销售"),
    FIRST_PRODUCTION("first", "线上/线下首单投产"),
    APPEND_PRODUCTION("append", "线上/线下追单投产"),
    ;

    private final String code;
    @EnumValue
    private final String text;

    SaleFacResultType(String text) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
    }

    public static SaleFacResultType startByCode(String code) {
        return Arrays.stream(SaleFacResultType.values()).filter(it -> code.startsWith(it.code)).findFirst().orElse(null);
    }

    public static List<SaleFacResultType> productionList() {
        return Arrays.stream(SaleFacResultType.values()).filter(it -> it.name().contains("PRODUCTION")).collect(Collectors.toList());
    }

    public static List<SaleFacResultType> saleList() {
        return Arrays.stream(SaleFacResultType.values()).filter(it -> it.name().contains("SALE")).collect(Collectors.toList());
    }

    @JsonValue
    public String getText() {
        return text;
    }

}
