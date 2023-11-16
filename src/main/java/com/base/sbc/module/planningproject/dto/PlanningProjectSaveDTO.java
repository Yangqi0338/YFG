package com.base.sbc.module.planningproject.dto;

import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("企划看板规划保存dto")
public class PlanningProjectSaveDTO extends PlanningProject {

    private List<PlanningProjectMaxCategory> planningProjectMaxCategoryList;


}
