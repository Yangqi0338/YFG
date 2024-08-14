package com.base.sbc.module.smp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Scm1SpareMaterialDTO {

    private String traceId;
    private String brandId;
    private String brandName;
    private String materialNo;
    private Integer demandQuantity;
    private Integer noOccupyQuantity;
    private Integer productQuantity;

    private Integer spareType;
    private String spareTypeName;


}