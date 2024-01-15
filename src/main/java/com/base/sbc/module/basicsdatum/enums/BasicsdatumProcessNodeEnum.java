package com.base.sbc.module.basicsdatum.enums;

import java.util.Objects;

public enum BasicsdatumProcessNodeEnum {
    NO_NEXT_DRAFT("noNextDraft", "未下稿"),
    REVIEWED_DRAFT("reviewedDraft", "审稿中"),
    NEXT_DRAFT("nextDraft", "已下稿"),
    PUNCHING_COMPLETED("punchingCompleted", "打版完成"),
    SAMPLE_CLOTHING_COMPLETED("sampleClothingCompleted", "样衣完成"),
    SAMPLE_SELECTION("sampleSelection", "看样选款"),
    ORDER_BOOK_PRODUCTION("orderBookProduction", "订货本制作"),
    BOSS_STYLE("bossStyle", "老板过款式"),
    RISK_ASSESSMENT("riskAssessment", "风险评估"),
    BUSINESS_ENTERPRISES("businessEnterprises", "商企投产"),
    SEND_MAIN_FABRIC("sendMainFabric", "设计部下面料详单"),
    DESIGN_DETAIL_DATE("designDetailDate", "设计部下明细单"),
    DESIGN_CORRECT_DATE("designCorrectDate", "设计部下正确样"),
    VERSION_ROOM_CHECK_VERSION("versionRoomCheckVersion", "版房查版"),
    FABRIC_REQUISITION("fabricRequisition", "面料请购"),
    PURCHASE_REQUEST("purchaseRequest", "面辅料采购申请"),
    REPLY_DELIVERY_TIME("replyDeliveryTime", "采购回复交期"),

    FABRIC_CYCLE("fabricCycle", "面料周期"),

    LINING_OF_ACCESSORIES("liningOfAccessories", "辅料齐套"),
    SURFACE_AUXILIARY_MATERIAL_TESTING("surfaceAuxiliaryMaterialTesting", "面辅料检测"),
    TECHNICAL_TABLE("technicalTable", "产前技术表"),
    PRE_PRODUCTION_SAMPLE_PRODUCTION("preProductionSampleProduction", "产前样制作"),
    ORDER_PLACEMENT_BY_ACCOUNTING_CONTROL("orderPlacementByAccountingControl", "计控下单/核价"),

    ON_LINE_PRODUCTION_TIME("onLineProductionTime", "上线生产时间"),

    ENTRY_WAREHOUSE_TIME("entryWarehouseTime", "入库时间"),

    TIME_TO_MARKET("timeToMarket", "上市时间");

    private final String code;
    private final String name;

    BasicsdatumProcessNodeEnum(String k, String v) {
        code = k;
        name = v;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BasicsdatumProcessNodeEnum getBycode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (BasicsdatumProcessNodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
