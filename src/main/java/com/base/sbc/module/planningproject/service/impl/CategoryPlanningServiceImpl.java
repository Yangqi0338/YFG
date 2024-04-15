package com.base.sbc.module.planningproject.service.impl;

import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningMapper;
import com.base.sbc.module.planningproject.service.CategoryPlanningService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-19 17:19:27
 * @mail 247967116@qq.com
 */
@Service
public class CategoryPlanningServiceImpl extends BaseServiceImpl<CategoryPlanningMapper, CategoryPlanning> implements CategoryPlanningService {
    /**
     * @param categoryPlanningQueryDto
     * @return
     */
    @Override
    public List<CategoryPlanningVo> queryList(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        BaseQueryWrapper<CategoryPlanning> queryWrapper = this.buildQueryWrapper(categoryPlanningQueryDto);
        return  baseMapper.listByQueryWrapper(queryWrapper);
    }

    @Override
    public List<CategoryPlanningVo> queryPage(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        PageHelper.startPage(categoryPlanningQueryDto);
         return this.queryList(categoryPlanningQueryDto);
    }

    /**
     * 构造查询条件
     */
    private BaseQueryWrapper<CategoryPlanning> buildQueryWrapper(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        BaseQueryWrapper<CategoryPlanning> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("channel_code", categoryPlanningQueryDto.getChannelCode());
        queryWrapper.notEmptyEq("season_id", categoryPlanningQueryDto.getSeasonId());
        queryWrapper.notEmptyLike("name", categoryPlanningQueryDto.getYearName());
        queryWrapper.orderByDesc("id");
        return queryWrapper;
    }
}
