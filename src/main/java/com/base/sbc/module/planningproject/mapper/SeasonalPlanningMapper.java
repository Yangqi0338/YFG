package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-18 17:06:47
 * @mail 247967116@qq.com
 */
@Mapper
public interface SeasonalPlanningMapper extends BaseMapper<SeasonalPlanning> {
    List<SeasonalPlanningVo> listByQueryWrapper(@Param("ew") QueryWrapper<SeasonalPlanning> ew);
}
