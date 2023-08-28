package com.base.sbc.module.planning.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询需求dto QueryDemandDto")
public class QueryDemandDto {

    /*产品季id*/
    @ApiModelProperty(value = "产品季id" ,required = true,example = "111")
    private String planningSeasonId;


    /*品类id*/
    @ApiModelProperty(value = "品类id", required = true, example = "111")
    private String categoryId;


    @ApiModelProperty(value = "表单编码", required = false, example = "122222")
    private String formCode;

    @ApiModelProperty(value = "维度字段id", required = false, example = "122222")
    private String fieldId;
}
