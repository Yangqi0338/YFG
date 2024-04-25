package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.orderbook.dto.QueryOrderDetailDTO;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailForSeasonPlanningVO;
import com.base.sbc.module.planning.dto.PlanningSummaryQueryDto;
import com.base.sbc.module.planning.service.PlanningSummaryService;
import com.base.sbc.module.planning.vo.PlanningSummaryQueryVo;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanningSummaryServiceImpl implements PlanningSummaryService {
    private static final String BAND = "band";
    private static final String DIMENSION = "dimension";
    private static final String ALL = "ALL";

    @Autowired
    private OrderBookDetailService orderBookDetailService;
    @Autowired
    private PlanningProjectPlankService planningProjectPlankService;
    @Autowired
    private PlanningProjectService planningProjectService;
    @Autowired
    private PlanningProjectDimensionService planningProjectDimensionService;
    @Autowired
    private SeasonalPlanningService seasonalPlanningService;
    @Autowired
    private SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    @Autowired
    private CategoryPlanningService categoryPlanningService;
    @Autowired
    private CategoryPlanningDetailsService categoryPlanningDetailsService;
    @Autowired
    private StyleService styleService;

    @Override
    public ApiResult queryList(PlanningSummaryQueryDto planningSummaryQueryDto) {
        ApiResult result = new ApiResult();
        // 波段维度查询
        // 查询总需求列表
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "0");
        queryWrapper.eq("season_id", planningSummaryQueryDto.getPlanningSeasonId());
        queryWrapper.eq("channel_code", planningSummaryQueryDto.getChannel());
        List<SeasonalPlanning> seasonalPlanningDetailsList = seasonalPlanningService.list(queryWrapper);
        if (CollectionUtils.isEmpty(seasonalPlanningDetailsList)) {
            return ApiResult.success("无数据");
        }

        SeasonalPlanning seasonalPlanning = seasonalPlanningDetailsList.get(0);
        QueryWrapper<SeasonalPlanningDetails> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("seasonal_planning_id", seasonalPlanning.getId());
        detailQueryWrapper.orderBy(true, true, "band_name");
        List<SeasonalPlanningDetails> seasonalList = seasonalPlanningDetailsService.list(detailQueryWrapper);

        // 订单数据
        QueryOrderDetailDTO dto = new QueryOrderDetailDTO();
        dto.setSeasonId(seasonalPlanning.getSeasonId());
        List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos = orderBookDetailService.querySeasonalPlanningOrder(dto);

        // 开款数据
        QueryWrapper<Style> styleQueryWrapper = new QueryWrapper<>();
        styleQueryWrapper.eq("planning_season_id", planningSummaryQueryDto.getPlanningSeasonId());
        styleQueryWrapper.eq("status", "1");
        List<Style> styleList = styleService.list(styleQueryWrapper);

        Map<String, List<PlanningSummaryQueryVo>> seasonalMap = new HashMap<>();
        Map<String, List<SeasonalPlanningDetails>> groupByProdCategoryNameMap = seasonalList.stream()
                .collect(Collectors.groupingBy(
                        SeasonalPlanningDetails::getProdCategoryName, // 品类分组
                        Collectors.toList()));

        // 波段数据
        if (StringUtils.equals(BAND, planningSummaryQueryDto.getQueryType())) {
            for (String prodCategoryName : groupByProdCategoryNameMap.keySet()) {
                List<SeasonalPlanningDetails> prodCategoryList = groupByProdCategoryNameMap.get(prodCategoryName);
                Map<String, List<SeasonalPlanningDetails>> groupByBandMap = prodCategoryList.stream()
                        .collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getBandName, // 波段分组
                                Collectors.toList()
                        ));
                List<PlanningSummaryQueryVo> planningSummaryQueryVoList = new ArrayList<>();
                int sumProdCategory = prodCategoryList.stream().mapToInt(s -> Integer.valueOf(s.getSkcCount())).sum();
                for (String bandName : groupByBandMap.keySet()) {
                    PlanningSummaryQueryVo planningSummary = new PlanningSummaryQueryVo();
                    List<SeasonalPlanningDetails> bandList = groupByBandMap.get(bandName);
                    int sumBand = bandList.stream().mapToInt(s -> Integer.valueOf(s.getSkcCount())).sum();
                    planningSummary.setBandName(bandName);
                    planningSummary.setDemandNumber(String.valueOf(sumBand));
                    double demandProportion = (double)sumBand/sumProdCategory;
                    planningSummary.setDemandProportion(String.format("%.2f", demandProportion));
                    Integer orderNumber = countOrder(orderBookDetailVos, prodCategoryName, bandName, ALL);
                    Integer orderBandNumber = countOrder(orderBookDetailVos, prodCategoryName, bandName, BAND);
                    planningSummary.setOrderNumber(String.valueOf(orderBandNumber));
                    if (orderNumber == 0) {
                        planningSummary.setOrderProportion("0");
                    } else {
                        planningSummary.setOrderProportion(String.valueOf(orderNumber/orderBandNumber));
                    }
                    // TODO SUNBand - 开款数
                    planningSummary.setDemandGap(String.valueOf(sumBand));
                    planningSummary.setSeatGap(String.valueOf(sumBand - orderBandNumber));
                    planningSummaryQueryVoList.add(planningSummary);
                }
                seasonalMap.put(prodCategoryName, planningSummaryQueryVoList);
            }
            result.setSuccess(true);
            result.setData(seasonalMap);
            return result;
        }

        // 维度查询
        if (StringUtils.equals(DIMENSION, planningSummaryQueryDto.getQueryType())) {
            QueryWrapper<CategoryPlanning> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.eq("status", "0");
            categoryQueryWrapper.eq("season_id", planningSummaryQueryDto.getPlanningSeasonId());
            categoryQueryWrapper.eq("channel_code", planningSummaryQueryDto.getChannel());
            List<CategoryPlanning> categoryPlanningList = categoryPlanningService.list(categoryQueryWrapper);
            if (CollectionUtils.isEmpty(seasonalPlanningDetailsList)) {
                return ApiResult.success("未生成品类企划");
            }
            CategoryPlanning categoryPlanning = categoryPlanningList.get(0);
            QueryWrapper<CategoryPlanningDetails> categoryDetailQueryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_planning_id", categoryPlanning.getId());
            List<CategoryPlanningDetails> categoryList = categoryPlanningDetailsService.list(categoryDetailQueryWrapper);
        }




        return null;
    }

    private int countOrder(List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos, String prodCategory, String bandName, String type) {
        if (CollectionUtils.isEmpty(orderBookDetailVos)) {
            return 0;
        }

        List<OrderBookDetailForSeasonPlanningVO> orderBookDetails = orderBookDetailVos.stream().filter(o -> o.getStyleId() != null).collect(Collectors.toList());
        if (StringUtils.equals(BAND, type) || StringUtils.equals(ALL, type)) {
            // 款式设计id去重
            List<OrderBookDetailForSeasonPlanningVO> distinctOrderList = orderBookDetails.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OrderBookDetailForSeasonPlanningVO::getStyleId))),
                            ArrayList::new
                    ));
            Map<String, List<OrderBookDetailForSeasonPlanningVO>> prodCategoryOrderMap = distinctOrderList.stream().collect(
                    Collectors.groupingBy(OrderBookDetailForSeasonPlanningVO::getProdCategoryName, // 品类分组
                            Collectors.toList()
                    ));
            List<OrderBookDetailForSeasonPlanningVO> prodCategoryOrderList = prodCategoryOrderMap.get(prodCategory);
            if (CollectionUtils.isEmpty(prodCategoryOrderList)) {
                return 0;
            }
            if (StringUtils.equals(ALL, type)) {
                return prodCategoryOrderList.size();
            }
            Map<String, List<OrderBookDetailForSeasonPlanningVO>> bandOrderMap = distinctOrderList.stream().collect(
                    Collectors.groupingBy(OrderBookDetailForSeasonPlanningVO::getBandName, // 品类分组
                            Collectors.toList()
                    ));
            List<OrderBookDetailForSeasonPlanningVO> bandOrderList = bandOrderMap.get(bandName);
            if (CollectionUtils.isEmpty(bandOrderList)) {
                return 0;
            }
            return bandOrderList.size();
        }

        return 0;
    }
}
