package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningDimensionality;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/*编辑维度标签*/
@Data
@ApiModel("企划维度-编辑维度标签 SaveUpdateDemandProportionDataDto")
public class UpdateDimensionalityDto extends PlanningDimensionality {


    private String planningSeasonName;
}
