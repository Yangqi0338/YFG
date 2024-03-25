/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.nodestatus.dto.NodestatusPageSearchDto;
import com.base.sbc.module.nodestatus.dto.ResearchProgressPageDto;
import com.base.sbc.module.patternmaking.dto.PatternMakingWeekMonthViewDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.vo.SampleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 类描述：打版管理 dao类
 * @address com.base.sbc.module.patternmaking.dao.PatternMakingDao
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 * @version 1.0
 */
@Mapper
public interface PatternMakingMapper extends BaseMapper<PatternMaking> {
    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    List<TechnologyCenterTaskVo> technologyCenterTaskList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<PatternDesignSampleTypeQtyVo> getPatternDesignSampleTypeCount(@Param(Constants.WRAPPER) QueryWrapper pmQw);

    List<PatternMakingTaskListVo> patternMakingTaskList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SampleBoardVo> sampleBoardList(@Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 导出
     * @param qw
     * @return
     */
    List<SampleBoardExcel> deriveList(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<Map<String, Object>> workPatternMakingSteps(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<SampleUserVo> getAllPatternDesignList(@Param("companyCode") String companyCode);

    List<Map<String, Object>> nsCount(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<PatternMakingForSampleVo> getAllList(@Param(Constants.WRAPPER) QueryWrapper qw);
    List<PatternMakingListVo> findBySampleDesignId(@Param(Constants.WRAPPER) QueryWrapper<PatternMaking> qw);

    /**
     * 根据时间按周月统计版类对比
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @return 返回集合数据
     */
    List<PatternMakingWeekMonthViewVo> versionComparisonViewWeekMonth(@Param("dto")  PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,@Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 根据时间按周月品类汇总统计
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @return 返回集合数据
     */
    List<PatternMakingWeekMonthViewVo> categorySummaryCount(@Param("dto")  PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,
                                                            @Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 根据时间按周月 统计样衣产能总数
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @return 返回集合数据
     */
    List<PatternMakingWeekMonthViewVo> sampleCapacityTotalCount(@Param("dto")  PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,
                                                                @Param(Constants.WRAPPER) QueryWrapper qw);

    /**
     * 根据时间按周月 统计 产能对比 》打版需求
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @return 返回集合数据
     */
    List<PatternMakingWeekMonthViewVo> capacityContrastDemandStatistics(@Param("dto") PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,
                                                                        @Param(Constants.WRAPPER) QueryWrapper qw);


    /**
     * 根据时间按周月 产能对比 》打版产能
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @return 返回集合数据
     */
    List<PatternMakingWeekMonthViewVo> capacityContrastCapacityStatistics(@Param("dto") PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,
                                                                          @Param(Constants.WRAPPER) QueryWrapper qw);


    List<PatternMaking> getPatternMakingSewingStatus(@Param(Constants.WRAPPER) QueryWrapper qw);

    List<NodeListVo> getProgressSteps(NodestatusPageSearchDto dto);

    List<StyleResearchProcessVo> getResearchProcessList(ResearchProgressPageDto dto);

    /**
     *  样衣评分
     * @param qw
     * @return
     */
    PatternMakingScoreVo sampleBoardScore(@Param(Constants.WRAPPER)BaseQueryWrapper<SampleBoardVo> qw);
/** 自定义方法区 不替换的区域【other_end】 **/
}

