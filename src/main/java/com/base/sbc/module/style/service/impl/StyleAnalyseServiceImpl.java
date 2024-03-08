package com.base.sbc.module.style.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.QueryGenerator;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.style.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.style.mapper.StyleAnalyseMapper;
import com.base.sbc.module.style.service.StyleAnalyseService;
import com.base.sbc.module.style.vo.StyleAnalyseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public PageInfo<StyleAnalyseVo> findDesignPage(StyleAnalyseQueryDto dto) {
        BaseQueryWrapper<StyleAnalyseQueryDto> qw = new BaseQueryWrapper<>();
        qw.eq("t.del_flag", "0");
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        Page<Object> objects = PageHelper.startPage(dto);
        List<StyleAnalyseVo> list = styleAnalyseMapper.findDesignPage(qw);

        if(CollUtil.isEmpty(list)){
            return new PageInfo<>(list);
        }

        //根据查询出维度系数数据
        LambdaQueryWrapper<FieldVal> fieldValQueryWrapper = new LambdaQueryWrapper<>();
        List<String> styleIdList = list.stream().map(StyleAnalyseVo::getId).distinct().collect(Collectors.toList());
        fieldValQueryWrapper.in(FieldVal::getForeignId, styleIdList);
        fieldValQueryWrapper.eq(FieldVal::getDataGroup, FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);
        List<FieldVal> fieldValList = fieldValService.list(fieldValQueryWrapper);
        Map<String, Map<String, String>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId, Collectors.toMap(FieldVal::getFieldName, o -> StrUtil.isEmpty(o.getValName()) ? o.getVal() : o.getValName(), (v1, v2) -> v1)));

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
        boolean isColumnHeard = QueryGenerator.initQueryWrapperByMap(qw, dto);
        Page<Object> objects = PageHelper.startPage(dto);
        List<StyleAnalyseVo> list = styleAnalyseMapper.findStylePage(qw);

        //根据查询出维度系数数据
        LambdaQueryWrapper<FieldVal> fieldValQueryWrapper = new LambdaQueryWrapper<>();
        List<String> styleColorIdList = list.stream().map(StyleAnalyseVo::getStyleColorId).distinct().collect(Collectors.toList());
        fieldValQueryWrapper.in(FieldVal::getForeignId, styleColorIdList);
        fieldValQueryWrapper.eq(FieldVal::getDataGroup, FieldValDataGroupConstant.STYLE_MARKING_ORDER);
        List<FieldVal> fieldValList = fieldValService.list(fieldValQueryWrapper);
        Map<String, Map<String, String>> fieldValMap = fieldValList.stream().collect(Collectors.groupingBy(FieldVal::getForeignId, Collectors.toMap(FieldVal::getFieldName, o -> StrUtil.isEmpty(o.getValName()) ? o.getVal() : o.getValName(), (v1, v2) -> v1)));
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
        stylePicUtils.setStyleColorPic2(list, "styleColorPic");
        return new PageInfo<>(list);
    }

}
