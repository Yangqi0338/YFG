package com.base.sbc.module.report.service.imp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.ureport.minio.MinioUtils;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.report.dto.*;
import com.base.sbc.module.report.mapper.ReportMapper;
import com.base.sbc.module.report.service.ReportService;
import com.base.sbc.module.report.vo.*;
import com.base.sbc.module.style.entity.LatestCommissioningDate;
import com.base.sbc.module.style.service.LatestCommissioningDateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Resource
    private StylePicUtils stylePicUtils;
    @Resource
    private MinioUtils minioUtils;

    @Resource
    private LatestCommissioningDateService commissioningDateService;

    @Override
    public PageInfo<HangTagReportVo> getHangTagReortPage(HangTagReportQueryDto dto) {
        BaseQueryWrapper<HangTagReportQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        List<String> bulkStyleNos = dto.getBulkStyleNos();
        String year = dto.getYear();
        String season = dto.getSeason();
        //QueryGenerator.reportParamBulkStyleNosCheck(bulkStyleNos, year, season);

        boolean yearIsNotBool = StrUtil.isNotEmpty(year) && StrUtil.isEmpty(season);
        boolean seasonIsNotBool = StrUtil.isEmpty(year) && StrUtil.isNotEmpty(season);
        boolean bulkStyleNosEmpty = CollUtil.isEmpty(bulkStyleNos);
        boolean yearEmpty = StrUtil.isEmpty(year);
        boolean seasonEmpty = StrUtil.isEmpty(season);
        if (!bulkStyleNosEmpty && bulkStyleNos.size() > 2000) {
            throw new OtherException("大货款号最多输入2000个！");
        }
        if (bulkStyleNosEmpty && (yearIsNotBool )) {
            throw new OtherException("当大货款号为空的时候,年份必须输入查询！");
        }
        if (bulkStyleNosEmpty && yearEmpty ) {
            throw new OtherException("请输入大货款号或年份参数查询！");
        }

        qw.notEmptyIn("t.bulk_style_no", bulkStyleNos);
        qw.notEmptyEq("ts.year", year);
        qw.notEmptyEq("ts.season", season);
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

        List<String> materialNos = dto.getMaterialNos();
        String year = dto.getYear();
        String season = dto.getSeason();
        //QueryGenerator.reportParamMaterialsNoCheck(materialNos, year, season);
        qw.notEmptyIn("t.material_code", materialNos);
        qw.notEmptyEq("t.year", year);
        qw.notEmptyEq("t.season", season);
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
        List<String> bulkStyleNos = dto.getBulkStyleNos();
        String year = dto.getYear();
        String season = dto.getSeason();
        QueryGenerator.reportParamBulkStyleNosCheck(bulkStyleNos, year, season);
        qw.notEmptyIn("tsc.style_no", bulkStyleNos);
        qw.notEmptyEq("ts.year", year);
        qw.notEmptyEq("ts.season", season);
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
        qw.eq("ts.del_flag", "0");

        List<String> bulkStyleNos = dto.getBulkStyleNos();
        String year = dto.getYear();
        String season = dto.getSeason();
        qw.notEmptyIn("tsc.style_no", bulkStyleNos);
        qw.notEmptyEq("ts.year", year);
        qw.notEmptyEq("ts.season", season);
        QueryGenerator.reportParamBulkStyleNosCheck(bulkStyleNos, year, season);
        qw.orderByDesc("tsc.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<StyleSizeReportVo> list = reportMapper.getStyleSizeReport(qw);
        if (!isColumnHeard) {
            Map<String, String> values = null;
            for (StyleSizeReportVo styleSizeReportVo : list) {
                values = new HashMap<>();
                String standard = styleSizeReportVo.getStandard();
                JSONObject jsonObject = JSONObject.parseObject(standard);
                for (String key : jsonObject.keySet()) {
                    //if (key.contains("\\(")) {
                    String[] split = key.split("\\(");
                    String s = split[1].replace(")", "");
                    if (key.contains("template")) {
                        values.put("template" + s, jsonObject.getString(key));
                    } else if (key.contains("garment")) {
                        values.put("garment" + s, jsonObject.getString(key));
                    } else if (key.contains("washing")) {
                        values.put("washing" + s, jsonObject.getString(key));
                    }
                    /* }else{
                        //FIXME 后续逻辑优化
                    }*/

                }
                styleSizeReportVo.setSizeMap(values);
            }
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
        qw.eq("tsc.del_flag", "0");
        List<String> bulkStyleNosParam = dto.getBulkStyleNos();
        String yearParam = dto.getYear();
        String seasonParam = dto.getSeason();
        qw.notEmptyIn("tsc.style_no", bulkStyleNosParam);
        qw.notEmptyEq("ts.year", yearParam);
        qw.notEmptyEq("ts.season", seasonParam);
        QueryGenerator.reportParamBulkStyleNosCheck(bulkStyleNosParam, yearParam, seasonParam);
        qw.orderByDesc("tsc.create_date");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        PageHelper.startPage(dto);
        List<DesignOrderScheduleDetailsReportVo> list = reportMapper.getDesignOrderScheduleDetailsReport(qw);

        QueryWrapper<LatestCommissioningDate> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("del_flag", 0);
        List<LatestCommissioningDate> latestCommissioningDateList = commissioningDateService.list(objectQueryWrapper);

        //转成Map
        Map<String, LatestCommissioningDate> latestCommissioningDateMap = latestCommissioningDateList.stream().collect(Collectors.toMap(x -> {
            return x.getBrandName() + x.getYear() + x.getBandName();
        }, Function.identity(), (oldValue, newValue) -> newValue));


        for (DesignOrderScheduleDetailsReportVo report : list) {
            String seasonName = report.getSeasonName();
            //订货本投产时间
            Date commissioningDate = report.getCommissioningDate();
            if (commissioningDate == null) {
//                report.setSendMainFabricDay("不延期");
//                report.setDesignDetailDay("不延期");
//                report.setDesignCorrectDay("不延期");
                continue;
            }

            String brandName = report.getBrandName();
            String year = report.getYear();
            String bandName = report.getBandName();

            LatestCommissioningDate latestCommissioningDate = latestCommissioningDateMap.get(brandName + year + bandName);
            if (latestCommissioningDate != null) {
                Date lastDate = null;
                if (latestCommissioningDate == null) {
                    lastDate = new Date();
                } else {
                    lastDate = latestCommissioningDate.getLatestCommissioningDate();
                }

                /**面料详单*/
                Date sendMainFabricDate = report.getSendMainFabricDate();
                if (sendMainFabricDate == null) {
                    sendMainFabricDate = new Date();
                }

                //比较面料详单和投产日期
                //1.如果面料详单日期比投产日期小为不延期
                int fabricCompare = DateUtil.compare(sendMainFabricDate, commissioningDate);
                if (fabricCompare == 1 || fabricCompare == 0) {
                    int compare = DateUtil.compare(lastDate, commissioningDate);
                    if (compare == 1) {
                        long betweenDay = DateUtil.between(sendMainFabricDate, commissioningDate, DateUnit.DAY);
                        if (betweenDay > 2) {
                            report.setSendMainFabricDay((betweenDay - 2) + "");
                        } else {
                            report.setSendMainFabricDay("不延期");
                        }
                    } else {
                        //首单（没超出也是首单）
                        long betweenDay = DateUtil.between(sendMainFabricDate, commissioningDate, DateUnit.DAY);
                        if (betweenDay > 6) {
                            report.setSendMainFabricDay((betweenDay - 6) + "");
                        } else {
                            report.setSendMainFabricDay("不延期");
                        }
                    }
                } else {
                    report.setSendMainFabricDay("不延期");
                }

                /**明细单*/
                Date designDetailDate = report.getDesignDetailDate();
                if (designDetailDate == null) {
                    designDetailDate = new Date();
                }

                int designDetailCompare = DateUtil.compare(designDetailDate, commissioningDate);
                if (designDetailCompare == 1 || designDetailCompare == 0) {
                    int compare = DateUtil.compare(lastDate, commissioningDate);
                    if (compare == 1) {
                        long betweenDay = DateUtil.between(designDetailDate, commissioningDate, DateUnit.DAY);
                        if (betweenDay > 2) {
                            report.setDesignDetailDay((betweenDay - 2) + "");
                        } else {
                            report.setDesignDetailDay("不延期");
                        }
                    } else {
                        long betweenDay = DateUtil.between(designDetailDate, commissioningDate, DateUnit.DAY);
                        if (betweenDay > 6) {
                            report.setDesignDetailDay((betweenDay - 6) + "");
                        } else {
                            report.setDesignDetailDay("不延期");
                        }
                    }
                } else {
                    report.setDesignDetailDay("不延期");
                }

                /**正确样*/
                Date designCorrectDate = report.getDesignCorrectDate();
                if (designCorrectDate == null) {
                    designCorrectDate = new Date();
                }
                int designCorrectCompare = DateUtil.compare(designCorrectDate, commissioningDate);

                if (designCorrectCompare == 1 || designCorrectCompare == 0) {
                    int compare = DateUtil.compare(lastDate, commissioningDate);
                    if (compare == 1) {
                        long betweenDay = DateUtil.between(designCorrectDate, commissioningDate, DateUnit.DAY);
                        if ("春".equals(seasonName)) {
                            if (betweenDay > 12) {
                                report.setDesignCorrectDay((betweenDay - 12) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        } else if ("夏".equals(seasonName)) {
                            if (betweenDay > 12) {
                                report.setDesignCorrectDay((betweenDay - 12) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        } else if ("秋".equals(seasonName)) {
                            if (betweenDay > 16) {
                                report.setDesignCorrectDay((betweenDay - 16) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        } else if ("冬".equals(seasonName)) {
                            if (betweenDay > 20) {
                                report.setDesignCorrectDay((betweenDay - 20) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        }

                    } else {
                        long betweenDay = DateUtil.between(designCorrectDate, commissioningDate, DateUnit.DAY);
                        if ("春".equals(seasonName)) {
                            if (betweenDay > 12) {
                                report.setDesignCorrectDay((betweenDay - 12) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        } else if ("夏".equals(seasonName)) {
                            if (betweenDay > 12) {
                                report.setDesignCorrectDay((betweenDay - 12) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        } else if ("秋".equals(seasonName)) {
                            if (betweenDay > 16) {
                                report.setDesignCorrectDay((betweenDay - 16) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        } else if ("冬".equals(seasonName)) {
                            if (betweenDay > 20) {
                                report.setDesignCorrectDay((betweenDay - 20) + "");
                            } else {
                                report.setDesignCorrectDay("不延期");
                            }
                        }
                    }
                } else {
                    report.setDesignCorrectDay("不延期");
                }
            }
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
    public void designOrderScheduleDetailsExport(HttpServletResponse response, DesignOrderScheduleDetailsQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<DesignOrderScheduleDetailsReportVo> list = getDesignOrderScheduleDetailsReportPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "设计下单进度明细报表", response, dto);
    }
}
