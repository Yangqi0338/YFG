package com.base.sbc.module.report.service.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternMakingTaskListVo;
import com.base.sbc.module.report.dto.TechnologyCenterBoardDto;
import com.base.sbc.module.report.mapper.TechnologyCenterBoardMapper;
import com.base.sbc.module.report.service.TechnologyCenterBoardService;
import com.base.sbc.module.report.vo.TechnologyCenterBoardCapacityNumberVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardCurrentTaskVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardDesignerRankVo;
import com.base.sbc.module.report.vo.TechnologyCenterBoardOverviewDataVo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyCenterBoardServiceImpl implements TechnologyCenterBoardService {
    
    @Resource
    private TechnologyCenterBoardMapper technologyCenterBoardMapper;

    @Override
    public TechnologyCenterBoardOverviewDataVo getPlateMakeUnderwayData(TechnologyCenterBoardDto dto) {
        BaseQueryWrapper qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(0, "打版任务", 0, Arrays.asList("待接收"), "count(0) as preAcceptedQuantity"));
        TechnologyCenterBoardOverviewDataVo data = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw);
        BaseQueryWrapper qw1 = getBaseQueryWrapper(new TechnologyCenterBoardDto(0, "打版任务", 0, Arrays.asList("打版中"), "count(0) as plateMakingQuantity"));
        TechnologyCenterBoardOverviewDataVo data1 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw1);
        data.setPlateMakingQuantity(data1.getPlateMakingQuantity());
        BaseQueryWrapper qw2 = getBaseQueryWrapper(new TechnologyCenterBoardDto(0, "打版任务", 1, null, "count(0) as breakPlateMakingQuantity"));
        TechnologyCenterBoardOverviewDataVo data2 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw2);
        data.setBreakPlateMakingQuantity(data2.getBreakPlateMakingQuantity());
        BaseQueryWrapper qw3 = getBaseQueryWrapper(new TechnologyCenterBoardDto(0, "样衣任务", 0, Arrays.asList("裁剪开始"), "count(0) as cuttingStartQuantity"));
        TechnologyCenterBoardOverviewDataVo data3 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw3);
        data.setCuttingStartQuantity(data3.getCuttingStartQuantity());
        BaseQueryWrapper qw4 = getBaseQueryWrapper(new TechnologyCenterBoardDto(0, "样衣任务", 0, Arrays.asList("车缝进行中"), "count(0) as sewingStartQuantity"));
        TechnologyCenterBoardOverviewDataVo data4 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw4);
        data.setSewingStartQuantity(data4.getSewingStartQuantity());
        return data;
    }

    @NotNull
    private static BaseQueryWrapper getBaseQueryWrapper(TechnologyCenterBoardDto dto) {
        BaseQueryWrapper qw = new BaseQueryWrapper();
        qw.eq("p.design_send_status", "1");
        qw.eq("p.prm_send_status", "1");
        qw.ne("p.del_flag", "1");
        qw.ne("s.del_flag", "1");
        qw.eq("p.finish_flag", "0");
        qw.eq("p.suspend", "0");
        qw.in("p.disable_flag", "0");
        qw.notEmptyEq("p.historical_data", dto.getHistoricalData());
        qw.notEmptyEq("p.node", dto.getNode());
        qw.notEmptyEq("p.break_off_pattern", dto.getBreakOffPattern());
        qw.notEmptyIn("p.status", dto.getNodeStatusList());
        qw.select(dto.getSqlSelect());
        return qw;
    }

    @Override
    public TechnologyCenterBoardOverviewDataVo getPlateMakeFinishData(TechnologyCenterBoardDto dto) {
        DateTime date = DateUtil.date();
        DateTime newDate = DateUtil.offsetDay(date, -6);
        String[] betweenDate = {DateUtil.format(newDate, "yyyy-MM-dd"),DateUtil.format(date, "yyyy-MM-dd")};
        dto.setBetweenDate(betweenDate);
        BaseQueryWrapper qw = new BaseQueryWrapper();
        qw.eq("p.design_send_status", "1");
        qw.ne("p.del_flag", "1");
        qw.ne("s.del_flag", "1");
        qw.in("p.disable_flag", "0");
        qw.between(" date_format(p.create_date,'%Y-%m-%d')",dto.getBetweenDate());

        qw.select("count(0) as plateMakingDemandQuantity");
        TechnologyCenterBoardOverviewDataVo data = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw);

        BaseQueryWrapper qw1 = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as plateMakingFinishQuantity"));


        qw1.between(" date_format(p.create_date,'%Y-%m-%d')",dto.getBetweenDate());

        TechnologyCenterBoardOverviewDataVo data1 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw1);
        data.setPlateMakingFinishQuantity(data1.getPlateMakingFinishQuantity());

        BaseQueryWrapper qw2 = new BaseQueryWrapper();
        qw2.eq("p.finish_flag", "1");
        qw2.eq("p.break_off_sample", "0");
        qw2.eq("p.prm_send_status", "1");
        qw2.ne("p.del_flag", "1");
        qw2.ne("s.del_flag", "1");
        qw2.eq("p.suspend", "0");
        qw2.in("p.disable_flag", "0");
        qw2.between(" date_format(p.create_date,'%Y-%m-%d')",dto.getBetweenDate());
        qw2.notEmptyEq("p.node", "样衣任务");
        qw2.select("count(0) as sampleFinishQuantity");
        TechnologyCenterBoardOverviewDataVo data2 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw2);
        data.setSampleFinishQuantity(data2.getSampleFinishQuantity());
        data.setBetweenDate(betweenDate);
        return data;
    }

    @Override
    public TechnologyCenterBoardCurrentTaskVo getCurrentTaskData(TechnologyCenterBoardDto dto) {
        return null;
    }

    @Override
    public List<TechnologyCenterBoardCapacityNumberVo> getCapacityNumber(TechnologyCenterBoardDto dto) {

        BaseQueryWrapper qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as sewingStartQuantity"));

        technologyCenterBoardMapper.getPlateMakeUnderwayData(qw);

        return null;
    }

    @Override
    public List<TechnologyCenterBoardDesignerRankVo> getDesignerRank(TechnologyCenterBoardDto dto) {
        return null;
    }
}
