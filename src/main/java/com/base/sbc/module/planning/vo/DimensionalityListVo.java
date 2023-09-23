package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningDimensionality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
/*维度*/
public class DimensionalityListVo {

    @ApiModelProperty(value = "维度表")
    List<PlanningDimensionality> planningDimensionalities;

    @ApiModelProperty(value = "品类标识 0品类 1中类")
    private String categoryFlag;

}
