package com.base.sbc.module.planningproject.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQueryDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
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
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            categoryPlanningDetail.setId(null);
            categoryPlanningDetail.setCategoryPlanningId(categoryPlanning.getId());
            categoryPlanningDetail.setCategoryPlanningName(categoryPlanning.getName());
        }
        categoryPlanningDetailsService.saveBatch(categoryPlanningDetails);
        return success("生成品类企划成功");
    }

    /**
     * 启用停用
     */
    @ApiOperation(value = "启用停用")
    @PostMapping("/updateStatus")
    public ApiResult updateStatus(@RequestBody BaseDto baseDto){
        String ids = baseDto.getIds();
        if ("0".equals(baseDto.getStatus())){
            List<String> idList = Arrays.asList(ids.split(","));
            List<CategoryPlanning> categoryPlannings = categoryPlanningService.listByIds(idList);
            List<String> seasonIds = categoryPlannings.stream().map(CategoryPlanning::getSeasonId).collect(Collectors.toList());
            List<String> channelCodes = categoryPlannings.stream().map(CategoryPlanning::getChannelCode).collect(Collectors.toList());

            QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("season_id", seasonIds);
            queryWrapper.in("channel_code", channelCodes);
            queryWrapper.notIn("id",idList);
            queryWrapper.eq("status","0");
            long l = categoryPlanningService.count(queryWrapper);
            if (l > 0){
                throw new RuntimeException("已存在启用的品类企划");
            }
        }
        UpdateWrapper<CategoryPlanning> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", baseDto.getStatus());
        updateWrapper.in("id", Arrays.asList(ids.split(",")));
        categoryPlanningService.update(updateWrapper);

        return updateSuccess("更新成功");
    }

    /**
     * 删除品类企划
     */
    @ApiOperation(value = "删除品类企划")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto){
        String ids = removeDto.getIds();
        List<String> list = Arrays.asList(ids.split(","));
        QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",list);
        queryWrapper.eq("status","0");
        long l = categoryPlanningService.count(queryWrapper);
        if (l > 0){
            throw new RuntimeException("存在启用的季节企划,不能删除");
        }
        categoryPlanningService.removeByIds(list);

        return deleteSuccess("删除成功");
    }
}
