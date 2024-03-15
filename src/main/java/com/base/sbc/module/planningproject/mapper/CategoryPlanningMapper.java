package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-19 17:18:45
 * @mail 247967116@qq.com
 */
@Mapper
public interface CategoryPlanningMapper extends BaseMapper<CategoryPlanning> {
    List<CategoryPlanningVo> listByQueryWrapper(@Param("ew") BaseQueryWrapper<CategoryPlanning> ew);
}
