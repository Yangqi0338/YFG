package com.base.sbc.module.style.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.style.vo.StyleAnalyseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StyleAnalyseMapper {

    List<StyleAnalyseVo> findDesignPage(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<StyleAnalyseVo> findStylePage(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
}
