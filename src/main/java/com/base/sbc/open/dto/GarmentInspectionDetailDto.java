package com.base.sbc.open.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GarmentInspectionDetailDto {
    /**
     * 物料厂家编号
     */
    private String matFactoryCode;
    /**
     * 校验百分比
     */
    private Boolean checkPercentage;

    /**
     * 材料类型
     */
    private String matTypeCode;

    /**
     * 材料类型名称
     */
    private String matType;

    /**
     * 二级分类名称
     */
    private String secondCategory;
    /**
     * 二级分类code
     */
    private String secondCategoryCode;

    /**
     * 百分比
     */
    private BigDecimal percentageQty;

    /**
     * 成分code
     */
    private String ingredientCode;
    /**
     * 成分名称
     */
    private String ingredientName;

    /**
     * 成分说明code
     */
    private String ingredientExplainCode;
    /**
     * 成分说明name
     */
    private String ingredientExplainName;

    /**
     * 检测单位code
     */
    private String testUnitCode;

    /**
     * 检测单位name
     */
    private String testUnitName;

    /**
     * 报告附件
     */
    private String attachmentUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 删除标识 0：正常，1：删除
     */
    private String delFlag;



}
