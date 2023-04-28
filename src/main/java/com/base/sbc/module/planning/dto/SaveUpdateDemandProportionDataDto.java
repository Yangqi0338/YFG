package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*新增修改需求数据*/
@Data
@ApiModel("企划需求-新增修改需求数据 SaveUpdateDemandProportionDataDto")
public class SaveUpdateDemandProportionDataDto {
    private String id;

    @ApiModelProperty(value = "需求维度id" ,required = true,example = "122222")
    private String demandId;

    @ApiModelProperty(value = "分类" ,required = true,example = "122222")
    private String   classify;

    @ApiModelProperty(value = "占比" ,required = true,example = "122222")
    private String   proportion;
}
