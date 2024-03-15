package com.base.sbc.module.report.service.imp;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void hangTagReortExport(HttpServletResponse response, HangTagReportQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<HangTagReportVo> list = getHangTagReortPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "吊牌充绒量报表", response, dto);
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
    public void materialSupplierQuoteExport(HttpServletResponse response, MaterialSupplierQuoteQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<MaterialSupplierQuoteVo> list = getMaterialSupplierQuoteReportPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "材料供应商报价报表", response, dto);
    }

    @Override
    public PageInfo<StylePackBomMaterialReportVo> getStylePackBomMaterialReportPage(StylePackBomMateriaQueryDto dto) {
        BaseQueryWrapper<MaterialSupplierQuoteQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("tsc.del_flag", "0");
        qw.isNotNullStr("tsc.bom");
        qw.notEmptyIn("tsc.style_no" , dto.getStyleColorNos());
        qw.notEmptyEq("ts.year" , dto.getYear());
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
    public void stylePackBomMaterialExport(HttpServletResponse response, StylePackBomMateriaQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<StylePackBomMaterialReportVo> list = getStylePackBomMaterialReportPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "BOM清单明细报表", response, dto);
    }

    @Override
    public PageInfo<StyleSizeReportVo> getStyleSizeReportPage(StyleSizeQueryDto dto) {
        BaseQueryWrapper<StyleSizeReportVo> qw = new BaseQueryWrapper<>();
        qw.eq("ts.del_flag" , "0");
        qw.orderByDesc("tsc.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<StyleSizeReportVo> list = reportMapper.getStyleSizeReport(qw);
        Map<String,String> values = null;
        for (StyleSizeReportVo styleSizeReportVo : list) {
            values =new HashMap<>();
            String standard = styleSizeReportVo.getStandard();
            JSONObject jsonObject = JSONObject.parseObject(standard);
            for (String key : jsonObject.keySet()) {
                String[] split = key.split("\\(");
                String s = split[1].replace(")","");
                if (key.contains("template")) {
                    values.put("template"+s,jsonObject.getString(key));
                }else if(key.contains("garment")){
                    values.put("garment"+s,jsonObject.getString(key));
                }else if(key.contains("washing")){
                    values.put("washing"+s,jsonObject.getString(key));
                }
            }
            styleSizeReportVo.setSizeMap(values);
        }



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
    public void styleSizeExport(HttpServletResponse response, StyleSizeQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<StyleSizeReportVo> list = getStyleSizeReportPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "尺寸报表", response, dto);
    }

    @Override
    public PageInfo<DesignOrderScheduleDetailsReportVo> getDesignOrderScheduleDetailsReportPage(DesignOrderScheduleDetailsQueryDto dto) {
        BaseQueryWrapper<DesignOrderScheduleDetailsReportVo> qw = new BaseQueryWrapper<>();
        qw.eq("tsc.del_flag" , "0");
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
