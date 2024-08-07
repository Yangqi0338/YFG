package com.base.sbc.module.style.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.ExcelUtils;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.report.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.report.mapper.StyleAnalyseMapper;
import com.base.sbc.module.report.service.StyleAnalyseService;
import com.base.sbc.module.report.vo.StyleAnalyseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleAnalyseServiceImpl implements StyleAnalyseService {

    @Autowired
    private StyleAnalyseMapper styleAnalyseMapper;
    @Autowired
    private StylePicUtils stylePicUtils;
    @Autowired
    private FieldValService fieldValService;
    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Override
    public PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto) {
        BaseQueryWrapper<StyleAnalyseQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        if (isColumnHeard && StrUtil.isNotEmpty(dto.getQueryFieldColumn())) {
            String s = dto.getQueryFieldColumn().split("\\.")[1];
            qw = new BaseQueryWrapper<>();
            if (StrUtil.isNotEmpty(dto.getColumnHeard()) && dto.getQueryFieldColumn().equals(dto.getColumnHeard())) {
                qw.like("tfv.val_name", dto.getFieldQueryMap().get(dto.getQueryFieldColumn()));
            }
            // 数据权限
            dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.styleAnalyseDesign.getK());
            List<StyleAnalyseVo> list = styleAnalyseMapper.findPageField(qw, FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY, s);

            for (StyleAnalyseVo styleAnalyseVo : list) {
                Map<String, String> dynamicColumn = new HashMap<>();
                dynamicColumn.put(s, styleAnalyseVo.getValName());
                styleAnalyseVo.setDynamicColumn(dynamicColumn);
            }
            return new PageInfo<>(list);
        }

        List<String> designNos = dto.getDesignNoAndStyleNos();
        String yearParam = dto.getYear();
        String seasonParam = dto.getSeason();
        qw.notEmptyIn("t.design_no", designNos);
        qw.notEmptyEq("t.year", yearParam);
        qw.notEmptyEq("t.season", seasonParam);
        QueryGenerator.reportParamBulkStyleNosCheck(designNos, yearParam, seasonParam);

        Page<Object> objects = PageHelper.startPage(dto);
        List<StyleAnalyseVo> list = styleAnalyseMapper.findDesignPage(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }

        //根据查询出维度系数数据
        LambdaQueryWrapper<FieldVal> fieldValQueryWrapper = new LambdaQueryWrapper<>();
        List<String> styleIdList = list.stream().map(StyleAnalyseVo::getId).distinct().collect(Collectors.toList());
        fieldValQueryWrapper.in(FieldVal::getForeignId, styleIdList);
        fieldValQueryWrapper.eq(FieldVal::getDataGroup, FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        List<FieldVal> fieldValList = fieldValService.list(fieldValQueryWrapper);
        Map<String, Map<String, String>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId, Collectors.toMap(FieldVal::getFieldName, o -> StrUtil.isEmpty(o.getValName()) ? StrUtil.isEmpty(o.getVal()) ? "" : o.getVal() : o.getValName(), (v1, v2) -> v1)));
        for (StyleAnalyseVo styleAnalyseVo : list) {
            if (fieldValMap.containsKey(styleAnalyseVo.getId())) {
                Map<String, String> map = fieldValMap.get(styleAnalyseVo.getId());
                styleAnalyseVo.setDynamicColumn(map);
            }
        }

        if (isColumnHeard) {
            return new PageInfo<>(list);
        }

        /*查询款式图*/
        stylePicUtils.setStylePic(list, "stylePic");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<StyleAnalyseVo> findStylePage(StyleAnalyseQueryDto dto) {
        BaseQueryWrapper<StyleAnalyseQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        List<String> designNos = dto.getDesignNoAndStyleNos();
        String yearParam = dto.getYear();
        String seasonParam = dto.getSeason();
        qw.notEmptyIn("sc.style_no", designNos);
        qw.notEmptyEq("t.year", yearParam);
        qw.notEmptyEq("t.season", seasonParam);
        qw.notIn("sc.defective_no", Arrays.asList("-9","-10","-11","-ZC"));
        QueryGenerator.reportParamBulkStyleNosCheck(designNos, yearParam, seasonParam);
        // 数据权限
        dataPermissionsService.getDataPermissionsForQw(qw, DataPermissionsBusinessTypeEnum.styleAnalyseStyle.getK());
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        if (isColumnHeard && StrUtil.isNotEmpty(dto.getQueryFieldColumn())) {
            String s = dto.getQueryFieldColumn().split("\\.")[1];
            qw = new BaseQueryWrapper<>();
            if (StrUtil.isNotEmpty(dto.getColumnHeard()) && dto.getQueryFieldColumn().equals(dto.getColumnHeard())) {
                qw.like("tfv.val_name", dto.getFieldQueryMap().get(dto.getQueryFieldColumn()));
            }
            List<StyleAnalyseVo> list = styleAnalyseMapper.findPageField(qw, FieldValDataGroupConstant.STYLE_MARKING_ORDER, s);

            for (StyleAnalyseVo styleAnalyseVo : list) {
                Map<String, String> dynamicColumn = new HashMap<>();
                dynamicColumn.put(s, styleAnalyseVo.getValName());
                styleAnalyseVo.setDynamicColumn(dynamicColumn);
            }
            return new PageInfo<>(list);
        }

        Page<Object> objects = PageHelper.startPage(dto);
        List<StyleAnalyseVo> list = styleAnalyseMapper.findStylePage(qw);

        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>(list);
        }

        //根据查询出维度系数数据
        LambdaQueryWrapper<FieldVal> fieldValQueryWrapper = new LambdaQueryWrapper<>();
        List<String> styleColorIdList = list.stream().map(StyleAnalyseVo::getStyleColorId).distinct().collect(Collectors.toList());
        fieldValQueryWrapper.in(FieldVal::getForeignId, styleColorIdList);
        fieldValQueryWrapper.eq(FieldVal::getDataGroup, FieldValDataGroupConstant.STYLE_MARKING_ORDER);
        List<FieldVal> fieldValList = fieldValService.list(fieldValQueryWrapper);
        Map<String, Map<String, String>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId, Collectors.toMap(FieldVal::getFieldName, o -> StrUtil.isEmpty(o.getValName()) ? StrUtil.isEmpty(o.getVal()) ? "" : o.getVal() : o.getValName(), (v1, v2) -> v1)));
        for (StyleAnalyseVo styleAnalyseVo : list) {
            if (fieldValMap.containsKey(styleAnalyseVo.getStyleColorId())) {
                Map<String, String> map = fieldValMap.get(styleAnalyseVo.getStyleColorId());
                styleAnalyseVo.setDynamicColumn(map);
            }
        }

        if (isColumnHeard) {
            return new PageInfo<>(list);
        }

        /*查询款式图*/
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

    @Override
    public void findDesignPageExport(HttpServletResponse response, StyleAnalyseQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<StyleAnalyseVo> list = findDesignPage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "设计分析报表", response, dto);
    }

    @Override
    public void findStylePageExport(HttpServletResponse response, StyleAnalyseQueryDto dto) throws IOException {
        dto.setPageNum(0);
        dto.setPageSize(0);
        List<StyleAnalyseVo> list = findStylePage(dto).getList();
        ExcelUtils.exportExcelByTableCode(list, "大货分析报表", response, dto);
    }

}
