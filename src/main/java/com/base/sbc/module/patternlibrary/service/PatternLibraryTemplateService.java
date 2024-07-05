package com.base.sbc.module.patternlibrary.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplateDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryTemplatePageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibraryTemplate;
import com.github.pagehelper.PageInfo;

/**
 * 版型库-模板表 服务类
 *
 * @author xhte
 * @create 2024-03-22
 */
public interface PatternLibraryTemplateService extends BaseService<PatternLibraryTemplate> {

    /**
     * 版型库-模板表列表
     *
     * @param patternLibraryTemplatePageDTO 查询条件
     * @return 版型库-模板表列表分页数据
     */
    PageInfo<PatternLibraryTemplate> listPages(PatternLibraryTemplatePageDTO patternLibraryTemplatePageDTO);

    /**
     * 版型库-模板新增/编辑
     *
     * @param patternLibraryTemplate 新增/编辑数据
     * @return 新增/编辑是否成功
     */
    Boolean saveOrUpdateDetail(PatternLibraryTemplate patternLibraryTemplate);


    /**
     * 版型库-模板详情
     *
     * @param patternLibraryTemplateId 版型库主表 ID
     * @return 版型库-模板详情
     */
    PatternLibraryTemplate getDetail(String patternLibraryTemplateId);

    /**
     * 版型库-模板删除
     *
     * @param patternLibraryTemplateId 版型库-模板 ID
     * @return 删除是否成功
     */
    Boolean removeDetail(String patternLibraryTemplateId);

    /**
     * 版型库-模板启用/禁用
     *
     * @param patternLibraryTemplateDTO 入参
     * @return 启用/禁用是否成功
     */
    Boolean updateEnableFlag(PatternLibraryTemplateDTO patternLibraryTemplateDTO);

}
