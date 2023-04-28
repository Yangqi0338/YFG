package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询维度dto QueryPlanningDimensionalityDto")
public class QueryPlanningDimensionalityDto {

    /*品类id*/
    @ApiModelProperty(value = "品类id" ,required = true,example = "111")
    private String categoryId;

    @ApiModelProperty(value = "产品季id" ,required = true,example = "111")
    private String planningSeasonId;
}
