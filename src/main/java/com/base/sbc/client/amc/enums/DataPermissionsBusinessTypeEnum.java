package com.base.sbc.client.amc.enums;

/**
 * @Author xhj
 * @Date 2023/7/13 9:07
 */
public enum DataPermissionsBusinessTypeEnum {

    //企划管理
    PlanningSeason("PlanningSeason", "产品季"),
    PlanningChannel("PlanningChannel", "产品季"),
    PlanningCategoryItem("PlanningCategoryItem", "坑位数据"),
    ChartBar("ChartBar", "波段汇总统计"),
    ProductBar("ProductBar", "品类汇总统计"),
    //款式设计
    design_task("design_task", "款式设计任务"),
    style_info("style_info", "设计档案"),
    StyleBoard("StyleBoard", "款式看板"),
    technologyCenter("technologyCenter", "技术中心看板"),
    FabricInformation("FabricInformation", "调样管理"),
    //款式配色
    styleColor("styleColor", "款式配色"),
    packDesign("packDesign", "设计BOM"),
    packBigGoods("packBigGoods", "标准资料包"),
    sample_capacity_total_count("sample_capacity_total_count", "样衣产能总数"),
    capacity_contrast_statistics("capacity_contrast_statistics", "样衣产能对比"),
    style_group("style_group", "款式搭配"),
    style_order_book("style_order_book", "订货本"),
    fabric_summary("fabric_summary", "面料汇总"),
    style_pricing("style_pricing", "款式定价"),
    sampleBoard("sampleBoard", "样衣看板"),
    material("material", "物料清单"),
    //打版管理
    patternMakingSteps("patternMakingSteps", "打版进度"),
    hangTagList("hangTagList", "吊牌列表"),
    pre_production_sample_task("pre_production_sample_task", "产前样样衣任务"),
    pre_production_sample_board("pre_production_sample_board", "产前样看板"),
    PatternMakingWeekMonthView("PatternMakingWeekMonthView", "版类对比"),
    CategorySummaryCount("CategorySummaryCount", "品类汇总统计"),

    //报表中心
    hangTagReport("hangTagReport", "电商充绒量报表"),
    designOrderScheduleDetailsReport("designOrderScheduleDetailsReport", "设计下单进度明细报表"),
    styleSizeReport("styleSizeReport", "尺寸查询报表"),
    stylePackBomMaterialReport("stylePackBomMaterialReport", "BOM清单查询报表"),

    work_log("work_log","工作小账"),
    ;
    private final String k;
    private final String v;

    DataPermissionsBusinessTypeEnum(String k, String v) {
        this.k = k;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }
}
