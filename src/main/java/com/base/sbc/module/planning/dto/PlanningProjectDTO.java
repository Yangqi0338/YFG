package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningProject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("企划看板规划筛选条件 PlanningBoardSearchDto")
public class PlanningProjectDTO extends PlanningProject {
}
