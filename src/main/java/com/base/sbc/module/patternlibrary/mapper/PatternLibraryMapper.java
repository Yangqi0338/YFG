package com.base.sbc.module.patternlibrary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.patternlibrary.dto.PatternLibraryDTO;
import com.base.sbc.module.patternlibrary.entity.PatternLibrary;
import com.base.sbc.module.patternlibrary.vo.PatternLibraryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
     * @param patternLibraryDTO 其他条件
     * @return 分页列表
     */
    List<PatternLibraryVO> listPages(
            @Param(Constants.WRAPPER) QueryWrapper<PatternLibraryVO> queryWrapper,
            @Param("patternLibraryDTO") PatternLibraryDTO patternLibraryDTO
    );

}
