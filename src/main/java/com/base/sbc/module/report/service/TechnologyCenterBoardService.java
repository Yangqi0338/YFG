package com.base.sbc.module.report.service;

import com.base.sbc.module.report.dto.TechnologyCenterBoardDto;
import com.base.sbc.module.report.vo.TechnologyCenterBoardCurrentTaskVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardDesignerRankVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardOverviewDataVo;

import java.util.List;

/**
 * 技术中心看板
 */
public interface TechnologyCenterBoardService {

    /**
     * 数据总览-打版进行中数据
     * @return
     */
    TechnologyCenterBoardOverviewDataVo getPlateMakeUnderwayData(TechnologyCenterBoardDto dto);

    /**
     * 数据总览-打版完成数据
     * @return
     */
    TechnologyCenterBoardOverviewDataVo getPlateMakeFinishData(TechnologyCenterBoardDto dto);

    /**
     * 版师/样衣工 当前任务
     * @return
     */
    TechnologyCenterBoardCurrentTaskVo getCurrentTaskData(TechnologyCenterBoardDto dto);

    /**
     * 打版/样衣 产能数
     * @return
     */
    TechnologyCenterBoardCurrentTaskVo getCapacityNumber(TechnologyCenterBoardDto dto);

    /**
     * 打版/样衣 排名统计
     * @return
     */
    List<TechnologyCenterBoardDesignerRankVo> getDesignerRank(TechnologyCenterBoardDto dto);


}
