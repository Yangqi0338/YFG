package com.base.sbc.module.planningproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.service.CategoryPlanningService;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResult generateCategoryPlanning(BaseDto baseDto) {
        String ids = baseDto.getIds();
        // 生成品类企划
        List<SeasonalPlanning> seasonalPlannings = seasonalPlanningService.listByIds(Arrays.asList(ids.split(",")));
        QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        long l = categoryPlanningService.count(queryWrapper);
        for (SeasonalPlanning seasonalPlanning : seasonalPlannings) {
            CategoryPlanning categoryPlanning = new CategoryPlanning();
            categoryPlanning.setName(seasonalPlanning.getName());
            categoryPlanning.setChannelCode(seasonalPlanning.getChannelCode());
            categoryPlanning.setChannelName(seasonalPlanning.getChannelName());
            categoryPlanning.setSeasonId(seasonalPlanning.getSeasonId());
            categoryPlanning.setSeasonName(seasonalPlanning.getSeasonName());
            categoryPlanning.setStatus(l == 0 ? "0" : "1");
            categoryPlanningService.save(categoryPlanning);
        }

        return success("生成品类企划成功");
    }
}
