package com.base.sbc.module.orderbook.vo;

import lombok.Data;

@Data
public class OrderBookDetailForSeasonPlanningVO {
    // 订货本详情id
    private String orderBookDetailId;
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
    // 款式类型code
    private String styleCategoryCode;
    // 款式类型
    private String styleCategoryName;
}
