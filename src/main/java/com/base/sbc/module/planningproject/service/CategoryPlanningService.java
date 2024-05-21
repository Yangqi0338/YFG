package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

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
            , List<Map<String, String>> queryList) ;


    /**
     * 获取所有等级的维度数据
     *
     * @param prodCategoryNameList 品类名称集合
     * @param channelCode          渠道 code
     * @param seasonId             产品季 id
     * @return
     */
    List<PlanningDimensionality> getPlanningDimensionalitieList(List<String> prodCategoryNameList, String channelCode, String seasonId);

    /**
     * 获取所有等级的维度数据，如果品类没有就使用中类，如果中类也没有 那么抛出异常
     *
     * @param resultMap   品类和中类名称 Map 集合
     * @param channelCode 渠道 code
     * @param seasonId    产品季 id
     * @return
     */
    List<PlanningDimensionality> getAllPlanningDimensionalitieList(Map<String, List<String>> resultMap, String channelCode, String seasonId);

    /**
     * 启用停用
     */
    void updateStatus(@RequestBody BaseDto baseDto);

    /**
     * 删除品类企划
     */
    void delByIds(RemoveDto removeDto);
}
