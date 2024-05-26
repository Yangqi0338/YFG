package com.base.sbc.module.style.vo;

import lombok.Data;

@Data
public class StyleDimensionVO {
    // 设计款号id
    private String styleId;
    // 大类code
    private String prodCategory1st;
    // 大类Name
    private String prodCategory1stName;
    // 品类code
    private String prodCategory;
    // 品类Name
    private String prodCategoryName;
    // 中类
    private String prodCategory2nd;
    // 中类名称
    private String prodCategory2ndName;
    // 波段
    private String bandCode;
    // 波段名称
    private String bandName;
    // 维度id
    private String fieldId;
    // 维度名称
    private String fieldExplain;
    // 类型code
    private String styleCode;
    // 类型名称
    private String styleName;
}
