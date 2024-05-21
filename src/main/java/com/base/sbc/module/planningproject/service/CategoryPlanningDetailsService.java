package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailDTO;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailVO;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-22 11:29:05
 * @mail 247967116@qq.com
 */
public interface CategoryPlanningDetailsService extends BaseService<CategoryPlanningDetails> {
    PageInfo<CategoryPlanningDetailsVo> queryPage(CategoryPlanningDetailsQueryDto dto);

    List<CategoryPlanningDetailsVo> queryList(CategoryPlanningDetailsQueryDto dto);

    CategoryPlanningDetailsVo getDetailById(String id);

    boolean updateDetail(CategoryPlanningDetailsVo categoryPlanningDetailsVo);


    /**
     * 根据id查询明细详情
     *
     * @param categoryPlanningDetailDTO 查询条件
     * @return 品类企划数据
     */
    CategoryPlanningDetailVO getDetail(CategoryPlanningDetailDTO categoryPlanningDetailDTO);

    /**
     * 品类企划保存
     *
     * @param categoryPlanningDetailsList 要保存的数据
     */
    void staging(List<CategoryPlanningDetails> categoryPlanningDetailsList);

    /**
     * 品类企划审核
     *
     * @param categoryPlanningDetailsList 要审核的数据
     */
    void preservation(List<CategoryPlanningDetails> categoryPlanningDetailsList);

    /**
     * 根据品类（多选逗号分隔）和品类企划 ID 获取维度系数
     *
     * @param categoryPlanningDetailDTO 要保存的数据
     */
    List<CategoryPlanningDetails> getDimensionality(CategoryPlanningDetailDTO categoryPlanningDetailDTO);

    /**
     * 作废品类企划 只能作废未提交的数据 作废的粒度是到品类级别
     *
     * @param categoryPlanningDetailDTO 要作废的数据
     */
    void revocation(CategoryPlanningDetailDTO categoryPlanningDetailDTO);

    /**
     * 反审核品类企划 只能反审核已审核的数据 反审核粒度是到维度级别
     *
     * @param categoryPlanningDetailsList 要反审核的数据
     */
    void reverseAudit(List<CategoryPlanningDetails> categoryPlanningDetailsList);

    /**
     * 品类企划更新接口
     *
     * @param seasonalPlanningDetailsList   季节企划重新导入后 需要修改和需要新增的季节企划的数据
     * @param removeSeasonPlaningDetailList 季节企划重新导入后 需要删除季节企划的数据
     */
    void updateBySeasonalPlanning(List<SeasonalPlanningDetails> seasonalPlanningDetailsList,
                                  List<SeasonalPlanningDetails> removeSeasonPlaningDetailList);
}
