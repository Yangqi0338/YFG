package com.base.sbc.module.style.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.CategoryStylePlanningVo;
import com.base.sbc.module.style.vo.StyleBoardCategorySummaryVo;

import com.base.sbc.module.style.vo.StyleSummaryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：款式看板相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.style.controller.StyleBoardController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-29 09:51
 */
@RestController
@Api(tags = "款式看板相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/styleBoard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleBoardController {

    @Resource
    private StyleService styleService;

    @ApiOperation(value = "品类波段汇总", notes = "")
    @GetMapping("/categoryBandSummary")
    public StyleSummaryVo categoryBandSummary(Principal user, @Valid PlanningBoardSearchDto dto) {
        return styleService.categoryBandSummary(user, dto);
    }


    @ApiOperation(value = "品类波段汇总带维度", notes = "")
    @GetMapping("/categoryBandSummaryAddDimension")
    public StyleSummaryVo categoryBandSummaryAddDimension(Principal user, @Valid PlanningBoardSearchDto dto) {
        return styleService.categoryBandSummaryAddDimension(user, dto);
    }

    @ApiOperation(value = "品类数据汇总", notes = "")
    @GetMapping("/categorySummary")
    public List<StyleBoardCategorySummaryVo> categorySummary(PlanningBoardSearchDto dto) {
        return styleService.categorySummary(dto);
    }

    @ApiOperation(value = "品类款式规划", notes = "")
    @GetMapping("/categoryStylePlanning")
    public CategoryStylePlanningVo categoryStylePlanning(PlanningBoardSearchDto dto) {
        return styleService.categoryStylePlanning(dto);
    }

    /**
     * 查询「维度系数-面料类型」的数据
     * @return 下稿面料枚举
     */
    @ApiOperation(value = "下稿面料枚举查询", notes = "")
    @GetMapping("/getFabricsUnderTheDrafts")
    public FieldManagementVo getFabricsUnderTheDrafts() {
        return styleService.getFabricsUnderTheDrafts();
    }

}
