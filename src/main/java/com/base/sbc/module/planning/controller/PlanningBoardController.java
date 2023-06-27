package com.base.sbc.module.planning.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 类描述：企划看板相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.controller.PlanningBoardController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 09:23
 */
@RestController
@Api(tags = "企划看板-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningBoard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningBoardController {


    @Resource
    private PlanningSeasonService planningSeasonService;

    @ApiOperation(value = "企划汇总", notes = "")
    @PostMapping("/planningSummary")
    public PlanningSummaryVo planningSummary(@Valid @RequestBody PlanningBoardSearchDto dto) {
        return planningSeasonService.planningSummary(dto);
    }


}
