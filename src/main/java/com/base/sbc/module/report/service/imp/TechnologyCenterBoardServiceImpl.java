package com.base.sbc.module.report.service.imp;

import com.base.sbc.module.report.dto.TechnologyCenterBoardDto;
import com.base.sbc.module.report.service.TechnologyCenterBoardService;
import com.base.sbc.module.report.vo.TechnologyCenterBoardCurrentTaskVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardDesignerRankVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardOverviewDataVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyCenterBoardServiceImpl implements TechnologyCenterBoardService {
    @Override
    public TechnologyCenterBoardOverviewDataVo getPlateMakeUnderwayData(TechnologyCenterBoardDto dto) {
        return null;
    }

    @Override
    public TechnologyCenterBoardOverviewDataVo getPlateMakeFinishData(TechnologyCenterBoardDto dto) {
        return null;
    }

    @Override
    public TechnologyCenterBoardCurrentTaskVo getCurrentTaskData(TechnologyCenterBoardDto dto) {
        return null;
    }

    @Override
    public TechnologyCenterBoardCurrentTaskVo getCapacityNumber(TechnologyCenterBoardDto dto) {
        return null;
    }

    @Override
    public List<TechnologyCenterBoardDesignerRankVo> getDesignerRank(TechnologyCenterBoardDto dto) {
        return null;
    }
}
