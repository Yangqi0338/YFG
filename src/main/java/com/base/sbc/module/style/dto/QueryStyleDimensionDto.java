package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryStyleDimensionDto {
    private String status;

    private String prodCategory1st;

    private String prodCategory;

    private String bandCode;

    private String planningSeasonId;
}
