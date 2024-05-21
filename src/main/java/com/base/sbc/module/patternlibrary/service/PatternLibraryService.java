package com.base.sbc.module.patternlibrary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.base.sbc.module.patternlibrary.dto.AuditsDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.dto.UseStyleDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.vo.CategoriesTypeVO;
import com.base.sbc.module.patternlibrary.vo.FilterCriteriaVO;
import com.base.sbc.module.patternlibrary.vo.UseStyleVO;
import com.base.sbc.module.style.entity.Style;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    PageInfo<PatternLibrary> listPages(PatternLibraryPageDTO patternLibraryPageDTO);

    /**
     * 版型库新增/编辑
     *
     * @param patternLibraryDTO 新增/编辑数据
     * @return 新增/编辑是否成功
     */
    Boolean saveOrUpdateDetails(PatternLibraryDTO patternLibraryDTO);

    /**
     * 版型库子表新增/编辑
     *
     * @param patternLibraryDTO 新增/编辑数据
     * @return 新增/编辑是否成功
     */
    Boolean saveOrUpdateItemDetail(PatternLibraryDTO patternLibraryDTO);

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
    PatternLibrary getDetail(String patternLibraryId);

    /**
     * 根据版型库 id 查询使用款记录列表
     *
     * @param useStyleDTO 查询条件
     * @return 使用款记录列表
     */
    List<UseStyleVO> listUseStyle(UseStyleDTO useStyleDTO);

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
     * @param auditsDTO 版型库主表 ID 集合
     * @return 审核是否成功
     */
    Boolean updateAudits(AuditsDTO auditsDTO);

    /**
     * 版型库启用/禁用
     *
     * @param patternLibraryDTO 入参
     * @return 启用/禁用是否成功
     */
    Boolean updateEnableFlag(PatternLibraryDTO patternLibraryDTO);

    /**
     * 版型库导入
     *
     * @param file Excel 文件
     * @return 导入是否成功
     */
    Boolean excelImport(MultipartFile file);

    /**
     * 版型库
     *
     * @param patternLibraryPageDTO 查询条件
     * @return 导出是否成功
     */
    Boolean excelExport(PatternLibraryPageDTO patternLibraryPageDTO, HttpServletResponse response);

    /**
     * 查询已开款的设计款号数据信息
     *
     * @return 已开款后的设计款号数据信息
     */
    List<Style> listStyle(String search, List<String> styleNoList);

    /**
     * 根据设计款号查询相关数据
     *
     * @param designNo 设计款号
     * @return 相关数据
     */
    PatternLibrary getInfoByDesignNo(String designNo);

    /**
     * 判断大类的类型
     *
     * @return 大类类型 map
     */
    CategoriesTypeVO getCategoriesType();

    /**
     * 获取所有的筛选条件
     *
     * @param type 筛选条件类型（1-版型编码 2-品牌 3-所属品类 4-廓形 5-所属版型库 6-涉及部件 7-审核状态 8-是否启用）
     * @return 筛选条件数据集合
     */
    List<FilterCriteriaVO> getAllFilterCriteria(Integer type);

}
