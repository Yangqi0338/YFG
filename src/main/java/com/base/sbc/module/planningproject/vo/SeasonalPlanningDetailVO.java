package com.base.sbc.module.planningproject.vo;

import lombok.Data;

import java.util.Map;

@Data
public class SeasonalPlanningDetailVO {
    private Map<String, Map<String, String>> demandExcel;
    private Map<String, Map<String, String>> orderExcel;
    private Map<String, Map<String, String>> gapExcel;
}
