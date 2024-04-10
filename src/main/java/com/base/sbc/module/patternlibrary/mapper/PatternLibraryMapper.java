package com.base.sbc.module.patternlibrary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryPageDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.vo.FilterCriteriaVO;
import com.base.sbc.module.style.entity.Style;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 版型库-主表 Mapper 接口
 *
 * @author xhte
 * @create 2024-03-22
 */
@Mapper
public interface PatternLibraryMapper extends BaseMapper<PatternLibrary> {

    /**
     * 版型库列表
     * @param queryWrapper 筛选条件
     * @param patternLibraryPageDTO 其他条件
     * @return 分页列表
     */
    List<PatternLibrary> listPages(
            @Param(Constants.WRAPPER) QueryWrapper<PatternLibrary> queryWrapper,
            @Param("patternLibraryPageDTO") PatternLibraryPageDTO patternLibraryPageDTO
    );

    /**
     * 版型库筛选条件查询
     * @param queryWrapper 筛选条件
     * @param type 筛选条件类型（1-版型编码 2-品牌 3-所属品类 4-廓形 5-所属版型库 6-设计部件 7-审核状态 8-是否启用）
     * @return 分页列表
     */
    List<FilterCriteriaVO> getAllFilterCriteria(
            @Param(Constants.WRAPPER) QueryWrapper<PatternLibrary> queryWrapper,
            @Param("type") Integer type

    );

    /**
     * 查询已开款的设计款号数据信息
     * @param queryWrapper 筛选条件
     * @return 款号列表
     */
    List<Style> listStyle(
            @Param(Constants.WRAPPER) QueryWrapper<Style> queryWrapper
    );

}
