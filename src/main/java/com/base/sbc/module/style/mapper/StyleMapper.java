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
import com.base.sbc.module.patternmaking.vo.PatternMakingForSampleVo;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.sample.vo.StyleUploadVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.vo.ChartBarVo;
import com.base.sbc.module.style.vo.StyleBoardCategorySummaryVo;
import com.base.sbc.module.style.vo.StyleDimensionVO;
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
//@DataIsolation(authority="style")
public interface StyleMapper extends BaseMapper<Style> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    List<StylePageVo> selectByQw(@Param(Constants.WRAPPER) QueryWrapper<Style> wrapper);

    List<SampleUserVo> getDesignerList(@Param("companyCode") String companyCode);

    List<ChartBarVo> getBandChart(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<ChartBarVo> getCategoryChart(@Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 自定义方法区 不替换的区域【other_end】
     **/
    List<PatternMakingForSampleVo> getAllList(String status);

    List<DimensionTotalVo> dimensionTotal(@Param(Constants.WRAPPER) QueryWrapper qw, @Param("fabricsUnderTheDrafts") List<String> fabricsUnderTheDrafts);

    List<PlanningSummaryDetailVo> categoryBandSummary(@Param(Constants.WRAPPER) QueryWrapper qw, @Param("fabricsUnderTheDrafts") List<String> fabricsUnderTheDrafts);

    List<StyleBoardCategorySummaryVo> categorySummary(@Param(Constants.WRAPPER) QueryWrapper qw, @Param("fabricsUnderTheDrafts") List<String> fabricsUnderTheDrafts);

    Long colorCount(@Param(Constants.WRAPPER) QueryWrapper prsQw, @Param("fabricsUnderTheDrafts") List<String> fabricsUnderTheDrafts);
    Long colorCount2(@Param(Constants.WRAPPER) QueryWrapper prsQw, @Param("fabricsUnderTheDrafts") List<String> fabricsUnderTheDrafts);

   Long colorCountStyle(@Param(Constants.WRAPPER) QueryWrapper prsQw, @Param("fabricsUnderTheDrafts") List<String> fabricsUnderTheDrafts);

    String selectMaxDesignNo(@Param(Constants.WRAPPER) QueryWrapper qc);

    String selectMaxOldDesignNo(@Param(Constants.WRAPPER) QueryWrapper qc);

    /**
     * 修改所有引用的设计款号
     *
     * @param oldDesignNo
     * @param newDesignNo
     * @return
     */
    Boolean reviseAllDesignNo(@Param("oldDesignNo") String oldDesignNo, @Param("newDesignNo") String newDesignNo);

    StyleUploadVo getStyleUploadInfo(@Param("styleId") String styleId);

    Long changeDevtType(@Param("styleId") String styleId, @Param("devtType") String devtType, @Param("devtTypeName") String devtTypeName);

    String selectMaxDesignNoYfg(
            @Param("companyCode") String companyCode,
            @Param("brand") String brand,
            @Param("year") String year,
            @Param("category") String category,
            @Param("pxLength") int pxLength,
            @Param("length") int length);

    List<StyleDimensionVO> queryStyleField(@Param(Constants.WRAPPER) QueryWrapper qw);
}

