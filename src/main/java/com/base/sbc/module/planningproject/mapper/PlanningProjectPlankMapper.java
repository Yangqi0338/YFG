package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/11/17 9:44:26
 * @mail 247967116@qq.com
 */
@Mapper
public interface PlanningProjectPlankMapper extends BaseMapper<PlanningProjectPlank>{
    List<PlanningProjectPlankVo> queryPage(@Param("ew") QueryWrapper<PlanningProjectPlank> queryWrapper);
}
