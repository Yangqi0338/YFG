package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.vo.*;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportService {

    /**
     * 吊牌充绒量报表
     * @return
     */
    PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto);
    /**
     * 材料供应商报价报表
     * @return
     */
    PageInfo<MaterialSupplierQuoteVo> getMaterialSupplierQuoteReportPage(MaterialSupplierQuoteQueryDto dto);
    /**
     * 材料供应商报价报表
     * @return
     */
    PageInfo<StylePackBomMaterialReportVo> getStylePackBomMaterialReportPage(StylePackBomMateriaQueryDto dto);
    /**
     * 尺寸报表
     * @return
     */
    PageInfo<StyleSizeReportVo> getStyleSizeReportPage(StyleSizeQueryDto dto);
    /**
     * 设计下单进度明细报表
     * @return
     */
    PageInfo<DesignOrderScheduleDetailsReportVo> getDesignOrderScheduleDetailsReportPage(DesignOrderScheduleDetailsQueryDto dto);

    /**
     * 设计下单进度明细报表导出
     */
    void designOrderScheduleDetailsExport(HttpServletResponse response, DesignOrderScheduleDetailsQueryDto dto) throws IOException;





}
