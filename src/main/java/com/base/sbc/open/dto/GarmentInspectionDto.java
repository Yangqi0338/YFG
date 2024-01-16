package com.base.sbc.open.dto;

import lombok.Data;

import java.util.List;

@Data
public class GarmentInspectionDto {
    /**
     * 年份日期
     */
    private String year;

    /**
     * 送检日期
     */
    private String inspectDate;

    /**
     * 大货款号
     */
    private String styleNo;

    /**
     * 明细集合
     */
    private List<GarmentInspectionDetailDto> garmentInspectionDetailDtoList;
}
