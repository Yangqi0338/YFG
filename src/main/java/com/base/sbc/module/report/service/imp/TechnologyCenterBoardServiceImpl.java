package com.base.sbc.module.report.service.imp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.base.sbc.config.common.BaseQueryWrapper;
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
import java.util.*;

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
        if (dto.getBetweenDate() == null || dto.getBetweenDate().length == 0) {
            DateTime date = DateUtil.date();
            DateTime newDate = DateUtil.offsetDay(date, -6);
            String[] betweenDate = {DateUtil.format(newDate, "yyyy-MM-dd"), DateUtil.format(date, "yyyy-MM-dd")};
            dto.setBetweenDate(betweenDate);
        }
        BaseQueryWrapper qw = new BaseQueryWrapper();
        qw.eq("p.design_send_status", "1");
        qw.ne("p.del_flag", "1");
        qw.ne("s.del_flag", "1");
        qw.in("p.disable_flag", "0");
        qw.between(" date_format(p.create_date,'%Y-%m-%d')", dto.getBetweenDate());

        qw.select("count(0) as plateMakingDemandQuantity");
        TechnologyCenterBoardOverviewDataVo data = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw);

        BaseQueryWrapper qw1 = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as plateMakingFinishQuantity"));


        qw1.between(" date_format(p.create_date,'%Y-%m-%d')", dto.getBetweenDate());

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
        qw2.between(" date_format(p.create_date,'%Y-%m-%d')", dto.getBetweenDate());
        qw2.notEmptyEq("p.node", "样衣任务");
        qw2.select("count(0) as sampleFinishQuantity");
        TechnologyCenterBoardOverviewDataVo data2 = technologyCenterBoardMapper.getPlateMakeUnderwayData(qw2);
        data.setSampleFinishQuantity(data2.getSampleFinishQuantity());
        data.setBetweenDate(dto.getBetweenDate());
        return data;
    }

    @Override
    public List<TechnologyCenterBoardCurrentTaskVo> getCurrentTaskData(TechnologyCenterBoardDto dto) {
        List<TechnologyCenterBoardCurrentTaskVo> resultDate = new ArrayList<>();


        BaseQueryWrapper qw = null;
        if ("0".equals(dto.getDataType())) {
            qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版中","已接收"), " p.pattern_design_id as id ,p.pattern_design_name as userName,s.design_no as designNo,p.sample_type_name"));
        } else if ("1".equals(dto.getDataType())) {
            qw = new BaseQueryWrapper();
            qw.eq("p.finish_flag", "1");
            qw.eq("p.break_off_sample", "0");
            qw.eq("p.prm_send_status", "1");
            qw.ne("p.del_flag", "1");
            qw.ne("s.del_flag", "1");
            qw.eq("p.suspend", "0");
            qw.in("p.disable_flag", "0");
            qw.eq("p.node", "样衣任务");
            qw.select(" p.stitcher_id as id,p.stitcher as userName,s.design_no as designNo,p.sample_type_name");
        }
        List<TechnologyCenterBoardCurrentTaskVo> list = technologyCenterBoardMapper.getCurrentTaskData(qw);
        Map<String,TechnologyCenterBoardCurrentTaskVo> mapData = new HashMap<>();
        if (CollUtil.isNotEmpty(list)) {
            for (TechnologyCenterBoardCurrentTaskVo currentTaskVo : list) {
                String id = currentTaskVo.getId();
                if (mapData.containsKey(id)) {
                    TechnologyCenterBoardCurrentTaskVo boardCurrentTaskVo = mapData.get(id);
                    LinkedHashMap<String, Integer> plateMakingTypeMap = boardCurrentTaskVo.getPlateMakingTypeMap();
                    if (plateMakingTypeMap.containsKey(currentTaskVo.getSampleTypeName())) {
                        Integer count = plateMakingTypeMap.get(currentTaskVo.getSampleTypeName());
                        plateMakingTypeMap.put(currentTaskVo.getSampleTypeName(), count + 1);
                        plateMakingTypeMap.put("总个数", plateMakingTypeMap.get("总个数") + 1);
                        boardCurrentTaskVo.setCount(boardCurrentTaskVo.getCount()+1);
                    }
                    List<String> designNolist = boardCurrentTaskVo.getDesignNolist();
                    if (designNolist.size() < 4) {
                        designNolist.add(currentTaskVo.getDesignNo());
                    }
                }else{
                    TechnologyCenterBoardCurrentTaskVo boardCurrentTaskVo = new TechnologyCenterBoardCurrentTaskVo();
                    boardCurrentTaskVo.setUserName(currentTaskVo.getUserName());
                    LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
                    map.put("初版样",0);
                    map.put("改版样",0);
                    map.put("齐色样",0);
                    map.put("拍照样",0);
                    map.put("产前样",0);
                    map.put("总个数",0);
                    if (map.containsKey(currentTaskVo.getSampleTypeName())) {
                        Integer count = map.get(currentTaskVo.getSampleTypeName());
                        map.put(currentTaskVo.getSampleTypeName(), count + 1);
                        map.put("总个数", map.get("总个数") + 1);
                        boardCurrentTaskVo.setCount(1);
                    }
                    boardCurrentTaskVo.setId(boardCurrentTaskVo.getId());
                    boardCurrentTaskVo.setDesignNolist(new ArrayList<>(Arrays.asList(currentTaskVo.getDesignNo())));
                    boardCurrentTaskVo.setPlateMakingTypeMap(map);
                    mapData.put(id,boardCurrentTaskVo);
                }
            }
        }

        for (Map.Entry<String, TechnologyCenterBoardCurrentTaskVo> taskVoEntry : mapData.entrySet()) {
            resultDate.add(taskVoEntry.getValue());
        }
        resultDate.sort(Comparator.comparing(TechnologyCenterBoardCurrentTaskVo::getCount).reversed());
        return resultDate;
    }

    @Override
    public List<TechnologyCenterBoardCapacityNumberVo> getCapacityNumber(TechnologyCenterBoardDto dto) {
        List<TechnologyCenterBoardCapacityNumberVo> list = new ArrayList<>();


        String type = dto.getType();
        //天 最近7天
        if ((dto.getBetweenDate() == null || dto.getBetweenDate().length == 0) && "day".equals(type)) {
            DateTime date = DateUtil.date();
            for (int i = 0; i <= 6; i++) {
                BaseQueryWrapper qw = null;
                if ("0".equals(dto.getDataType())) {
                    qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as count"));
                } else if ("1".equals(dto.getDataType())) {
                    qw = new BaseQueryWrapper();
                    qw.eq("p.finish_flag", "1");
                    qw.eq("p.break_off_sample", "0");
                    qw.eq("p.prm_send_status", "1");
                    qw.ne("p.del_flag", "1");
                    qw.ne("s.del_flag", "1");
                    qw.eq("p.suspend", "0");
                    qw.in("p.disable_flag", "0");
                    qw.eq("p.node", "样衣任务");
                    qw.select("count(0) as count");
                }
                DateTime newDate = DateUtil.offsetDay(date, -(i));
                String format = DateUtil.format(newDate, "yyyy-MM-dd");
                qw.eq(" date_format(p.create_date,'%Y-%m-%d')", format);
                TechnologyCenterBoardCapacityNumberVo capacityNumber = technologyCenterBoardMapper.getCapacityNumber(qw);
                capacityNumber.setDateFormat(format);
                capacityNumber.setBetweenDate(new String[]{DateUtil.format(DateUtil.offsetDay(new Date(), -6), "yyyy-MM-dd"), DateUtil.format(new Date(), "yyyy-MM-dd")});
                list.add(capacityNumber);
            }
        }
        //周 最近5周
        if ((dto.getBetweenDate() == null || dto.getBetweenDate().length == 0) && "week".equals(type)) {
            DateTime date = DateUtil.date();
            for (int i = 7; i <= 35; i = i + 7) {
                BaseQueryWrapper qw = null;
                if ("0".equals(dto.getDataType())) {
                    qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as count"));
                } else if ("1".equals(dto.getDataType())) {
                    qw = new BaseQueryWrapper();
                    qw.eq("p.finish_flag", "1");
                    qw.eq("p.break_off_sample", "0");
                    qw.eq("p.prm_send_status", "1");
                    qw.ne("p.del_flag", "1");
                    qw.ne("s.del_flag", "1");
                    qw.eq("p.suspend", "0");
                    qw.in("p.disable_flag", "0");
                    qw.eq("p.node", "样衣任务");
                    qw.select("count(0) as count");
                }
                DateTime newDate = DateUtil.offsetDay(date, -(7));
                String[] betweenDate = {DateUtil.format(newDate, "yyyy-MM-dd"), DateUtil.format(date, "yyyy-MM-dd")};
                qw.between(" date_format(p.create_date,'%Y-%m-%d')", betweenDate);
                TechnologyCenterBoardCapacityNumberVo capacityNumber = technologyCenterBoardMapper.getCapacityNumber(qw);
                date = newDate;
                capacityNumber.setDateFormat(betweenDate[0] + "/" + betweenDate[1]);
                capacityNumber.setBetweenDate(new String[]{DateUtil.format(DateUtil.offsetDay(new Date(), -35), "yyyy-MM-dd"), DateUtil.format(new Date(), "yyyy-MM-dd")});
                list.add(capacityNumber);
            }
        }

        //月 最近6月
        if ((dto.getBetweenDate() == null || dto.getBetweenDate().length == 0) && "month".equals(type)) {
            DateTime date = DateUtil.date();
            for (int i = 30; i <= 180; i = i + 30) {
                BaseQueryWrapper qw = null;
                if ("0".equals(dto.getDataType())) {
                    qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as count"));
                } else if ("1".equals(dto.getDataType())) {
                    qw = new BaseQueryWrapper<TechnologyCenterBoardDto>();
                    qw.eq("p.finish_flag", "1");
                    qw.eq("p.break_off_sample", "0");
                    qw.eq("p.prm_send_status", "1");
                    qw.ne("p.del_flag", "1");
                    qw.ne("s.del_flag", "1");
                    qw.eq("p.suspend", "0");
                    qw.in("p.disable_flag", "0");
                    qw.eq("p.node", "样衣任务");
                    qw.select("count(0) as count");
                }
                DateTime newDate = DateUtil.offsetDay(date, -(30));
                String[] betweenDate = {DateUtil.format(newDate, "yyyy-MM-dd"), DateUtil.format(date, "yyyy-MM-dd")};
                qw.between(" date_format(p.create_date,'%Y-%m-%d')", betweenDate);
                TechnologyCenterBoardCapacityNumberVo capacityNumber = technologyCenterBoardMapper.getCapacityNumber(qw);
                capacityNumber.setDateFormat(betweenDate[0] + "/" + betweenDate[1]);
                capacityNumber.setBetweenDate(new String[]{DateUtil.format(DateUtil.offsetDay(new Date(), -180), "yyyy-MM-dd"), DateUtil.format(new Date(), "yyyy-MM-dd")});
                date = newDate;

                list.add(capacityNumber);
            }
        }
        return list;
    }

    @Override
    public List<TechnologyCenterBoardDesignerRankVo> getDesignerRank(TechnologyCenterBoardDto dto) {
        BaseQueryWrapper qw = null;
        String[] betweenDate = dto.getBetweenDate();
        if ((dto.getBetweenDate() == null || dto.getBetweenDate().length == 0)) {
            DateTime date = DateUtil.date();
            DateTime newDate = DateUtil.offsetDay(date, -(7));
            betweenDate = new String[]{DateUtil.format(newDate, "yyyy-MM-dd"), DateUtil.format(date, "yyyy-MM-dd")};
        }

        if ("0".equals(dto.getDataType())) {
            qw = getBaseQueryWrapper(new TechnologyCenterBoardDto(null, "打版任务", 0, Arrays.asList("打版完成"), "count(0) as count"));
            qw.between(" date_format(p.create_date,'%Y-%m-%d')", betweenDate);
            qw.select("p.pattern_design_name as designer,count(0) as count");
            qw.groupBy(" p.pattern_design_id,p.pattern_design_name");
            qw.orderByDesc("count(0)");
            qw.last("limit 20");
        } else if ("1".equals(dto.getDataType())) {
            qw = new BaseQueryWrapper<TechnologyCenterBoardDto>();
            qw.eq("p.finish_flag", "1");
            qw.eq("p.break_off_sample", "0");
            qw.eq("p.prm_send_status", "1");
            qw.ne("p.del_flag", "1");
            qw.ne("s.del_flag", "1");
            qw.eq("p.suspend", "0");
            qw.in("p.disable_flag", "0");
            qw.eq("p.node", "样衣任务");
            qw.between(" date_format(p.create_date,'%Y-%m-%d')", betweenDate);
            qw.select("p.stitcher as designer,count(0) as count");
            qw.groupBy(" p.stitcher_id,p.stitcher");
            qw.orderByDesc("count(0)");
            qw.last("limit 20");
        }

        List<TechnologyCenterBoardDesignerRankVo> designerRankList = technologyCenterBoardMapper.getDesignerRank(qw);
        if (CollUtil.isNotEmpty(designerRankList)) {
            for (TechnologyCenterBoardDesignerRankVo vo : designerRankList) {
                vo.setBetweenDate(betweenDate);
            }
        }
        return designerRankList;
    }
}
