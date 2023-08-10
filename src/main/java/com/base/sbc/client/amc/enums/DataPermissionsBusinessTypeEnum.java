package com.base.sbc.client.amc.enums;

/**
 * @Author xhj
 * @Date 2023/7/13 9:07
 */
public enum DataPermissionsBusinessTypeEnum {
    SAMPLE_DESIGN("sample_design", "款式设计"),
    PIT_LOCATION("pit_location", "坑位数据"),
    PLANNING_KANBAN("planning_kanban", "企划看板"),
    DESIGN_TASK("design_task", "设计任务"),
    STYLE_INFO("style_info", "款式资料"),
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
