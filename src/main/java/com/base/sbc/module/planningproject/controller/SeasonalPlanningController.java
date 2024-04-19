package com.base.sbc.module.planningproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.planningproject.dto.CategoryPlanningQuerySaveDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.service.SeasonalPlanningService;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:27
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "季节企划-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/seasonalPlanning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class SeasonalPlanningController extends BaseController {
    private final SeasonalPlanningService seasonalPlanningService;

    /**
     * 导入季节企划
     */
    @ApiOperation(value = "导入Excel")
    @PostMapping("/importExcel")
    @DuplicationCheck(type = 1,time = 20)
    public ApiResult importExcel(MultipartFile file,  SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws Exception {
        seasonalPlanningSaveDto.setCompanyCode(this.getUserCompany());
        return seasonalPlanningService.importSeasonalPlanningExcel(file,seasonalPlanningSaveDto);
    }

    /**
     * 季节企划详情
     */
    @ApiOperation(value = "季节企划详情")
    @PostMapping("/getSeasonalPlanningDetails")
    @DuplicationCheck(type = 1,time = 20)
    public ApiResult getSeasonalPlanningDetails(@RequestBody SeasonalPlanningDetails seasonalPlanningDetails) throws Exception {
        return seasonalPlanningService.getSeasonalPlanningDetails(seasonalPlanningDetails);
    }

    /**
     * 根据条件查询
     */
    @ApiOperation(value = "根据条件查询")
    @GetMapping("/queryList")
    public ApiResult queryList(SeasonalPlanningQueryDto seasonalPlanningQueryDto){
        List<SeasonalPlanningVo> list = seasonalPlanningService.queryList(seasonalPlanningQueryDto);
        return selectSuccess(list);
    }

    /**
     * 根据id查询详情
     */
    @ApiOperation(value = "根据id查询详情")
    @GetMapping("/getDetailById")
    public ApiResult getDetailById(@RequestParam String id){
        SeasonalPlanningVo seasonalPlanningVo = seasonalPlanningService.getDetailById(id);
        return selectSuccess(seasonalPlanningVo);
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
            List<SeasonalPlanning> seasonalPlannings = seasonalPlanningService.listByIds(idList);
            List<String> seasonIds = seasonalPlannings.stream().map(SeasonalPlanning::getSeasonId).collect(Collectors.toList());
            List<String> channelCodes = seasonalPlannings.stream().map(SeasonalPlanning::getChannelCode).collect(Collectors.toList());

            QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("season_id", seasonIds);
            queryWrapper.in("channel_code", channelCodes);
            queryWrapper.notIn("id",idList);
            queryWrapper.eq("status","0");
            long l = seasonalPlanningService.count(queryWrapper);
            if (l > 0){
                throw new RuntimeException("已存在启用的季节企划");
            }
        }
        UpdateWrapper<SeasonalPlanning> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", baseDto.getStatus());
        updateWrapper.in("id", Arrays.asList(ids.split(",")));
        seasonalPlanningService.update(updateWrapper);
        return updateSuccess("更新成功");
    }

    /**
     * 删除季节企划
     */
    @ApiOperation(value = "删除季节企划")
    @DeleteMapping("/delByIds")
    public ApiResult delByIds(RemoveDto removeDto){
        String ids = removeDto.getIds();
        List<String> list = Arrays.asList(ids.split(","));
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", list);
        queryWrapper.eq("status","0");
        long l = seasonalPlanningService.count(queryWrapper);
        if (l > 0){
            throw new RuntimeException("存在启用的季节企划,不能删除");
        }
        seasonalPlanningService.removeByIds(list);
        return deleteSuccess("删除成功");
    }
}
