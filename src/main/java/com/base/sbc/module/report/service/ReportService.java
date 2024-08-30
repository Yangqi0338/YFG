package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.vo.*;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ReportService {

    /**
     * 吊牌充绒量报表
     * @return
     */
    PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto);

    /**
     * 吊牌充绒量报表-导出
     */
    void hangTagReortExport(HttpServletResponse response, HangTagReportQueryDto dto) throws IOException;

    /**
     * 材料供应商报价报表
     * @return
     */
    PageInfo<MaterialSupplierQuoteVo> getMaterialSupplierQuoteReportPage(MaterialSupplierQuoteQueryDto dto);

    /**
     * 材料供应商报价报表-导出
     */
    void materialSupplierQuoteExport(HttpServletResponse response, MaterialSupplierQuoteQueryDto dto) throws IOException;

    /**
     * BOM清单明细报表
     * @return
     */
    PageInfo<StylePackBomMaterialReportVo> getStylePackBomMaterialReportPage(StylePackBomMateriaQueryDto dto);

    /**
     * BOM清单明细报表-导出
     */
    void stylePackBomMaterialExport(HttpServletResponse response, StylePackBomMateriaQueryDto dto) throws IOException;
    /**
     * 尺寸报表
     * @return
     */
    PageInfo<StyleSizeReportVo> getStyleSizeReportPage(StyleSizeQueryDto dto);

    /**
     * 尺寸报表-导出
     */
    void styleSizeExport(HttpServletResponse response, StyleSizeQueryDto dto) throws IOException;
    /**
     * 设计下单进度明细报表
     * @return
     */
    PageInfo<DesignOrderScheduleDetailsReportVo> getDesignOrderScheduleDetailsReportPage(DesignOrderScheduleDetailsQueryDto dto);

    /**
     * 设计下单进度明细报表-导出
     */
    void designOrderScheduleDetailsExport(HttpServletResponse response, DesignOrderScheduleDetailsQueryDto dto) throws IOException;


    PageInfo<SeasonPlanPercentageVo> seasonPlanPercentage(SeasonPlanPercentageQueryDto dto);

    void seasonPlanPercentageExport(HttpServletResponse response, SeasonPlanPercentageQueryDto dto) throws IOException;

    List<PatternMakingReportVo> patternMaking(PatternMakingQueryDto dto);

    void patternMakingExport(HttpServletResponse response, PatternMakingQueryDto dto) throws IOException;
}
