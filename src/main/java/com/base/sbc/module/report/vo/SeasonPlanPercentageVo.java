package com.base.sbc.module.report.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class SeasonPlanPercentageVo {

    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 年份名称
     */
    private String yearName;
    /**
     * 季节
     */
    private String season;
    /**
     * 季节名称
     */
    private String seasonName;
    /**
     * 波段名称
     */
    private String bandName;
    /**
     * 设计师
     */
    private String designer;
    /**
     * 品类名称
     */
    private String prodCategoryName;
    /**
     * 中类名称
     */
    private String prodCategory2ndName;
    /**
     * 列头筛选数量
     */
    private Integer groupCount;
    /**
     * 需求数量
     */
    private Integer skcCount;
    /**
     * 完成数量
     */
    private Integer completeCount;
    /**
     * 剩余数量
     */
    private Integer residueCount;
    /**
     * 完成率
     */
    private BigDecimal completePercentage;


    public Integer getResidueCount() {
        return skcCount - completeCount;
    }

    public BigDecimal getCompletePercentage() {
        return new BigDecimal(completeCount).divide(new BigDecimal(skcCount), RoundingMode.DOWN);
    }

}
