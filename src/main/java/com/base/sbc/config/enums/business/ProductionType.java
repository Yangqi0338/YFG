package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * {@code 描述：生产类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/4/11
 */
@Getter
@AllArgsConstructor
public enum ProductionType {
    CMT("CMT","CMT"),
    FOB("FOB","FOB"),
    WPO("WPO","外购"),
    SALE("999","代销"),
    SCK("SCK","市场"),
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

    ProductionType(String text) {
        this.code = this.name();
        this.text = text;
    }

    public static ProductionType findByCode(String code){
        return Arrays.stream(ProductionType.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

    public static boolean equals(String code, ProductionType productionType){
        return findByCode(code) == productionType;
    }

}
