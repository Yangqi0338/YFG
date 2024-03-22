package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 版型库-主表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryServiceImpl extends ServiceImpl<PatternLibraryMapper, PatternLibrary> implements PatternLibraryService {

    @Autowired
    private DataPermissionsService dataPermissionsService;

    @Autowired
    private PatternLibraryItemService patternLibraryItemService;

    @Autowired
    private PatternLibraryTemplateService patternLibraryTemplateService;

    @Override
    public PageInfo<PatternLibraryVO> listPages(PatternLibraryDTO patternLibraryDTO) {
        QueryWrapper<PatternLibraryVO> queryWrapper = new QueryWrapper<>();
        queryWrapper
                // 版型编码
                .like(ObjectUtil.isNotEmpty(patternLibraryDTO.getCode())
                        , "tpl.code", patternLibraryDTO.getCode())
                // 大类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory1st())
                        , "tpl.prod_category1st", patternLibraryDTO.getProdCategory1st())
                // 品类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory())
                        , "tpl.prod_category", patternLibraryDTO.getProdCategory())
                // 中类编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getProdCategory2nd())
                        , "tpl.prod_category2nd", patternLibraryDTO.getProdCategory2nd())
                // 廓形编码
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getSilhouetteCode())
                        , "tpl.silhouette_code", patternLibraryDTO.getSilhouetteCode())
                // 所属版型库
                .in(ObjectUtil.isNotEmpty(patternLibraryDTO.getTemplateCodeList())
                        , "tpl.template_code", patternLibraryDTO.getTemplateCodeList())
                // 状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getStatus())
                        , "tpl.status", patternLibraryDTO.getStatus())
                // 启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryDTO.getEnableFlag())
                        , "tpl.enable_flag", patternLibraryDTO.getEnableFlag());
        // 权限设置
        dataPermissionsService.getDataPermissionsForQw(queryWrapper, DataPermissionsBusinessTypeEnum.PATTERN_LIBRARY.getK(), "tpl");
        // 列表分页
        PageHelper.startPage(patternLibraryDTO.getPageNum(), patternLibraryDTO.getPageSize());
        // 得到部件库主表数据集合
        List<PatternLibraryVO> patternLibraryVOList = baseMapper.listPages(queryWrapper, patternLibraryDTO);
        // 设置子表数据
        if (ObjectUtil.isNotEmpty(patternLibraryVOList)) {
            // 拿到分页后的主表 ID
            List<String> patterLibraryIdList = patternLibraryVOList
                    .stream().map(PatternLibraryVO::getId).collect(Collectors.toList());
            // 根据主表 ID 查询子表数据
            List<PatternLibraryItem> patternLibraryItemList = patternLibraryItemService.list(
                    new LambdaQueryWrapper<PatternLibraryItem>()
                            .in(PatternLibraryItem::getPatternLibraryId, patterLibraryIdList)
            );
            // 子表根据主表 ID 分组转成 map
            Map<String, List<PatternLibraryItem>> patternLibraryItemMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryItemList)) {
                patternLibraryItemMap = patternLibraryItemList
                        .stream()
                        .collect(Collectors.groupingBy(PatternLibraryItem::getPatternLibraryId));
            }

            // 拿到分页后的模板 code 集合
            Set<String> templateCodeList = patternLibraryVOList
                    .stream().map(PatternLibraryVO::getTemplateCode).collect(Collectors.toSet());
            // 根据模板 code 集合查询模板数据
            List<PatternLibraryTemplate> patternLibraryTemplateList = patternLibraryTemplateService.list(
                    new LambdaQueryWrapper<PatternLibraryTemplate>()
                            .in(PatternLibraryTemplate::getCode, templateCodeList)
            );
            // 模板数据根据模板 code 转成 map
            Map<String, PatternLibraryTemplate> patternLibraryTemplateMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
                patternLibraryTemplateMap = patternLibraryTemplateList
                        .stream().collect(Collectors.toMap(PatternLibraryTemplate::getCode, item -> item));
            }

            for (PatternLibraryVO patternLibraryVO : patternLibraryVOList) {
                // 设置所属版型库数据
                patternLibraryVO.setPatternLibraryTemplate(
                        patternLibraryTemplateMap.get(patternLibraryVO.getTemplateCode())
                );
                // 设置子表数据
                patternLibraryVO.setPatternLibraryItemList(patternLibraryItemMap.get(patternLibraryVO.getId()));
            }
        }
        return new PageInfo<>(patternLibraryVOList);
    }

}
