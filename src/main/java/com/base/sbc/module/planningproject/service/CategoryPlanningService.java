package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
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

    /**
     * 根据季节企划详情数据进行品类企划详情的组装生成
     * @param detailsList 季节企划详情集合数据
     * @param seasonalPlanning 季节企划数据
     * @param categoryPlanning 品类企划数据
     * @return 品类企划详情集合数据
     */
    List<CategoryPlanningDetails> generationCategoryPlanningDetails(
            List<SeasonalPlanningDetails> detailsList
            , SeasonalPlanning seasonalPlanning
            , CategoryPlanning categoryPlanning
            , List<String> dimensionIdList) ;
}
