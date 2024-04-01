package com.base.sbc.module.patternlibrary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.patternlibrary.dto.AuditsDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import com.base.sbc.module.style.entity.Style;
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
     * @param patternLibraryPageDTO 查询条件
     * @return 版型库列表分页数据
     */
    PageInfo<PatternLibraryVO> listPages(PatternLibraryPageDTO patternLibraryPageDTO);

    /**
     * 版型库新增/编辑
     *
     * @param patternLibraryDTO 新增/编辑数据
     * @return 新增/编辑是否成功
     */
    Boolean saveOrUpdateDetails(PatternLibraryDTO patternLibraryDTO);

    /**
     * 版型库批量编辑
     *
     * @param patternLibraryDTOList 编辑数据
     * @return 编辑是否成功
     */
    Boolean updateDetails(List<PatternLibraryDTO> patternLibraryDTOList);

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
     * 版型库批量审核通过
     *
     * @param auditsDTO 版型库主表 ID 集合
     * @return 审核是否成功
     */
    Boolean updateAuditsPass(AuditsDTO auditsDTO);

    /**
     * 版型库批量审核驳回
     *
     * @param auditsDTO 版型库主表 ID 集合
     * @return 审核是否成功
     */
    Boolean updateAuditsReject(AuditsDTO auditsDTO);

    /**
     * 查询已开款的设计款号数据信息
     *
     * @return 已开款后的设计款号数据信息
     */
    List<Style> listStyle(String search);

    /**
     * 根据设计款号查询相关数据
     *
     * @param designNo 设计款号
     * @return 相关数据
     */
    PatternLibraryVO getInfoByDesignNo(String designNo);

    /**
     * 判断大类的类型
     *
     * @return 大类类型 map
     */
    CategoriesTypeVO getCategoriesType();

}
