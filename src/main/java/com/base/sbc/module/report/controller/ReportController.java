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
    public void findDesignPageExport(HttpServletResponse response, StyleAnalyseQueryDto dto) throws IOException {
        styleAnalyseService.findDesignPageExport(response, dto);
    }

    @ApiOperation(value = "大货分析报表")
    @GetMapping("/findStylePage")
    public PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto) {
        return styleAnalyseService.findStylePage(dto);
    }

    @ApiOperation(value = "大货分析报表-导出")
    @GetMapping("/findStylePageExport")
    public void findStylePageExport(HttpServletResponse response, StyleAnalyseQueryDto dto) throws IOException {
        styleAnalyseService.findStylePageExport(response, dto);
    }

    @ApiOperation(value = "供应商材料报价报表")
    @GetMapping("/materialsAndQuoteReport")
    public PageInfo<MaterialSupplierQuoteVo> findMaterialsAndQuote(MaterialSupplierQuoteQueryDto dto) {
        return reportService.getMaterialSupplierQuoteReportPage(dto);
    }

    @ApiOperation(value = "供应商材料报价报表-导出")
    @GetMapping("/materialSupplierQuoteExport")
    public void materialSupplierQuoteExport(HttpServletResponse response,MaterialSupplierQuoteQueryDto dto) throws IOException {
        reportService.materialSupplierQuoteExport(response,dto);
    }

    @ApiOperation(value = "BOM清单查询报表")
    @GetMapping("/stylePackBomMaterialReport")
    public PageInfo<StylePackBomMaterialReportVo> findStyleBomList(StylePackBomMateriaQueryDto dto) {
        return reportService.getStylePackBomMaterialReportPage(dto);
    }

    @ApiOperation(value = "BOM清单查询报表-导出")
    @GetMapping("/stylePackBomMaterialExport")
    public void stylePackBomMaterialExport(HttpServletResponse response,StylePackBomMateriaQueryDto dto) throws IOException {
         reportService.stylePackBomMaterialExport(response,dto);
    }

    @ApiOperation(value = "尺寸查询报表")
    @GetMapping("/styleSizeReport")
    public PageInfo<StyleSizeReportVo> findStyleSize(StyleSizeQueryDto dto) {
        return reportService.getStyleSizeReportPage(dto);
    }

    @ApiOperation(value = "尺寸查询报表-导出")
    @GetMapping("/styleSizeExport")
    public void styleSizeExport(HttpServletResponse response,StyleSizeQueryDto dto) throws IOException {
        reportService.styleSizeExport(response,dto);
    }


    @ApiOperation(value = "电商充绒量报表")
    @GetMapping("/hangTagReport")
    public PageInfo<HangTagReportVo> findHangTagReport(HangTagReportQueryDto dto) {
        return reportService.getHangTagReortPage(dto);
    }
    @ApiOperation(value = "电商充绒量报表-导出")
    @GetMapping("/hangTagReportExport")
    public void hangTagReportExport(HttpServletResponse response,HangTagReportQueryDto dto) throws IOException{
         reportService.hangTagReortExport(response,dto);
    }

    @ApiOperation(value = "设计下单进度明细报表")
    @GetMapping("/designOrderScheduleDetailsReport")
    public PageInfo<DesignOrderScheduleDetailsReportVo> findDesignOrderScheduleDetailsReport(DesignOrderScheduleDetailsQueryDto dto) {
        return reportService.getDesignOrderScheduleDetailsReportPage(dto);
    }

    @ApiOperation(value = "设计下单进度明细报表-导出")
    @GetMapping("/designOrderScheduleDetailsExport")
    public void hangTagReportExport(HttpServletResponse response, DesignOrderScheduleDetailsQueryDto dto) throws IOException {
        reportService.designOrderScheduleDetailsExport(response, dto);
    }

    @ApiOperation(value = "季节企划完成率报表")
    @GetMapping("/seasonPlanPercentage")
    public PageInfo<SeasonPlanPercentageVo> seasonPlanPercentage(SeasonPlanPercentageQueryDto dto) {
        return reportService.seasonPlanPercentage(dto);
    }

    @ApiOperation(value = "季节企划完成率报表-导出")
    @GetMapping("/seasonPlanPercentageExport")
    public void seasonPlanPercentageExport(HttpServletResponse response, SeasonPlanPercentageQueryDto dto) throws IOException {
        reportService.seasonPlanPercentageExport(response, dto);
    }

}
