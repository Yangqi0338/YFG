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

    /** 月份 */
    @Excel(name = "月份")
    private String month;

    /** 波段 */
    @Excel(name = "波段")
    private String band;

    /** 旧款号 */
    @Excel(name = "旧款号")
    private String oldDesignNo;

    /** 款式风格 */
    @Excel(name = "款式风格")
    private String styleType;

    /** 四级类目 */
    @Excel(name = "四级类目")
    private String levelFourType;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

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
