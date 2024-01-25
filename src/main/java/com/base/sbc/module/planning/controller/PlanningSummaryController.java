package com.base.sbc.module.planning.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailVo;
import com.base.sbc.module.planning.dto.PlanningSummaryQueryDto;
import com.base.sbc.module.planning.vo.PlanningSummaryQueryVo;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-25 9:40:08
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "企划汇总-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningSummary", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class PlanningSummaryController extends BaseController{
    private final OrderBookDetailService orderBookDetailService;
    private final PlanningProjectPlankService planningProjectPlankService;
    private final PlanningProjectService planningProjectService;
    private final PlanningProjectDimensionService planningProjectDimensionService;

    /**
     * 获取企划汇总列表
     * <p>
     * 需求数量:坑位总数量,下单数:订货本下单数量,
     */
    @RequestMapping("/queryList")
    public ApiResult queryList(PlanningSummaryQueryDto dto) {
        String createId = dto.getCreateId();
        List<String> ids=null;
        if (StringUtils.isNotBlank(createId)){
            ids= Arrays.asList(createId.split(","));
        }
        // 获取产品季下坑位总数量
        BaseQueryWrapper<PlanningProject> queryWrapper1 = new BaseQueryWrapper<>();
        queryWrapper1.eq("season_id", dto.getPlanningSeasonId());
        queryWrapper1.eq("status", "0");
        queryWrapper1.in(ids!=null,"create_id", ids);
        List<PlanningProject> list = planningProjectService.list(queryWrapper1);
        List<String> planningProjectIds = list.stream().map(PlanningProject::getId).collect(Collectors.toList());
        if(planningProjectIds.isEmpty()){
            return ApiResult.error("产品季下没有数据", 10000);
        }
        // 获取企划看板-规划-维度
        BaseQueryWrapper<PlanningProjectDimension> queryWrapper2 = new BaseQueryWrapper<>();
        queryWrapper2.in("planning_project_id", planningProjectIds);
        queryWrapper2.notEmptyEq("prod_category_code", dto.getCategoryCode());
        //企划看板有可能是别人创建的
        // queryWrapper2.in(ids!=null,"create_id", ids);
        List<PlanningProjectDimension> planningProjectDimensions = planningProjectDimensionService.list(queryWrapper2);

        // 获取企划看板-规划-坑位
        List<String> planningProjectDimensionIds = planningProjectDimensions.stream().map(PlanningProjectDimension::getId).collect(Collectors.toList());

        if(planningProjectDimensionIds.isEmpty()){
            return ApiResult.error("坑位下没有数据", 10000);
        }
        BaseQueryWrapper<PlanningProjectPlank> queryWrapper3 = new BaseQueryWrapper<>();
        queryWrapper3.in("planning_project_dimension_id", planningProjectDimensionIds);
        queryWrapper3.in(ids!=null,"create_id", ids);
        List<PlanningProjectPlank> planningProjectPlanks = planningProjectPlankService.list(queryWrapper3);

        // 获取产品季下下单的订货本
        BaseQueryWrapper<OrderBookDetail> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tob.season_id", dto.getPlanningSeasonId());
        queryWrapper.eq("tobl.is_order", "1");
        queryWrapper.notEmptyEq("ts.prod_category", dto.getCategoryCode());
        List<OrderBookDetailVo> orderBookDetailVos = orderBookDetailService.querylist(queryWrapper, null);

        Map<String, Integer> map = new HashMap<>();
        for (PlanningProjectDimension planningProjectDimension : planningProjectDimensions) {
            String bandName = planningProjectDimension.getDimensionName() +"-"+planningProjectDimension.getBandName();
            int i = 0;
            for (PlanningProjectPlank planningProjectPlank : planningProjectPlanks) {
                if (planningProjectPlank.getPlanningProjectDimensionId().equals(planningProjectDimension.getId())) {
                    i++;
                }
            }
            Integer bandNameSum = map.get(bandName);
            if (bandNameSum!=null){
                 map.put(bandName, bandNameSum+i);
            }else {
                map.put(bandName, i);
            }
        }

        //获取需求坑位总数量
        int sum = 0;
        for (String bandName : map.keySet()) {
            Integer num = map.get(bandName);
            sum+=num;
        }

        List<PlanningSummaryQueryVo>  planningSummaryQueryVos=new ArrayList<>();
        for (String bandName : map.keySet()) {
            Integer num = map.get(bandName);
            PlanningSummaryQueryVo planningSummaryQueryVo = new PlanningSummaryQueryVo();
            String[] split = bandName.split("-");

            planningSummaryQueryVo.setDimensionName(split[0]);
            planningSummaryQueryVo.setBandName(split[1]);
            planningSummaryQueryVo.setDemandNumber(String.valueOf(num));
            BigDecimal bigDecimal = new BigDecimal(sum);
            BigDecimal bigDecimal1 = new BigDecimal(num);
            planningSummaryQueryVo.setDemandProportion(bigDecimal1.divide(bigDecimal,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) +"%");
                int i=0;
            for (OrderBookDetailVo orderBookDetailVo : orderBookDetailVos) {
                if (orderBookDetailVo.getBandName().equals(split[0])) {
                    i++;
                }
            }

            planningSummaryQueryVo.setOrderNumber(String.valueOf(i));
            if (i==0){
                planningSummaryQueryVo.setOrderProportion("0%");
            }else {
                BigDecimal bigDecimal2 = new BigDecimal(i);
                BigDecimal bigDecimal3 = new BigDecimal(orderBookDetailVos.size());
                planningSummaryQueryVo.setOrderProportion(bigDecimal2.divide(bigDecimal3,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) +"%");

            }
            planningSummaryQueryVo.setDemandGap(String.valueOf(num-i));
            planningSummaryQueryVo.setSeatGap(String.valueOf(num-i));
            planningSummaryQueryVos.add(planningSummaryQueryVo);
        }

        return selectSuccess(planningSummaryQueryVos);
    }
}
