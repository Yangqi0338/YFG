package com.base.sbc.client.amc.enums;

/**
 * @Author xhj
 * @Date 2023/7/13 9:07
 */
public enum DataPermissionsBusinessTypeEnum {
    PlanningSeason("PlanningSeason", "产品季"),
    PlanningCategoryItem("PlanningCategoryItem", "坑位数据"),
    ChartBar("ChartBar", "波段汇总统计"),
    ProductBar("ProductBar", "品类汇总统计"),
    design_task("design_task", "款式设计任务"),
    style_info("style_info", "设计档案"),
    styleColor("styleColor", "款式配色"),
    packDesign("packDesign", "设计BOM"),
    packBigGoods("packBigGoods", "标准资料包"),
    sample_capacity_total_count("sample_capacity_total_count", "样衣产能总数"),
    capacity_contrast_statistics("capacity_contrast_statistics", "样衣产能对比"),
    style_group("style_group", "款式搭配"),
    style_order_book("style_order_book", "订货本"),
    fabric_summary("fabric_summary", "面料汇总"),
    style_pricing("style_pricing", "款式定价"),
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
