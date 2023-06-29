package com.base.sbc.module.sample.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.vo.CategoryStylePlanningVo;
import com.base.sbc.module.sample.vo.StyleBoardCategorySummaryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：款式看板相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.controller.StyleBoardController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-29 09:51
 */
@RestController
@Api(tags = "款式看板相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/styleBoard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleBoardController {

    @Resource
    private SampleDesignService sampleDesignService;

    @ApiOperation(value = "品类波段汇总", notes = "")
    @GetMapping("/categoryBandSummary")
    public PlanningSummaryVo categoryBandSummary(@Valid PlanningBoardSearchDto dto) {
        return sampleDesignService.categoryBandSummary(dto);
    }

    @ApiOperation(value = "品类数据汇总", notes = "")
    @GetMapping("/categorySummary")
    public List<StyleBoardCategorySummaryVo> categorySummary(PlanningBoardSearchDto dto) {
        return sampleDesignService.categorySummary(dto);
    }

    @ApiOperation(value = "品类款式规划", notes = "")
    @GetMapping("/categoryStylePlanning")
    public CategoryStylePlanningVo categoryStylePlanning(PlanningBoardSearchDto dto) {
        return sampleDesignService.categoryStylePlanning(dto);
    }

}
