package com.base.sbc.module.basicsdatum.enums;

public enum BasicsdatumProcessNodeEnum {
    NO_NEXT_DRAFT("NO_NEXT_DRAFT", "未下稿"),
    REVIEWED_DRAFT("REVIEWED_DRAFT", "审稿中"),
    NEXT_DRAFT("NEXT_DRAFT", "已下稿"),
    PUNCHING_COMPLETED("PUNCHING_COMPLETED", "打版完成"),
    SAMPLE_CLOTHING_COMPLETED("SAMPLE_CLOTHING_COMPLETED", "样衣完成"),
    SAMPLE_SELECTION("SAMPLE_SELECTION", "看样选款"),
    ORDER_BOOK_PRODUCTION("ORDER_BOOK_PRODUCTION", "订货本制作"),
    BOSS_STYLE("BOSS_STYLE", "老板过款式"),
    RISK_ASSESSMENT("RISK_ASSESSMENT", "风险评估"),
    BUSINESS_ENTERPRISES("BUSINESS_ENTERPRISES", "设计部下面料详单"),
    SEND_MAIN_FABRIC("SEND_MAIN_FABRIC", "设计部下明细单"),
    DESIGN_DETAIL_DATE("DESIGN_DETAIL_DATE", "设计部下正确样"),
    DESIGN_CORRECT_DATE("DESIGN_CORRECT_DATE", "设计部下明细单"),
    VERSION_ROOM_CHECK_VERSION("VERSION_ROOM_CHECK_VERSION", "版房查版"),
    PURCHASE_REQUEST("PURCHASE_REQUEST", "面辅料采购申请"),
    REPLY_DELIVERY_TIME("REPLY_DELIVERY_TIME", "采购回复交期"),
    SURFACE_AUXILIARY_MATERIAL_TESTING("SURFACE_AUXILIARY_MATERIAL_TESTING", "面辅料检测"),
    TECHNICAL_TABLE("TECHNICAL_TABLE", "产前技术表"),
    PRE_PRODUCTION_SAMPLE_PRODUCTION("PRE_PRODUCTION_SAMPLE_PRODUCTION", "产前样制作"),
    PRICING("PRICING", "核价"),
    ORDER_PLACEMENT_BY_ACCOUNTING_CONTROL("ORDER_PLACEMENT_BY_ACCOUNTING_CONTROL", "计控下单"),
    ;

    private final String key;
    private final String value;

    BasicsdatumProcessNodeEnum(String k, String v) {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
