package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.PlanningProjectDTO;
import com.base.sbc.module.planning.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planning.entity.PlanningProject;
import com.base.sbc.module.planning.vo.PlanningProjectVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PlanningProjectService extends BaseService<PlanningProject> {
    PageInfo<PlanningProjectVo> planningProject(PlanningProjectPageDTO dto);

    String planningProjectAdd(PlanningProjectDTO dto);

    boolean planningProjectDel(String id);

    String planningProjectUpdate(PlanningProjectDTO dto);
}
