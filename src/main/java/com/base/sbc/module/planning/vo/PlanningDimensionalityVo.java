package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningDimensionality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanningDimensionalityVo extends PlanningDimensionality {



    @ApiModelProperty(value = "分组名称")
    private String groupName;
}
