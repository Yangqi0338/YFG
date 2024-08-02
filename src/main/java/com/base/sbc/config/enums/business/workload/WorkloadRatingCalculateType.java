package com.base.sbc.config.enums.business.workload;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 * {@code 描述：订货本渠道类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/3/7
 */
@Getter
@AllArgsConstructor
public enum WorkloadRatingCalculateType {
    /**/
    BASE("基础分"),
    RATE("浮动比率", "a * (b / 100)", BigDecimal.ONE),
    APPEND("附加分"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    /** 计算公式 */
    private final String formula;

    /** 计算公式 */
    private final BigDecimal defaultValue;

    WorkloadRatingCalculateType(String text) {
        this(text, "a", BigDecimal.ZERO);
    }

    WorkloadRatingCalculateType(String text, String formula, BigDecimal defaultValue) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
//        this.code = this.ordinal()+"";
        this.text = text;
        this.formula = formula;
        this.defaultValue = defaultValue;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public Pair<BigDecimal, BigDecimal> calculate(BigDecimal... args) {
        Expression expression = AviatorEvaluator.compile(this.formula);
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 1, argsLength = args.length; i < argsLength; i++) {
            map.put(String.valueOf((char) (i + 96)), args[i]);
        }
        Object execute = expression.execute(map);
        BigDecimal source = args[0];
        BigDecimal increment = new BigDecimal(execute.toString()).setScale(1, RoundingMode.HALF_UP);
        return Pair.of(source.add(increment), increment);
    }

}
