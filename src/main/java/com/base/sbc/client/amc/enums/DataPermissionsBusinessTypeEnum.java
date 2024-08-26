package com.base.sbc.client.amc.enums;

/**
 * @Author xhj
 * @Date 2023/7/13 9:07
 */
public enum DataPermissionsBusinessTypeEnum {

    //企划管理
    PlanningSeason("PlanningSeason", "产品季"),
    PlanningChannel("PlanningChannel", "产品季"),
    planningBoard("planningBoard", "企划看板"),
    PlanningCategoryItem("PlanningCategoryItem", "坑位数据"),
    requirementsManagement("requirementsManagement", "企划需求"),
    seasonPlanning("seasonPlanning", "季节企划"),
    categoryPlanning("categoryPlanning", "品类企划"),
    planningProject("planningProject", "企划看板"),
    ChartBar("ChartBar", "波段汇总统计"),
    ProductBar("ProductBar", "品类汇总统计"),
    //款式设计
    design_task("design_task", "款式设计任务"),
    style_info("style_info", "设计档案"),
    StyleBoard("StyleBoard", "款式看板"),
    technologyCenter("technologyCenter", "技术中心看板"),
    componentLibrary("componentLibrary", "部件库"),
    FabricInformation("FabricInformation", "调样管理"),
    //款式分析
    styleMarking("styleMarking","款式打标"),
    markingCheckPage("markingCheckPage","款式分析"),
    //款式配色
    styleColor("styleColor", "款式配色"),
    packDesign("packDesign", "设计BOM"),
    styleAgent("styleAgent", "款式列表SKU"),
    packBigGoods("packBigGoods", "标准资料包"),
    bulkCargoStyle("bulkCargoStyle", "大货款列表"),
    sample_capacity_total_count("sample_capacity_total_count", "样衣产能总数"),
    capacity_contrast_statistics("capacity_contrast_statistics", "样衣产能对比"),
    style_group("style_group", "款式搭配"),
    style_order_book("style_order_book", "订货本"),
    order_book_follow("order_book_follow", "订货本跟进"),
    style_research_node("style_research_node", "研发总进度"),
    retentionStyle("retentionStyle", "滞留款查询"),
    fabric_summary("fabric_summary", "面料汇总"),
    style_pricing("style_pricing", "款式定价"),
    pricingTemplate("pricingTemplate", "核价模板"),
    costPricing("costPricing", "成本核价"),
    material("material", "物料清单"),
    style_color_correct_info("style_color_correct_info","正确样流转"),
    esOrderBook("esOrderBook","es订货本"),
    //打版管理
    patternMakingSteps("patternMakingSteps", "打版进度"),
    patternMakingTask("patternMakingTask","打版任务" ),
    sampleTask("sampleTask","样衣任务" ),
    blackPatternMakingTask("blackPatternMakingTask","黑单打版任务" ),
    blackSampleTask("blackSampleTask","黑单样衣任务" ),
    sampleBoard("sampleBoard", "样衣看板"),
    hangTagList("hangTagList", "吊牌列表"),
    styleCountryStatus("styleCountryStatus", "多语言列表"),
    pre_production_sample_task("pre_production_sample_task", "产前样样衣任务"),
    pre_production_sample_board("pre_production_sample_board", "产前样看板"),
    PatternMakingWeekMonthView("PatternMakingWeekMonthView", "版类对比"),
    CategorySummaryCount("CategorySummaryCount", "品类汇总统计"),

    //报表中心
    styleAnalyseDesign("styleAnalyseDesign", "设计分析报表"),
    hangTagReport("hangTagReport", "电商充绒量报表"),
    designOrderScheduleDetailsReport("designOrderScheduleDetailsReport", "设计下单进度明细报表"),
    styleSizeReport("styleSizeReport", "尺寸查询报表"),
    stylePackBomMaterialReport("stylePackBomMaterialReport", "BOM清单查询报表"),
    seasonPlanPercentage("seasonPlanPercentage", "季节企划完成率"),
    styleAnalyseStyle("styleAnalyseStyle", "大货分析报表"),
    patternMakingReport("patternMakingReport","下稿计划报表"),

    work_log("work_log","工作小账"),
    PATTERN_LIBRARY("t_pattern_library","版型库"),
    fabricSummaryList("fabricSummaryList", "面料详单"),
    hrTrafficLight("t_hr_traffic_light", "人事红绿灯"),
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
