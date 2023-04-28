package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanningDemandProportionDataVo {

    /** 需求维度id */
    @ApiModelProperty(value = "需求维度id"  )
    private String demandDimensionalityId;
    /** 分类,维度 */
    @ApiModelProperty(value = "分类,维度"  )
    private String classifyDimensionality;
    /** 占比，检查 */
    @ApiModelProperty(value = "占比，检查"  )
    private String proportionExamine;
}
