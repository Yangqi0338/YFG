package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.common.vo.BasePageInfo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planningproject.dto.PlanningProjectDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

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

    /**
     * 企划看板 2.0 企划看板计划查询
     *
     * @param dto 查询条件
     * @return 企划看板信息
     */
    PageInfo<PlanningProjectVo> queryPageNew(PlanningProjectPageDTO dto);

    /**
     * 根据品类和企划看板 ID 获取维度系数
     *
     * @param planningProjectDTO 筛选条件
     */
    List<PlanningProjectDimension> getDimensionality(PlanningProjectDTO planningProjectDTO);

    /**
     * 查询已生成的品类信息
     *
     * @param planningProjectDTO 筛选条件
     */
    List<Map<String, Object>> getProdCategory(PlanningProjectDTO planningProjectDTO);

    /**
     * 启用/停用
     *
     * @param ids
     * @param status
     */
    void startStop(String ids, String status);

    /**
     * 根据ids删除
     */
    void delByIds(String ids);
}
