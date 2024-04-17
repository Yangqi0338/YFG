package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-19 17:19:08
 * @mail 247967116@qq.com
 */
public interface CategoryPlanningService extends BaseService<CategoryPlanning> {
    List<CategoryPlanningVo> queryList(CategoryPlanningQueryDto categoryPlanningQueryDto);

    List<CategoryPlanningVo> queryPage(CategoryPlanningQueryDto categoryPlanningQueryDto);

    /**
     * 根据季节企划生成品类企划,同时生成企划看板
     */
    void generateCategoryPlanningNew(BaseDto baseDto);
}
