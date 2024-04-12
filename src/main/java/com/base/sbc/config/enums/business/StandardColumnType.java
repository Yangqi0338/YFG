package com.base.sbc.config.enums.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
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
public enum StandardColumnType {
    TAG("吊牌","DP"),
    TAG_ROOT("吊牌字段", "DP", Arrays.asList(TAG)),
    WASHING("温馨提示", "XM"),
    WASHING_ROOT("洗唛", "XM", Arrays.asList(WASHING)),
    ;
    /** 编码 */
    @EnumValue
    private final String code;
    /** 文本 */
    private final String text;
    private final String preCode;
    private final List<StandardColumnType> childrenTypeList;

    @JsonValue
    public String getCode() {
        return code;
    }

    StandardColumnType(String text, String preCode) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
        this.preCode = preCode;
        this.childrenTypeList = new ArrayList<>();
    }

    StandardColumnType(String text, String preCode, List<StandardColumnType> childrenTypeList) {
        String code = this.name().toLowerCase();
        this.code = StrUtil.toCamelCase(code);
        this.text = text;
        this.preCode = preCode;
        this.childrenTypeList = childrenTypeList;
    }

    public static List<StandardColumnType> findRootList(){
        return Arrays.stream(StandardColumnType.values()).filter(it-> it.code.contains("_root")).collect(Collectors.toList());
    }

    public StandardColumnType findParent(){
        return Arrays.stream(StandardColumnType.values()).filter(it-> it.childrenTypeList.contains(this)).findFirst().orElse(null);
    }

    public static StandardColumnType findByCode(String code) {
        return Arrays.stream(StandardColumnType.values()).filter(it-> it.code.equals(code)).findFirst().orElse(null);
    }


}
