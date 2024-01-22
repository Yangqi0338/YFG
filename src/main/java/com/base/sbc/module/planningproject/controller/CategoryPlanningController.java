package com.base.sbc.module.planningproject.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.service.CategoryPlanningDetailsService;
import com.base.sbc.module.planningproject.service.CategoryPlanningService;
import com.base.sbc.module.planningproject.service.SeasonalPlanningDetailsService;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author 卞康
 * @date 2024-01-19 17:22:13
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "品类企划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/categoryPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class CategoryPlanningController extends BaseController {
    private final CategoryPlanningService categoryPlanningService;
    private final SeasonalPlanningService seasonalPlanningService;
    private final SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    private final CategoryPlanningDetailsService categoryPlanningDetailsService;

    /**
     * 根据条件查询列表
     */
    @GetMapping("/queryList")
    public ApiResult queryList(CategoryPlanningQueryDto categoryPlanningQueryDto) {
        List<CategoryPlanningVo> categoryPlanningVos = categoryPlanningService.queryList(categoryPlanningQueryDto);
        return selectSuccess(categoryPlanningVos);
    }

    /**
     * 根据季节企划生成品类企划
     */
    @PostMapping("/generateCategoryPlanning")
    public ApiResult generateCategoryPlanning(@RequestBody BaseDto baseDto) {
        // 生成品类企划
        SeasonalPlanning seasonalPlanning = seasonalPlanningService.getById(baseDto.getId());
        QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        queryWrapper.eq("seasonal_planning_id", seasonalPlanning.getId());
        queryWrapper.eq("channel_code", seasonalPlanning.getChannelCode());
        queryWrapper.eq("company_code", getUserCompany());
        long l = categoryPlanningService.count(queryWrapper);
        CategoryPlanning categoryPlanning = new CategoryPlanning();
        categoryPlanning.setName(seasonalPlanning.getName());
        categoryPlanning.setChannelCode(seasonalPlanning.getChannelCode());
        categoryPlanning.setChannelName(seasonalPlanning.getChannelName());
        categoryPlanning.setSeasonId(seasonalPlanning.getSeasonId());
        categoryPlanning.setSeasonName(seasonalPlanning.getSeasonName());
        categoryPlanning.setSeasonalPlanningId(seasonalPlanning.getId());
        categoryPlanning.setStatus(l == 0 ? "0" : "1");
        categoryPlanningService.save(categoryPlanning);

        // 生成品类企划明细
        List<SeasonalPlanningDetails> detailsList = seasonalPlanningDetailsService.listByField("seasonal_planning_id", seasonalPlanning.getId());
        List<CategoryPlanningDetails> categoryPlanningDetails = BeanUtil.copyToList(detailsList, CategoryPlanningDetails.class);
        for (CategoryPlanningDetails categoryPlanningDetail : categoryPlanningDetails) {
            categoryPlanningDetail.setCategoryPlanningId(categoryPlanning.getId());
            categoryPlanningDetail.setCategoryPlanningName(categoryPlanning.getName());
        }
        categoryPlanningDetailsService.saveBatch(categoryPlanningDetails);
        return success("生成品类企划成功");
    }
}
