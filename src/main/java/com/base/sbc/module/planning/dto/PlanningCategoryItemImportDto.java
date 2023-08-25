package com.base.sbc.module.planning.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PlanningCategoryItemImportDto {
    /** 特殊需求 */
    @Excel(name = "特殊需求")
    private String specialNeedsFlag;

    /** 重点款 */
    @Excel(name = "重点款")
    private String importantStyleFlag;

    /** 月份+波段 */
    @Excel(name = "月份+波段")
    private String monthBandName;

    /** 大类 */
    @Excel(name = "大类")
    private String prodCategory;

    /** 中类 */
    @Excel(name = "中类")
    private String prodCategory2nd;

    /** 小类 */
    @Excel(name = "小类")
    private String prodCategory3rd;
}
