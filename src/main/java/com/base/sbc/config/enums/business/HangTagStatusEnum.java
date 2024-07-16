package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
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
public enum HangTagStatusEnum {
    NOT_INPUT("未填写"),
    NOT_COMMIT("未提交"),
    DESIGN_CHECK("待工艺员确认"),
    TECH_CHECK("待技术员确认"),
    QC_CHECK("待品控确认"),
    TRANSLATE_CHECK("待翻译确认"),
    SUSPEND("不通过"),
    FINISH("已确认"),
    PART_TRANSLATE_CHECK("部分翻译确认"),
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

    HangTagStatusEnum(String text) {
        this.code = this.ordinal()+"";
        this.text = text;

    }

    public static String getTextByCode(String code) {
        if (StrUtil.isNotEmpty(code)) {
            for (HangTagStatusEnum e : HangTagStatusEnum.values()) {
                if (e.getCode() == code) {
                    return e.getText();
                }
            }
        }
        return null;
    }

    public static HangTagStatusEnum findByCode(String code){
        return Arrays.stream(HangTagStatusEnum.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }

    public boolean lessThan(HangTagStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) < 0;
    }

    public boolean greatThan(HangTagStatusEnum statusEnum){
        return this.code.compareTo(statusEnum.code) > 0;
    }

}
