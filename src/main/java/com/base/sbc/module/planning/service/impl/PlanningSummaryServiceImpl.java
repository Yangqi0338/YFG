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
import com.base.sbc.module.style.dto.QueryStyleDimensionDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleDimensionVO;
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
    private static final String ALL = "all";

    @Autowired
    private OrderBookDetailService orderBookDetailService;
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
        detailQueryWrapper.eq("prod_category_code", planningSummaryQueryDto.getCategoryCode());
        detailQueryWrapper.orderBy(true, true, "band_name");
        List<SeasonalPlanningDetails> seasonalList = seasonalPlanningDetailsService.list(detailQueryWrapper);
        if (CollectionUtils.isEmpty(seasonalList)) {
            return ApiResult.success("无数据");
        }
        String prodCategoryName = seasonalList.get(0).getProdCategoryName();

        // 订单数据
        QueryOrderDetailDTO dto = new QueryOrderDetailDTO();
        dto.setSeasonId(seasonalPlanning.getSeasonId());
        List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos = orderBookDetailService.querySeasonalPlanningOrder(dto);

        // 开款数据
        QueryWrapper<Style> styleQueryWrapper = new QueryWrapper<>();
        styleQueryWrapper.eq("planning_season_id", planningSummaryQueryDto.getPlanningSeasonId());
        styleQueryWrapper.eq("prod_category", planningSummaryQueryDto.getCategoryCode());
        styleQueryWrapper.in("status", "1", "2");
        List<Style> styleList = styleService.list(styleQueryWrapper);

        // 波段数据
        if (StringUtils.equals(BAND, planningSummaryQueryDto.getQueryType())) {
            Map<String, List<SeasonalPlanningDetails>> groupByBandMap = seasonalList.stream()
                    .collect(Collectors.groupingBy(
                            SeasonalPlanningDetails::getBandName, // 波段分组
                            Collectors.toList()
                    ));
            List<PlanningSummaryQueryVo> planningSummaryQueryVoList = new ArrayList<>();
            int sumProdCategory = seasonalList.stream().mapToInt(s -> Integer.valueOf(s.getSkcCount())).sum();
            for (String bandName : groupByBandMap.keySet()) {
                PlanningSummaryQueryVo planningSummary = new PlanningSummaryQueryVo();
                List<SeasonalPlanningDetails> bandList = groupByBandMap.get(bandName);
                int sumBand = bandList.stream().mapToInt(s -> Integer.valueOf(s.getSkcCount())).sum();
                planningSummary.setBandName(bandName);
                planningSummary.setDemandNumber(String.valueOf(sumBand));
                double demandProportion = (double)sumBand/sumProdCategory;
                planningSummary.setDemandProportion(String.format("%.2f", demandProportion));
                Integer orderNumber = countOrder(orderBookDetailVos, prodCategoryName, bandName, null, null, ALL);
                Integer orderBandNumber = countOrder(orderBookDetailVos, prodCategoryName, bandName, null, null, BAND);
                planningSummary.setOrderNumber(String.valueOf(orderBandNumber));
                if (orderNumber == 0 || orderBandNumber == 0) {
                    planningSummary.setOrderProportion("0");
                } else {
                    planningSummary.setOrderProportion(String.valueOf(orderBandNumber/orderNumber));
                }

                List<Style> filterList = styleList.stream().filter(style -> StringUtils.equals(bandName, style.getBandName())).collect(Collectors.toList());
                Integer styleSize = filterList.size();
                Integer gap = sumBand - styleSize;
                planningSummary.setDemandGap(String.valueOf(gap));
                Integer seatGap = sumBand - orderBandNumber;
                planningSummary.setSeatGap(String.valueOf(seatGap));
                planningSummaryQueryVoList.add(planningSummary);
            }
            Collections.sort(planningSummaryQueryVoList, Comparator.comparing(PlanningSummaryQueryVo::getBandName));
            result.setSuccess(true);
            result.setData(planningSummaryQueryVoList);
            return result;
        }

        // 维度查询
        if (StringUtils.equals(DIMENSION, planningSummaryQueryDto.getQueryType())) {
            Map<String, List<PlanningSummaryQueryVo>> planningSummaryQueryVoMap = new HashMap<>();
            QueryWrapper<CategoryPlanning> categoryQueryWrapper = new QueryWrapper<>();
            categoryQueryWrapper.eq("status", "0");
            categoryQueryWrapper.eq("season_id", planningSummaryQueryDto.getPlanningSeasonId());
            categoryQueryWrapper.eq("channel_code", planningSummaryQueryDto.getChannel());
            List<CategoryPlanning> categoryPlanningList = categoryPlanningService.list(categoryQueryWrapper);
            if (CollectionUtils.isEmpty(categoryPlanningList)) {
                return ApiResult.success("未生成品类企划");
            }
            CategoryPlanning categoryPlanning = categoryPlanningList.get(0);
            QueryWrapper<CategoryPlanningDetails> categoryDetailQueryWrapper = new QueryWrapper<>();
            categoryDetailQueryWrapper.eq("category_planning_id", categoryPlanning.getId());
            categoryDetailQueryWrapper.eq("prod_category_code", planningSummaryQueryDto.getCategoryCode());
            categoryDetailQueryWrapper.ne("is_generate", "2");
            List<CategoryPlanningDetails> categoryList = categoryPlanningDetailsService.list(categoryDetailQueryWrapper);
            Map<String, List<CategoryPlanningDetails>> dimensionNameMap = categoryList.stream()
                    .collect(Collectors.groupingBy(
                            CategoryPlanningDetails::getDimensionName, // 维度名称分组
                            Collectors.toList()
                    ));
            QueryStyleDimensionDto queryStyleDimensionDto = new QueryStyleDimensionDto();
            queryStyleDimensionDto.setPlanningSeasonId(planningSummaryQueryDto.getPlanningSeasonId());
            queryStyleDimensionDto.setProdCategory(planningSummaryQueryDto.getCategoryCode());
            queryStyleDimensionDto.setStatus("0");
            List<StyleDimensionVO> styleDimensionVOS = styleService.queryStyleField(queryStyleDimensionDto);

            for (String dimensionName : dimensionNameMap.keySet()) {
                List<CategoryPlanningDetails> dimensionNameList = dimensionNameMap.get(dimensionName);
                Map<String, List<CategoryPlanningDetails>> dimensionCodeMap = dimensionNameList.stream()
                        .collect(Collectors.groupingBy(
                                CategoryPlanningDetails::getDimensionCode, // code分组
                                Collectors.toList()
                        ));
                int sumDimensionName = dimensionNameList.stream().mapToInt(s -> Integer.valueOf(s.getNumber())).sum();
                List<PlanningSummaryQueryVo> planningSummaryDimensionList = new ArrayList<>();
                for (String dimensionCode : dimensionCodeMap.keySet()) {
                    PlanningSummaryQueryVo planningSummary = new PlanningSummaryQueryVo();
                    List<CategoryPlanningDetails> dimensionCodeList = dimensionCodeMap.get(dimensionCode);
                    int sumDimensionCode = dimensionCodeList.stream().mapToInt(s -> Integer.valueOf(s.getNumber())).sum();
                    planningSummary.setDimensionName(dimensionCode);
                    planningSummary.setDemandNumber(String.valueOf(sumDimensionCode));
                    if (sumDimensionName == 0) {
                        planningSummary.setDemandProportion("0");
                    } else {
                        double demandProportion = (double)sumDimensionCode/sumDimensionName;
                        planningSummary.setDemandProportion(String.format("%.2f", demandProportion));
                    }

                    Integer orderNumber = countOrder(orderBookDetailVos, prodCategoryName, null, dimensionName, null, DIMENSION);
                    Integer orderDimensionNumber = countOrder(orderBookDetailVos, prodCategoryName, null, dimensionName, dimensionCode, DIMENSION);
                    planningSummary.setOrderNumber(String.valueOf(orderDimensionNumber));
                    if (orderNumber == 0) {
                        planningSummary.setOrderProportion("0");
                    } else {
                        double orderProportion = (double)orderDimensionNumber/orderNumber;
                        planningSummary.setOrderProportion(String.format("%.2f", orderProportion));
                    }
                    List<StyleDimensionVO> styleDimensionVOList = styleDimensionVOS.stream().filter(s -> StringUtils.equals(dimensionName, s.getFieldExplain()) &&
                            StringUtils.equals(dimensionCode, s.getStyleName())).collect(Collectors.toList());
                    Integer dSize = styleDimensionVOList.size();
                    Integer gapNumber = sumDimensionCode - dSize;
                    planningSummary.setDemandGap(String.valueOf(gapNumber));
                    Integer orderGap = sumDimensionCode - orderDimensionNumber;
                    planningSummary.setSeatGap(String.valueOf(orderGap));
                    planningSummaryDimensionList.add(planningSummary);
                }
                planningSummaryQueryVoMap.put(dimensionName, planningSummaryDimensionList);
            }
            result.setSuccess(true);
            result.setData(planningSummaryQueryVoMap);
            return result;
        }
        return null;
    }

    private int countOrder(List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos, String prodCategory,
                           String bandName, String dimension, String dimensionCode, String type) {
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
                    Collectors.groupingBy(OrderBookDetailForSeasonPlanningVO::getBandName, // 波段分组
                            Collectors.toList()
                    ));
            List<OrderBookDetailForSeasonPlanningVO> bandOrderList = bandOrderMap.get(bandName);
            if (CollectionUtils.isEmpty(bandOrderList)) {
                return 0;
            }
            return bandOrderList.size();
        } else {
            Map<String, List<OrderBookDetailForSeasonPlanningVO>> prodCategoryOrderMap = orderBookDetails.stream().collect(
                    Collectors.groupingBy(OrderBookDetailForSeasonPlanningVO::getProdCategoryName, // 品类分组
                            Collectors.toList()
                    ));
            List<OrderBookDetailForSeasonPlanningVO> getProdCategoryList = prodCategoryOrderMap.get(prodCategory);
            if (CollectionUtils.isEmpty(getProdCategoryList)) {
                return 0;
            }
            if (StringUtils.isEmpty(dimensionCode)) {
                List<OrderBookDetailForSeasonPlanningVO> filterByFieldExplain = getProdCategoryList.stream().filter(order ->
                        StringUtils.equals(dimension, order.getFieldExplain())
                ).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(filterByFieldExplain)) {
                    return 0;
                }
                return filterByFieldExplain.size();
            }

            List<OrderBookDetailForSeasonPlanningVO> filterStyleNameList = getProdCategoryList.stream().filter(order ->
                        StringUtils.equals(dimension, order.getFieldExplain()) && StringUtils.equals(dimensionCode, order.getStyleName())
                    ).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filterStyleNameList)) {
                return 0;
            }
            return filterStyleNameList.size();
        }
    }
}
