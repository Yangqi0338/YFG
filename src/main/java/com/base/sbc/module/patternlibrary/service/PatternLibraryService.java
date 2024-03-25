package com.base.sbc.module.patternlibrary.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 版型库-主表 服务类
 *
 * @author xhte
 * @create 2024-03-22
 */
public interface PatternLibraryService extends IService<PatternLibrary> {

    /**
     * 版型库列表
     *
     * @param patternLibraryDTO 查询条件
     * @return 版型库列表分页数据
     */
    PageInfo<PatternLibraryVO> listPages(PatternLibraryDTO patternLibraryDTO);

    /**
     * 版型库详情
     *
     * @param patternLibraryId 版型库主表 ID
     * @return 版型库详情
     */
    PatternLibraryVO getDetail(String patternLibraryId);

    /**
     * 版型库删除
     *
     * @param patternLibraryId 版型库主表 ID
     * @return 删除是否成功
     */
    Boolean removeDetail(String patternLibraryId);

    /**
     * 版型库批量审核
     *
     * @param patternLibraryIdList 版型库主表 ID 集合
     * @return 审核是否成功
     */
    Boolean updateAudits(List<String> patternLibraryIdList);

}
