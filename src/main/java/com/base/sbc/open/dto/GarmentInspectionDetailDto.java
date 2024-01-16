package com.base.sbc.open.dto;

import lombok.Data;

import java.util.List;

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
     * 排序
     */
    private Integer sort;

}
