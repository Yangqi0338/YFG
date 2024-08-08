package com.base.sbc.config.enums;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public enum UnitConverterEnum {
    /**/
    KILOMETER("km(千米)", "a / 1000"),
    METER("米"),
    PIECE("件"),
    HECTOMETER("百米", "a / 100"),
    PERCENT("百分比", "a / 100"),
    SOURCE("原值"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    /** 可以通过的后缀 */
    private final String formula;

    UnitConverterEnum(String text) {
        this(text, "a");
    }

    UnitConverterEnum(String text, String formula) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.upperFirst(StrUtil.toCamelCase(code));
        this.text = text;
        this.formula = formula;
    }

    @JsonValue
    public String getText() {
        return text;
    }

    public BigDecimal calculate(BigDecimal... args) {
        Expression expression = AviatorEvaluator.compile(this.formula);
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            map.put(String.valueOf((char) (i + 97)), args[i]);
        }
        Object execute = expression.execute(map);
        return new BigDecimal(execute.toString()).setScale(1, RoundingMode.HALF_UP);
    }

}