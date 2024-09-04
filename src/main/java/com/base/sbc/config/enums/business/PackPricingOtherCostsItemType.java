package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
public enum PackPricingOtherCostsItemType {
    OTHER("其它费", "costOtherPrice"),
    OUTSOURCE_PROCESS("外协加工费","{\"CMT\":\"outSource\",\"FOB\":\"outSource1\"}"),
    ;
    /** 编码 */
    private final String code;
    /** 文本 */
    @EnumValue
    private final String text;

    /** 名称字典 */
    private final String dict;

    @JsonValue
    public String getText() {
        return text;
    }

    PackPricingOtherCostsItemType(String text, String dict) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
        this.dict = dict;
    }

    public String getDict(String key) {
        if (StrUtil.isBlank(dict) || !JSONUtil.isTypeJSON(dict)) return dict;
        return JSONUtil.getByPath(JSONUtil.parse(dict),key,"");
    }

    public static PackPricingOtherCostsItemType findByCode(String code){
        return Arrays.stream(PackPricingOtherCostsItemType.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return getText();
    }
}
