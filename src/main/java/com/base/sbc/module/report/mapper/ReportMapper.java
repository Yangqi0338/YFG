/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.report.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.report.dto.PatternMakingQueryDto;
import com.base.sbc.module.report.dto.SeasonPlanPercentageQueryDto;
import com.base.sbc.module.report.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper{

    List<HangTagReportVo> getHangTagReortList(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    List<MaterialSupplierQuoteVo> getMaterialSupplierQuoteReporList(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    List<StylePackBomMaterialReportVo> getStylePackBomListReport(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    List<StyleSizeReportVo> getStyleSizeReport(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    List<DesignOrderScheduleDetailsReportVo> getDesignOrderScheduleDetailsReport(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

    List<SeasonPlanPercentageVo> seasonPlanPercentage(@Param(Constants.WRAPPER) BaseQueryWrapper<SeasonPlanPercentageQueryDto> qw);

    List<PatternMakingReportVo> patternMaking(@Param(Constants.WRAPPER) BaseQueryWrapper<PatternMakingQueryDto> qw);
}

