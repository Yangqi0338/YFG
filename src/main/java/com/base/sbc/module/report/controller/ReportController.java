package com.base.sbc.module.report.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.report.dto.HangTagReportQueryDto;
import com.base.sbc.module.report.service.ReportService;
import com.base.sbc.module.report.vo.HangTagReportVo;
import com.base.sbc.module.style.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.style.service.StyleAnalyseService;
import com.base.sbc.module.style.vo.StyleAnalyseVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "报表中心")
@RequestMapping(value = BaseController.SAAS_URL + "/report", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ReportController {

    @Autowired
    private StyleAnalyseService styleAnalyseService;

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/findDesignPage")
    public PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findDesignPage(dto);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/findStylePage")
    public PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

    @ApiOperation(value = "材料请求与报价")
    @GetMapping("/findMaterialsAndQuoteReport")
    public PageInfo<StyleAnalyseVo> findMaterialsAndQuote(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

    @ApiOperation(value = "BOM清单查询报表")
    @GetMapping("/findStyleBomListReport")
    public PageInfo<StyleAnalyseVo> findStyleBomList(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

    @ApiOperation(value = "尺寸查询报表")
    @GetMapping("/findStyleSizeReport")
    public PageInfo<StyleAnalyseVo> findStyleSize(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

    @ApiOperation(value = "电商充绒量")
    @GetMapping("/hangTagReport")
    public PageInfo<HangTagReportVo> hangTagReport(HangTagReportQueryDto dto) {
        return reportService.getHangTagReortPage(dto);
    }

}
