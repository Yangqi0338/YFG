package com.base.sbc.open.dto;

import lombok.Data;

import java.util.List;

@Data
public class GarmentInspectionDto {
    /**
     * 年份
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
     * 备注
     */
    private String remark;

    /**
     * 明细集合
     */
    private List<GarmentInspectionDetailDto> garmentInspectionDetailDtoList;
}
