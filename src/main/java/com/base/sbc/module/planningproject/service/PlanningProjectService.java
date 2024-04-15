package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.common.vo.BasePageInfo;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import com.github.pagehelper.PageInfo;

public interface PlanningProjectService extends BaseService<PlanningProject> {
    PageInfo<PlanningProjectVo> queryPage(PlanningProjectPageDTO dto);

    /**
     * 保存企划看板规划信息
     *
     * @param planningProjectSaveDTO 实体对象
     * @return 返回的结果
     */
    boolean save(PlanningProjectSaveDTO planningProjectSaveDTO);

    BasePageInfo<PlanningSeasonOverviewVo> historyList(PlanningProjectPageDTO dto);
}
