package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailDTO;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
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
     * 品类企划暂存
     *
     * @param categoryPlanningDetailsList 要暂存的数据
     */
    void staging(List<CategoryPlanningDetails> categoryPlanningDetailsList);

    /**
     * 品类企划保存
     *
     * @param categoryPlanningDetailsList 要保存的数据
     */
    void preservation(List<CategoryPlanningDetails> categoryPlanningDetailsList);

    /**
     * 根据品类（多选逗号分隔）和品类企划 ID 获取维度系数
     *
     * @param categoryPlanningDetailDTO 要保存的数据
     */
    List<CategoryPlanningDetails> getDimensionality(CategoryPlanningDetailDTO categoryPlanningDetailDTO);
}
