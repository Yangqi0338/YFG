package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanningProjectMapper extends BaseMapper<PlanningProject> {

    void historyList(PlanningProjectPageDTO dto);
}
