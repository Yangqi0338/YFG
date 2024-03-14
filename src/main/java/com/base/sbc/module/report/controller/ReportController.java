package com.base.sbc.module.report.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.service.ReportService;
import com.base.sbc.module.report.service.StyleAnalyseService;
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
@Api(tags = "报表中心")
@RequestMapping(value = BaseController.SAAS_URL + "/report", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ReportController {
    @Autowired
    private StyleAnalyseService styleAnalyseService;

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "设计分析报表")
    @GetMapping("/findDesignPage")
    public PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findDesignPage(dto);
    }

    @ApiOperation(value = "设计分析报表-导出")
    @GetMapping("/findDesignPageExport")
    public void findDesignPageExport(HttpServletResponse response,StyleAnalyseQueryDto dto) throws IOException {
        styleAnalyseService.findDesignPageExport(response,dto);
    }

    @ApiOperation(value = "大货分析报表")
    @GetMapping("/findStylePage")
    public PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

    @ApiOperation(value = "材料请求与报价")
    @GetMapping("/materialsAndQuoteReport")
    public PageInfo<MaterialSupplierQuoteVo> findMaterialsAndQuote(MaterialSupplierQuoteQueryDto dto) {
        return reportService.getMaterialSupplierQuoteReportPage(dto);
    }

    @ApiOperation(value = "BOM清单查询报表")
    @GetMapping("/stylePackBomMaterialReport")
    public PageInfo<StylePackBomMaterialReportVo> findStyleBomList(StylePackBomMateriaQueryDto dto) {
        return reportService.getStylePackBomMaterialReportPage(dto);
    }

    @ApiOperation(value = "尺寸查询报表")
    @GetMapping("/styleSizeReport")
    public PageInfo<StyleSizeReportVo> findStyleSize(StyleSizeQueryDto dto) {
        return reportService.getStyleSizeReportPage(dto);
    }

    @ApiOperation(value = "电商充绒量")
    @GetMapping("/hangTagReport")
    public PageInfo<HangTagReportVo> hangTagReport(HangTagReportQueryDto dto) {
        return reportService.getHangTagReortPage(dto);
    }

}
