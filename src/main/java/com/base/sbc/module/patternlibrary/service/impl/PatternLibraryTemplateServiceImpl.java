package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplatePageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplateItem;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateDetail(PatternLibraryTemplate patternLibraryTemplate) {
        if (ObjectUtil.isEmpty(patternLibraryTemplate)) {
            throw new OtherException("新增/编辑数据不能为空！");
        }
        List<PatternLibraryTemplateItem> patternLibraryTemplateItemList = patternLibraryTemplate.getPatternLibraryTemplateItemList();
        // 去除名称前后空格
        patternLibraryTemplate.setName(patternLibraryTemplate.getName().trim());
        if (ObjectUtil.isEmpty(patternLibraryTemplate.getId())) {
            // 新增
            // 生成唯一的编码 防止并发重复
            synchronized (this) {
                int soleId = 1;
                // 查询最大的一条编码
                PatternLibraryTemplate template = getOne(
                        new LambdaQueryWrapper<PatternLibraryTemplate>()
                                .orderByDesc(PatternLibraryTemplate::getSoleId)
                                .last("limit 1")
                );
                if (ObjectUtil.isNotEmpty(template)) {
                    soleId = template.getSoleId() + 1;
                }
                patternLibraryTemplate.setCode("BX" + String.format("%03d", soleId));
                patternLibraryTemplate.setSoleId(soleId);
                // 新增主表数据
                save(patternLibraryTemplate);
            }
            // 新增子表数据
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateItemList)) {
                for (PatternLibraryTemplateItem patternLibraryTemplateItem : patternLibraryTemplateItemList) {
                    patternLibraryTemplateItem.setTemplateId(patternLibraryTemplate.getId());
                }
                patternLibraryTemplateItemService.saveBatch(patternLibraryTemplateItemList);
            }
        } else {
            // 编辑
            // 修改主表数据
            updateById(patternLibraryTemplate);
            // 修改子表数据
            if (ObjectUtil.isNotEmpty(patternLibraryTemplateItemList)) {
                List<String> patternLibraryTemplateItemIdList = patternLibraryTemplateItemList
                        .stream().map(PatternLibraryTemplateItem::getId)
                        .filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
                // 先删除此版型库-模板下的数据且不是此 ID 集合下的数据
                patternLibraryTemplateItemService.remove(
                        new LambdaQueryWrapper<PatternLibraryTemplateItem>()
                                .eq(PatternLibraryTemplateItem::getTemplateId, patternLibraryTemplate.getId())
                                .notIn(PatternLibraryTemplateItem::getId, patternLibraryTemplateItemIdList)
                );
                for (PatternLibraryTemplateItem patternLibraryTemplateItem : patternLibraryTemplateItemList) {
                    patternLibraryTemplateItem.setTemplateId(patternLibraryTemplate.getId());
                }
                patternLibraryTemplateItemService.saveOrUpdateBatch(patternLibraryTemplateItemList);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public PatternLibraryTemplate getDetail(String patternLibraryTemplateId) {
        if (ObjectUtil.isEmpty(patternLibraryTemplateId)) {
            throw new OtherException("请选择数据查看！");
        }
        // 根据版型库-模板主表 ID 查询
        PatternLibraryTemplate patternLibraryTemplate = getById(patternLibraryTemplateId);
        if (ObjectUtil.isEmpty(patternLibraryTemplate)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        // 查询版型库模板子表信息
        if (ObjectUtil.isNotEmpty(patternLibraryTemplate)) {
            List<PatternLibraryTemplateItem> patternLibraryTemplateItemList = patternLibraryTemplateItemService.list(
                    new LambdaQueryWrapper<PatternLibraryTemplateItem>()
                            .eq(PatternLibraryTemplateItem::getTemplateId, patternLibraryTemplate.getId())
                            .eq(PatternLibraryTemplateItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
            );
            patternLibraryTemplate.setPatternLibraryTemplateItemList(patternLibraryTemplateItemList);
        }
        return patternLibraryTemplate;
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDetail(String patternLibraryTemplateId) {
        if (ObjectUtil.isEmpty(patternLibraryTemplateId)) {
            throw new OtherException("请选择数据删除！");
        }
        // 根据版型库-模板主表 ID 查询
        PatternLibraryTemplate patternLibraryTemplate = getById(patternLibraryTemplateId);
        if (ObjectUtil.isEmpty(patternLibraryTemplate)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        // 删除版型库主表数据
        removeById(patternLibraryTemplate);
        // 删除版型库-模板子表数据
        patternLibraryTemplateItemService.remove(
                new LambdaQueryWrapper<PatternLibraryTemplateItem>()
                        .eq(PatternLibraryTemplateItem::getTemplateId, patternLibraryTemplate.getId())
                        .eq(PatternLibraryTemplateItem::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL)
        );
        return Boolean.TRUE;
    }

}
