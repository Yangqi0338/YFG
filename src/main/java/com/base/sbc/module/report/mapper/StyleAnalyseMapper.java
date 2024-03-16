package com.base.sbc.module.report.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.report.dto.StyleAnalyseQueryDto;
import com.base.sbc.module.report.vo.StyleAnalyseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StyleAnalyseMapper {

    List<StyleAnalyseVo> findDesignPage(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<StyleAnalyseVo> findStylePage(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<StyleAnalyseVo> findPageField(@Param(Constants.WRAPPER) BaseQueryWrapper<StyleAnalyseQueryDto> qw, @Param("dataGroup") String dataGroup, @Param("fieldName") String fieldName);
}
