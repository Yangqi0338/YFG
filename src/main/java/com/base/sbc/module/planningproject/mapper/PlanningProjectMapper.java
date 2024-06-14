package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanningProjectMapper extends BaseMapper<PlanningProject> {
    List<PlanningProject> listByQueryWrapper(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<PlanningSeasonOverviewVo> historyList(@Param(Constants.WRAPPER) QueryWrapper qw);
}
