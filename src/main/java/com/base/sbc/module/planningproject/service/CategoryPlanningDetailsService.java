package com.base.sbc.module.planningproject.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
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
}
