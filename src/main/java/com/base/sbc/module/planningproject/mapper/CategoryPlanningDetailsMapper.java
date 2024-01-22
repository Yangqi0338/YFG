package com.base.sbc.module.planningproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-22 11:28:30
 * @mail 247967116@qq.com
 */
@Mapper
public interface CategoryPlanningDetailsMapper extends BaseMapper<CategoryPlanningDetails> {
    List<CategoryPlanningDetailsVo> listByQueryWrapper(@Param("ew") BaseQueryWrapper<CategoryPlanningDetails> ew);
}
