/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.common.vo.CountVo;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSeasonOverviewVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.sample.vo.ChartBarVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 类描述：企划-坑位信息 dao类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.dao.PlanningCategoryItemDao
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 */

@Mapper
public interface PlanningCategoryItemMapper extends BaseMapper<PlanningCategoryItem> {

    String selectMaxDesignNo(@Param(Constants.WRAPPER) QueryWrapper qc);


    List<String> selectCategoryIdsByBand(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<PlanningSeasonOverviewVo> listSeat(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SampleUserVo> getAllDesigner(@Param("companyCode") String userCompany);

    List<DimensionTotalVo> dimensionTotal(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<PlanningSummaryDetailVo> planningSummaryDetail(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<ChartBarVo> categorySummary(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<Map<String, Long>> totalSkcByPlanningSeason(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<Map<String, Long>> totalSkcByChannel(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<CountVo> countCategoryByChannelId(@Param(Constants.WRAPPER) QueryWrapper qw);
}
