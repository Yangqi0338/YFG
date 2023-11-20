package com.base.sbc.module.planningproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.planningproject.dto.MatchSaveDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.dto.UnMatchDto;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:58
 * @mail 247967116@qq.com
 * 企划看板坑位
 */
@RestController
@RequestMapping(value = BaseController.SAAS_URL + "/planningProjectPlank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class PlanningProjectPlankController extends BaseController {
    private final PlanningProjectPlankService planningProjectPlankService;
    private final StyleColorService styleColorService;
    private final StyleService styleService;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    private final PlanningProjectDimensionService planningProjectDimensionService;

    /**
     * 查询列表
     */
    @ApiOperation(value = "查询列表")
    @GetMapping("/queryPage")
    public ApiResult queryPage(PlanningProjectPlankPageDto dto) {
        return selectSuccess(planningProjectPlankService.queryPage(dto));
    }

    /**
     * 匹配坑位
     */
    @ApiOperation(value = "匹配坑位")
    @PostMapping("/match")
    public ApiResult match(MatchSaveDto matchSaveDto) {
        StyleColor styleColor = styleColorService.getById(matchSaveDto.getStyleColorId());
        Style style = styleService.getById(styleColor.getStyleId());
        PlanningProjectPlank planningProjectPlank = planningProjectPlankService.getById(matchSaveDto.getPlankId());
        planningProjectPlank.setBulkStyleNo(styleColor.getStyleNo());
        planningProjectPlank.setStyleColorId(matchSaveDto.getStyleColorId());
        planningProjectPlank.setPic(style.getStylePic());
        planningProjectPlank.setBandName(styleColor.getBandName());
        planningProjectPlank.setBandCode(styleColor.getBandCode());
        planningProjectPlank.setMatchingStyleStatus("1");
        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColor.getColorCode()));
        if (colourLibrary != null) {
            planningProjectPlank.setColorSystem(colourLibrary.getColorType());
        }
        return updateSuccess("匹配成功");
    }

    /**
     * 匹配坑位
     */
    @ApiOperation(value = "取消匹配")
    @PostMapping("/unMatch")
    public ApiResult unMatch(UnMatchDto unMatchDto) {
        PlanningProjectPlank planningProjectPlank = planningProjectPlankService.getById(unMatchDto.getPlankId());
        PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getOne(new QueryWrapper<PlanningProjectDimension>().eq("planning_project_id", planningProjectPlank.getPlanningProjectId()));

        planningProjectPlank.setBulkStyleNo(null);
        planningProjectPlank.setStyleColorId(null);
        planningProjectPlank.setPic(null);
        planningProjectPlank.setBandName(planningProjectDimension.getBandName());
        planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
        planningProjectPlank.setMatchingStyleStatus("0");
        planningProjectPlank.setColorSystem(null);
        return updateSuccess("取消匹配成功");
    }

}
