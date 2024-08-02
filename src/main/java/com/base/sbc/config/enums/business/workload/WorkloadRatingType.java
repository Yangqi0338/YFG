package com.base.sbc.config.enums.business.workload;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@code 描述：订货本渠道类型}
 *
 * @author KC
 * @CopyRight @ 广州尚捷科技有限公司
 * @since 2024/3/7
 */
@Getter
@AllArgsConstructor
public enum WorkloadRatingType {
    /**/
    SAMPLE("样衣工"),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;

    WorkloadRatingType(String text) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
//        this.code = this.ordinal()+"";
        this.text = text;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

}
