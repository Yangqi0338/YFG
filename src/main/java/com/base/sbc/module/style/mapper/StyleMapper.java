/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.annotation.DataIsolation;
import com.base.sbc.module.patternmaking.vo.PatternMakingForSampleVo;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.vo.ChartBarVo;
import com.base.sbc.module.style.vo.StyleBoardCategorySummaryVo;
import com.base.sbc.module.style.vo.StylePageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：款式设计 dao类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.sample.dao.SampleDao
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
@Mapper
@DataIsolation(authority="style")
public interface StyleMapper extends BaseMapper<Style> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/
    @DataIsolation(authority="style",authorityFields={"s.prod_category","s.brand"})
    List<StylePageVo> selectByQw(@Param(Constants.WRAPPER) QueryWrapper<Style> wrapper);

    List<SampleUserVo> getDesignerList(@Param("companyCode") String companyCode);

    List<ChartBarVo> getBandChart(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<ChartBarVo> getCategoryChart(@Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 自定义方法区 不替换的区域【other_end】
     **/
    List<PatternMakingForSampleVo> getAllList(String status);

    List<DimensionTotalVo> dimensionTotal(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<PlanningSummaryDetailVo> categoryBandSummary(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<StyleBoardCategorySummaryVo> categorySummary(@Param(Constants.WRAPPER) QueryWrapper qw);

    Long colorCount(@Param(Constants.WRAPPER) QueryWrapper prsQw);

    String selectMaxDesignNo(@Param(Constants.WRAPPER) QueryWrapper qc);
}

