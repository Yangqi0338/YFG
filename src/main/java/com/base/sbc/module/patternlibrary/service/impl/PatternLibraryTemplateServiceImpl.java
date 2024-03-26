package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplatePageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryItem;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplateItem;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 版型库-模板表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryTemplateServiceImpl extends ServiceImpl<PatternLibraryTemplateMapper, PatternLibraryTemplate> implements PatternLibraryTemplateService {

    @Autowired
    private PatternLibraryTemplateItemService patternLibraryTemplateItemService;

    @Override
    public PageInfo<PatternLibraryTemplate> listPages(PatternLibraryTemplatePageDTO patternLibraryTemplatePageDTO) {
        QueryWrapper<PatternLibraryTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper
                // 模板编码
                .like(ObjectUtil.isNotEmpty(patternLibraryTemplatePageDTO.getSearch())
                        , "tplt.code", patternLibraryTemplatePageDTO.getSearch())
                // 模板名称
                .eq(ObjectUtil.isNotEmpty(patternLibraryTemplatePageDTO.getSearch())
                        , "tpl.name", patternLibraryTemplatePageDTO.getSearch());
        // 列表分页
        PageHelper.startPage(patternLibraryTemplatePageDTO.getPageNum(), patternLibraryTemplatePageDTO.getPageSize());
        // 得到版型库主表数据集合
        List<PatternLibraryTemplate> patternLibraryTemplateList = baseMapper.selectList(queryWrapper);
        // 设置版型类型
        if (ObjectUtil.isNotEmpty(patternLibraryTemplateList)) {
            // 拿到模板数据的 ID 集合
            Set<String> patternLibraryTemplateIdSet = patternLibraryTemplateList
                    .stream().map(PatternLibraryTemplate::getId).collect(Collectors.toSet());
            // 根据模板数据的 ID 集合 查询出模板子表的数据
            List<PatternLibraryTemplateItem> patternLibraryTemplateItemList = patternLibraryTemplateItemService.list(
                    new LambdaQueryWrapper<PatternLibraryTemplateItem>()
                            .in(PatternLibraryTemplateItem::getTemplateId, patternLibraryTemplateIdSet)
                            .in(PatternLibraryTemplateItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
            );
            // 按照模板 ID 分组
            Map<String, List<PatternLibraryTemplateItem>> colpatternLibraryTemplateItemMap = Collections.emptyMap();
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateItemList)) {
                colpatternLibraryTemplateItemMap = patternLibraryTemplateItemList
                        .stream().collect(Collectors.groupingBy(PatternLibraryTemplateItem::getTemplateId));
            }
            // 设置模板子表数据
            for (PatternLibraryTemplate patternLibraryTemplate : patternLibraryTemplateList) {
                patternLibraryTemplate.setPatternLibraryTemplateItemList(
                        colpatternLibraryTemplateItemMap.get(patternLibraryTemplate.getId())
                );
            }
        }
        return new PageInfo<>(patternLibraryTemplateList);
    }

    @Override
    public Boolean saveOrUpdateDetails(PatternLibraryTemplate patternLibraryTemplate) {
        return null;
    }

    @Override
    public PatternLibraryVO getDetail(String patternLibraryTemplateId) {
        return null;
    }

    @Override
    public Boolean removeDetail(String patternLibraryTemplateId) {
        return null;
    }

}
