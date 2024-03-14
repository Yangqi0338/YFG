package com.base.sbc.module.report.service.imp;

import cn.hutool.core.collection.CollUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.mapper.ReportMapper;
import com.base.sbc.module.report.service.ReportService;
import com.base.sbc.module.report.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Resource
    private StylePicUtils stylePicUtils;
    @Resource
    private MinioUtils minioUtils;

    @Override
    public PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto) {
        BaseQueryWrapper<HangTagReportQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        qw.notEmptyIn("t.bulk_style_no" , dto.getStyleColorNos());
        qw.orderByDesc("t.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<HangTagReportVo> list = reportMapper.getHangTagReortList(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        /*查询款式图*/
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");

        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<MaterialSupplierQuoteVo> getMaterialSupplierQuoteReportPage(MaterialSupplierQuoteQueryDto dto) {
        BaseQueryWrapper<MaterialSupplierQuoteQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        qw.orderByDesc("t.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<MaterialSupplierQuoteVo> list = reportMapper.getMaterialSupplierQuoteReporList(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        minioUtils.setObjectUrlToList(list, "imageUrl");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<StylePackBomMaterialReportVo> getStylePackBomMaterialReportPage(StylePackBomMateriaQueryDto dto) {
        BaseQueryWrapper<MaterialSupplierQuoteQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("tsc.del_flag", "0");
        qw.notEmptyIn("tsc.style_no" , dto.getStyleColorNos());
        qw.orderByDesc("tsc.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<StylePackBomMaterialReportVo> list = reportMapper.getStylePackBomListReport(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        minioUtils.setObjectUrlToList(list, "imageUrl");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<StyleSizeReportVo> getStyleSizeReportPage(StyleSizeQueryDto dto) {
        BaseQueryWrapper<StyleSizeReportVo> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag" , "0");
        qw.eq("tpsd.pack_type" , "packBigGoods");
        qw.notEmptyIn("tsc.style_no" , dto.getStyleColorNos());
        qw.orderByDesc("tsc.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<StyleSizeReportVo> list = reportMapper.getStyleSizeReport(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<DesignOrderScheduleDetailsReportVo> getDesignOrderScheduleDetailsReportPage(DesignOrderScheduleDetailsQueryDto dto) {
        BaseQueryWrapper<StyleSizeReportVo> qw = new BaseQueryWrapper<>();
        qw.eq("ts.del_flag" , "0");
        qw.eq("tsc.del_flag" , "0");
        qw.notEmptyIn("tsc.style_no" , dto.getStyleColorNos());
        qw.orderByDesc("tsc.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<DesignOrderScheduleDetailsReportVo> list = reportMapper.getDesignOrderScheduleDetailsReport(qw);
        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }
        if (isColumnHeard) {
            return new PageInfo<>(list);
        }
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    public void designOrderScheduleDetailsExport(HttpServletResponse response, DesignOrderScheduleDetailsQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<DesignOrderScheduleDetailsReportVo> list = getDesignOrderScheduleDetailsReportPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "设计下单进度明细报表", response, dto);
    }
}
