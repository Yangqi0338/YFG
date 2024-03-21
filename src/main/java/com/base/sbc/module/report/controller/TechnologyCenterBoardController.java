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

@RestController
@Api(tags = "技术中心看板")
@RequestMapping(value = BaseController.SAAS_URL + "/technologyCenterBoard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class TechnologyCenterBoardController {
    @Autowired
    private TechnologyCenterBoardService technologyCenterBoardService;


    @ApiOperation(value = "设计下单进度明细报表-导出")
    @GetMapping("/designOrderScheduleDetailsExport")
    public TechnologyCenterBoardOverviewDataVo hangTagReportExport(TechnologyCenterBoardDto dto){
        return technologyCenterBoardService.getPlateMakeUnderwayData(dto);
    }

}
