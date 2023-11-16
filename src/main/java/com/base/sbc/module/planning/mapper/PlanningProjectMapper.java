package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planning.dto.PlanningProjectDTO;
import com.base.sbc.module.planning.dto.PlanningProjectPageDTO;
import com.base.sbc.module.planning.entity.PlanningProject;
import com.base.sbc.module.planning.vo.PlanningProjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanningProjectMapper extends BaseMapper<PlanningProject> {
    List<PlanningProjectVo> getplanningProjectList(@Param("dto") PlanningProjectPageDTO dto);

}
