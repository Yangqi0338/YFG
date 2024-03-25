package com.base.sbc.module.report.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.report.vo.TechnologyCenterBoardCapacityNumberVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardCurrentTaskVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardDesignerRankVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardOverviewDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TechnologyCenterBoardMapper {

    /**
     * 数据总览
     * @return
     */
    TechnologyCenterBoardOverviewDataVo getPlateMakeUnderwayData(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    /**
     * 数据总览
     * @return
     */
    TechnologyCenterBoardCapacityNumberVo getCapacityNumber(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    /**
     * 数据总览
     * @return
     */
    List<TechnologyCenterBoardDesignerRankVo> getDesignerRank(@Param(Constants.WRAPPER) BaseQueryWrapper qw);
    /**
     * 数据总览
     * @return
     */
    List<TechnologyCenterBoardCurrentTaskVo> getCurrentTaskData(@Param(Constants.WRAPPER) BaseQueryWrapper qw);

}
