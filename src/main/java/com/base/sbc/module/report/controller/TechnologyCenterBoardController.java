package com.base.sbc.module.report.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.service.ReportService;
import com.base.sbc.module.report.service.StyleAnalyseService;
import com.base.sbc.module.report.service.TechnologyCenterBoardService;
import com.base.sbc.module.report.vo.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(tags = "技术中心看板")
@RequestMapping(value = BaseController.SAAS_URL + "/technologyCenterBoard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class TechnologyCenterBoardController {
    @Autowired
    private TechnologyCenterBoardService technologyCenterBoardService;


    @ApiOperation(value = "数据总览-打版进行中数据")
    @GetMapping("/getPlateMakeUnderwayData")
    public TechnologyCenterBoardOverviewDataVo getPlateMakeUnderwayData(TechnologyCenterBoardDto dto){
        return technologyCenterBoardService.getPlateMakeUnderwayData(dto);
    }
    @ApiOperation(value = "数据总览-打版完成数据")
    @GetMapping("/getPlateMakeFinishData")
    public TechnologyCenterBoardOverviewDataVo getPlateMakeFinishData(TechnologyCenterBoardDto dto){
        return technologyCenterBoardService.getPlateMakeFinishData(dto);
    }
    @ApiOperation(value = "版师/样衣工,当前任务")
    @GetMapping("/getCurrentTaskData")
    public List<TechnologyCenterBoardCurrentTaskVo> getCurrentTaskData(TechnologyCenterBoardDto dto){
        return technologyCenterBoardService.getCurrentTaskData(dto);
    }
    @ApiOperation(value = "打版/样衣,产能数")
    @GetMapping("/getCapacityNumber")
    public List<TechnologyCenterBoardCapacityNumberVo> getCapacityNumber(TechnologyCenterBoardDto dto){
        return technologyCenterBoardService.getCapacityNumber(dto);
    }
    @ApiOperation(value = "打版/样衣,排名统计")
    @GetMapping("/getDesignerRank")
    public List<TechnologyCenterBoardDesignerRankVo> getDesignerRank(TechnologyCenterBoardDto dto){
        return technologyCenterBoardService.getDesignerRank(dto);
    }

}
