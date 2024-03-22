package com.base.sbc.module.patternlibrary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import org.apache.ibatis.annotations.Param;

/**
 * 版型库-主表 Mapper 接口
 *
 * @author xhte
 * @create 2024-03-22
 */
public interface PatternLibraryMapper extends BaseMapper<PatternLibrary> {

    /**
     * 版型库列表
     * @param iPage 分页参数
     * @param queryWrapper 筛选条件
     * @param patternLibraryDTO 其他条件
     * @return 分页列表
     */
    IPage<PatternLibraryVO> listPages(
            @Param("iPage") IPage<PatternLibraryVO> iPage,
            @Param("queryWrapper") LambdaQueryWrapper<PatternLibraryVO> queryWrapper,
            @Param("patternLibraryDTO") PatternLibraryDTO patternLibraryDTO
    );

}
