package com.base.sbc.module.planningproject.vo;

import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("企划看板规划Vo PlanningProjectVo")
public class PlanningProjectVo extends PlanningProject {
    private List<PlanningProjectMaxCategory> planningProjectMaxCategoryList;

    private List<PlanningProjectDimension> planningProjectDimensionList;

    /**
     * 季节企划Id
     */
    private String seasonalPlanningId;
}
