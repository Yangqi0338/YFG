package com.base.sbc.module.patternlibrary.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplateDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplatePageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.base.sbc.module.patternlibrary.mapper.PatternLibraryTemplateMapper;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateItemService;
import com.base.sbc.module.patternlibrary.service.PatternLibraryTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 版型库-模板表 服务实现类
 *
 * @author xhte
 * @create 2024-03-22
 */
@Service
public class PatternLibraryTemplateServiceImpl extends ServiceImpl<PatternLibraryTemplateMapper, PatternLibraryTemplate> implements PatternLibraryTemplateService {

    @Override
    public PageInfo<PatternLibraryTemplate> listPages(PatternLibraryTemplatePageDTO patternLibraryTemplatePageDTO) {
        QueryWrapper<PatternLibraryTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper
                // 模板编码
                .like(ObjectUtil.isNotEmpty(patternLibraryTemplatePageDTO.getSearch())
                        , "code", patternLibraryTemplatePageDTO.getSearch())
                // 模板名称
                .eq(ObjectUtil.isNotEmpty(patternLibraryTemplatePageDTO.getSearch())
                        , "name", patternLibraryTemplatePageDTO.getSearch())
                // 模板启用状态
                .eq(ObjectUtil.isNotEmpty(patternLibraryTemplatePageDTO.getEnableFlag())
                        , "enable_flag", patternLibraryTemplatePageDTO.getEnableFlag());
        // 列表分页
        PageHelper.startPage(patternLibraryTemplatePageDTO.getPageNum(), patternLibraryTemplatePageDTO.getPageSize());
        // 得到版型库主表数据集合
        List<PatternLibraryTemplate> patternLibraryTemplateList = baseMapper.selectList(queryWrapper);
        return new PageInfo<>(patternLibraryTemplateList);
    }

    @Override
    @DuplicationCheck
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateDetail(PatternLibraryTemplate patternLibraryTemplate) {
        if (ObjectUtil.isEmpty(patternLibraryTemplate)) {
            throw new OtherException("新增/编辑数据不能为空！");
        }
        // 去除名称前后空格
        patternLibraryTemplate.setName(patternLibraryTemplate.getName().trim());
        if (ObjectUtil.isEmpty(patternLibraryTemplate.getId())) {
            // 新增
            // 生成唯一的编码
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
        } else {
            // 编辑
            // 修改主表数据
            updateById(patternLibraryTemplate);
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
        return Boolean.TRUE;
    }

    /**
     * @param patternLibraryTemplateDTO 入参
     * @return
     */
    @Override
    public Boolean updateEnableFlag(PatternLibraryTemplateDTO patternLibraryTemplateDTO) {
        if (ObjectUtil.isEmpty(patternLibraryTemplateDTO.getId())) {
            throw new OtherException("请选择数据启用/禁用！");
        }
        PatternLibraryTemplate patternLibraryTemplate = getById(patternLibraryTemplateDTO.getId());
        if (ObjectUtil.isEmpty(patternLibraryTemplate)) {
            throw new OtherException("当前数据不存在，请刷新后重试！");
        }
        patternLibraryTemplate.setEnableFlag(patternLibraryTemplateDTO.getEnableFlag());
        updateById(patternLibraryTemplate);
        return true;
    }

}
