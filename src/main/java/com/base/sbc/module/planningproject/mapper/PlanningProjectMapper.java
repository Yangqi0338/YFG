package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planningproject.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.vo.PlanningProjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanningProjectMapper extends BaseMapper<PlanningProject> {
    List<PlanningProjectVo> getplanningProjectList(@Param("dto") PlanningProjectPageDTO dto);

}
