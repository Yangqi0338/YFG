package com.base.sbc.client.amc.enums;

/**
 * @Author xhj
 * @Date 2023/7/13 9:07
 */
public enum DataPermissionsBusinessTypeEnum {
    PlanningSeason("PlanningSeason", "产品季"),
    PlanningCategoryItem("PlanningCategoryItem", "坑位数据"),
    design_task("design_task", "款式设计任务"),
    style_info("style_info", "设计档案"),
    sample_capacity_total_count("sample_capacity_total_count", "样衣产能总数"),
    capacity_contrast_statistics("capacity_contrast_statistics", "样衣产能对比"),
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
