package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicDictDepend;
import com.base.sbc.client.ccm.entity.BasicDictDependsQueryDto;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.orderbook.dto.QueryOrderDetailDTO;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.service.OrderBookService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailForSeasonPlanningVO;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningQueryDto;
import com.base.sbc.module.planningproject.dto.SeasonalPlanningSaveDto;
import com.base.sbc.module.planningproject.entity.CategoryPlanning;
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.SeasonalPlanning;
import com.base.sbc.module.planningproject.entity.SeasonalPlanningDetails;
import com.base.sbc.module.planningproject.mapper.SeasonalPlanningMapper;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningDetailVO;
import com.base.sbc.module.planningproject.vo.SeasonalPlanningVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-18 17:08:00
 * @mail 247967116@qq.com
 */
@Service
public class SeasonalPlanningServiceImpl extends BaseServiceImpl<SeasonalPlanningMapper, SeasonalPlanning> implements SeasonalPlanningService {

    private final static String COLUMN = "column";
    private final static String ROW = "row";

    private final static String pattern = "[1-9]\\d*";

    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    @Autowired
    private OrderBookService orderBookService;
    @Autowired
    private OrderBookDetailService orderBookDetailService;
    @Autowired
    private PlanningProjectDimensionService planningProjectDimensionService;
    @Autowired
    private StyleColorService styleColorService;
    @Autowired
    private BasicsdatumDimensionalityService basicsdatumDimensionalityService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private CategoryPlanningService categoryPlanningService;
    @Autowired
    private CategoryPlanningDetailsService categoryPlanningDetailsService;

    @Override
    public ApiResult importSeasonalPlanningExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) {
        ApiResult result = checkParam(seasonalPlanningSaveDto);
        if (!result.getSuccess()) {
            return result;
        }

        // Excel 转 List
        List<HashMap<Integer, String>> hashMaps = null;
        try {
            hashMaps = EasyExcel.read(file.getInputStream()).headRowNumber(0).doReadAllSync();
        } catch (IOException e) {
            return ApiResult.error("文件解析异常：" + e.getMessage(), 500);
        }

        Map<Integer, SeasonalPlanningDetails> detailsMap = new HashMap<>();
        List<SeasonalPlanningDetails> importDetailsList = new ArrayList<>();

        List<BasicStructureTreeVo> basicStructureTreeVos = ccmFeignService.basicStructureTreeByCode("品类", null, "0,1,2");
        Map<String, String> prodCategoryMap = new HashMap<>();
        for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVos) {
            //大类
            for (BasicStructureTreeVo child : basicStructureTreeVo.getChildren()) {
                //品类
                for (BasicStructureTreeVo childChild : child.getChildren()) {
                    //中类
                    prodCategoryMap.put(basicStructureTreeVo.getName() + "_" + child.getName() + "_" + childChild.getName(),
                            basicStructureTreeVo.getValue() + "_" + child.getValue() + "_" + childChild.getValue());
                }
                prodCategoryMap.put(basicStructureTreeVo.getName() + "_" + child.getName(),
                        basicStructureTreeVo.getValue() + "_" + child.getValue());
            }
            prodCategoryMap.put(basicStructureTreeVo.getName(), basicStructureTreeVo.getValue());
        }

        ApiResult apiResult = buildImportExcelDetails(hashMaps, detailsMap, prodCategoryMap, seasonalPlanningSaveDto, importDetailsList);
        if (!apiResult.getSuccess()) {
            return apiResult;
        }

        // 新增
        if (StringUtils.isBlank(seasonalPlanningSaveDto.getId())) {
            // 保存产品企划
            saveSeasonalPlanning(seasonalPlanningSaveDto);
            for (SeasonalPlanningDetails details : importDetailsList) {
                details.setSeasonalPlanningId(seasonalPlanningSaveDto.getId());
            }
            seasonalPlanningDetailsService.saveBatch(importDetailsList);
            return ApiResult.success("导入成功");
        }

        // 比较更新
        QueryWrapper<SeasonalPlanningDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seasonal_planning_id", seasonalPlanningSaveDto.getId());
        List<SeasonalPlanningDetails> seasonalPlanningDetailsList = seasonalPlanningDetailsService.list(queryWrapper);
        // 新增list
        List<SeasonalPlanningDetails> addList = new ArrayList<>();
        // 更新list
        List<SeasonalPlanningDetails> updateList = new ArrayList<>();
        // 新增list
        List<SeasonalPlanningDetails> delList = new ArrayList<>();
        compareForUpdate(importDetailsList, seasonalPlanningDetailsList, addList, updateList, delList);
        for (SeasonalPlanningDetails addDetails : addList) {
            addDetails.setSeasonalPlanningId(seasonalPlanningSaveDto.getId());
        }
        seasonalPlanningDetailsService.saveBatch(addList);
        seasonalPlanningDetailsService.updateBatchById(updateList);
        seasonalPlanningDetailsService.updateBatchById(delList);

        return ApiResult.success("更新成功");
    }

    private void compareForUpdate(List<SeasonalPlanningDetails> importDetailsList, List<SeasonalPlanningDetails> oldDetailsList,
                                  List<SeasonalPlanningDetails> addList, List<SeasonalPlanningDetails> updateList, List<SeasonalPlanningDetails> delList) {
        Map<String, List<SeasonalPlanningDetails>> importDetailsMap = importDetailsList.stream().collect(Collectors.groupingBy(
                        SeasonalPlanningDetails::getProdCategoryName, // 品类分组
                        Collectors.toList()
                ));

        Map<String, List<SeasonalPlanningDetails>> oldDetailsMap = oldDetailsList.stream().collect(Collectors.groupingBy(
                SeasonalPlanningDetails::getProdCategoryName, // 品类分组
                Collectors.toList()
        ));

        for (String prodCategoryName : importDetailsMap.keySet()) {
            List<SeasonalPlanningDetails> oldSeasonalList = oldDetailsMap.get(prodCategoryName);
            List<SeasonalPlanningDetails> importSeasonalList = importDetailsMap.get(prodCategoryName);
            if (CollectionUtils.isEmpty(oldSeasonalList)) {
                // old数据中不存在的品类 ----新增
                addList.addAll(importSeasonalList);
            } else {
                // old数据存在的品类 ----更新
                if (StringUtils.isBlank(importSeasonalList.get(0).getProdCategory2ndName())) {
                    // 中类为空
                    // 1.判断历史数据是否为空
                    // 2.不为空则删除历史数据、更新数据
                    // 3.为空，则更新
                    if (StringUtils.isNotBlank(oldSeasonalList.get(0).getProdCategory2ndName())) {
                        List<SeasonalPlanningDetails> delDetalsList = oldSeasonalList.stream().map(s -> {
                            s.setDelFlag("0");
                            return s;
                        }).collect(Collectors.toList());
                        delList.addAll(delDetalsList);
                        addList.addAll(importSeasonalList);
                    } else {
                        copySeasonalPlanningDetails(importSeasonalList, oldSeasonalList);
                        updateList.addAll(oldSeasonalList);
                    }
                } else {
                    // 中类不为空
                    // 1.按中类分组
                    // 2.判断old中 中类是否为空
                    // 3.old 中类为空 删除old，更新
                    // 4.old 中类不为空，遍历比较更新
                    Map<String, List<SeasonalPlanningDetails>> importProdCategory2ndMap = importSeasonalList.stream().collect(Collectors.groupingBy(
                            SeasonalPlanningDetails::getProdCategory2ndCode, // 中类
                            Collectors.toList()));
                    if (StringUtils.isNotBlank(oldSeasonalList.get(0).getProdCategory2ndName())) {
                        Map<String, List<SeasonalPlanningDetails>> oldProdCategory2ndListMap = importSeasonalList.stream().collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getProdCategory2ndCode, // 中类
                                Collectors.toList()
                        ));
                        for (String improtProdCategory2nd : importProdCategory2ndMap.keySet()) {
                            List<SeasonalPlanningDetails> oldProdCategory2nds = oldProdCategory2ndListMap.get(improtProdCategory2nd);
                            List<SeasonalPlanningDetails> importProdCategory2nds = importProdCategory2ndMap.get(improtProdCategory2nd);
                            if (CollectionUtils.isEmpty(oldProdCategory2nds)) {
                                addList.addAll(importProdCategory2nds);
                            } else {
                                // 导入数据是否为 "0"
                                int sumOfValues = importProdCategory2nds.stream()
                                        .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                        .sum();
                                if (0 == sumOfValues) {
                                    List<SeasonalPlanningDetails> deloldProdCategory2ndsList = oldProdCategory2nds.stream().map(s -> {
                                        s.setDelFlag("0");
                                        return s;
                                    }).collect(Collectors.toList());
                                    delList.addAll(deloldProdCategory2ndsList);
                                } else {
                                    copySeasonalPlanningDetails(importProdCategory2nds, oldProdCategory2nds);
                                    updateList.addAll(oldProdCategory2nds);
                                }
                            }
                        }
                    } else {
                        List<SeasonalPlanningDetails> deloldoldSeasonalList = oldSeasonalList.stream().map(s -> {
                            s.setDelFlag("0");
                            return s;
                        }).collect(Collectors.toList());
                        delList.addAll(deloldoldSeasonalList);
                        addList.addAll(importSeasonalList);
                    }
                }
            }
        }
    }

    private void copySeasonalPlanningDetails(List<SeasonalPlanningDetails> newSeasonalList, List<SeasonalPlanningDetails> oldSeasonalList) {
        for (SeasonalPlanningDetails detail : oldSeasonalList) {
            SeasonalPlanningDetails newSl = newSeasonalList.stream().filter(ns -> StringUtils.equals(detail.getProdCategoryName(), ns.getProdCategoryName()) &&
                    StringUtils.equals(detail.getProdCategory2ndName(), ns.getProdCategory2ndName()) &&
                    StringUtils.equals(detail.getBandName(), ns.getBandName()) &&
                    StringUtils.equals(detail.getStyleCategory(), ns.getStyleCategory())).findFirst().orElse(null);
            if (null == newSl) {
                detail.setSkcCount("0");
            } else {
                detail.setSkcCount(newSl.getSkcCount());
            }

        }
    }

    private ApiResult buildImportExcelDetails(List<HashMap<Integer, String>> hashMaps, Map<Integer, SeasonalPlanningDetails> detailsMap, Map<String, String> prodCategoryMap,
                                      SeasonalPlanningSaveDto seasonalPlanningSaveDto, List<SeasonalPlanningDetails> detailsList) {
        ApiResult apiResult = new ApiResult();
        //大类名称
        String prodCategory1stName = null;
        //品类名称
        String prodCategoryName = null;
        //中类名称
        String prodCategory2ndName = null;
        //获取字典依赖管理的配置
        BasicDictDependsQueryDto basicDictDependsQueryDto  = new BasicDictDependsQueryDto();
        basicDictDependsQueryDto.setPageNum(1);
        basicDictDependsQueryDto.setPageSize(9999);
        basicDictDependsQueryDto.setDictTypeName("月份");
        List<BasicDictDepend> dictDependsList = ccmFeignService.getDictDependsList(basicDictDependsQueryDto);
        Map<String, BasicDictDepend> dictDependsMap = dictDependsList.stream().collect(Collectors.toMap(map->map.getDictCode()+map.getDependCode(), Function.identity()));
        List<BasicBaseDict> c8Band = ccmFeignService.getDictInfoToList("C8_Band");
        Map<String, String> bandMap = c8Band.stream().collect(Collectors.toMap(BasicBaseDict::getName, BasicBaseDict::getValue));

        // 处理表格数据
        for (int i = 0; i < hashMaps.size(); i++) {
            HashMap<Integer, String> integerStringHashMap = hashMaps.get(i);
            int index = 0;
            for (int j = 0; j < integerStringHashMap.size(); j++) {
                switch (i) {
                    case 0:
                        if (j == 0) {
                            seasonalPlanningSaveDto.setName(integerStringHashMap.get(j));
                        }
                        break;
                    case 1: //上市波段
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                String seasonCode = seasonalPlanningSaveDto.getSeasonCode();
                                String s1 = integerStringHashMap.get(j).split("")[0];
                                if (Integer.parseInt(s1) <10){
                                    s1 ="0"+s1;
                                }
                                BasicDictDepend basicDictDepend = dictDependsMap.get(s1 + seasonCode);
                                if (null == basicDictDepend) {
                                    return ApiResult.error("波段:" + integerStringHashMap.get(j) + " 所属月份与季节:"+ seasonCode + " 不匹配", 500);
                                }
                                orDefault.setBandName(integerStringHashMap.get(j));
                                index = j;
                            } else {
                                orDefault.setBandName(integerStringHashMap.get(index));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    case 2: //下单时间
                        if (i > 0 && j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setOrderTime(integerStringHashMap.get(j));
                                index = j;
                            } else {
                                orDefault.setOrderTime(integerStringHashMap.get(index));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    case 3: //上市时间
                        if (i > 0 && j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setLaunchTime(integerStringHashMap.get(j));
                                index = j;
                            } else {
                                orDefault.setLaunchTime(integerStringHashMap.get(index));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    case 4: //款式类别
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setStyleCategory(integerStringHashMap.get(j));
                                index = j;
                            } else {
                                orDefault.setStyleCategory(integerStringHashMap.get(index));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    default:
                        //数据列
                        SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                        if (j == 0) {
                            //大类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategory1stName = integerStringHashMap.get(j);
                                //大类有值之后重置，品类和中类
                                prodCategoryName = null;
                                prodCategory2ndName = null;
                            }
                        } else if (j == 1) {
                            //品类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategoryName = integerStringHashMap.get(j);
                                // TODO 品类校验(是否以生成企划)
                                apiResult = getDimensionalityList(prodCategoryMap, seasonalPlanningSaveDto, prodCategory1stName, prodCategoryName);
                                if (!apiResult.getSuccess()) {
                                    return apiResult;
                                }
                            }
                        } else if ( j == 2) {
                            //中类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategory2ndName = integerStringHashMap.get(j);
                            }
                            apiResult = checkCategory(prodCategoryMap, prodCategory1stName, prodCategoryName, prodCategory2ndName);
                            if (!apiResult.getSuccess()) {
                                return apiResult;
                            }
                        } else{
                            if (!integerStringHashMap.get(j).matches(pattern)) {
                                return ApiResult.error(integerStringHashMap.get(j) + " 解析异常，请输入正整数", 500);
                            }
                            SeasonalPlanningDetails details = new SeasonalPlanningDetails();
                            details.setProdCategory1stName(prodCategory1stName);
                            details.setProdCategoryName(prodCategoryName);
                            details.setProdCategory2ndName(prodCategory2ndName);
                            if (prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName + "_" + prodCategory2ndName)) {
                                String[] s = prodCategoryMap.get(prodCategory1stName + "_" + prodCategoryName + "_" + prodCategory2ndName).split("_");
                                details.setProdCategory1stCode(s[0]);
                                details.setProdCategoryCode(s[1]);
                                details.setProdCategory2ndCode(s[2]);
                            } else if (prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName)) {
                                String[] s = prodCategoryMap.get(prodCategory1stName + "_" + prodCategoryName).split("_");
                                details.setProdCategory1stCode(s[0]);
                                details.setProdCategoryCode(s[1]);
                            }
                            details.setBandName(orDefault.getBandName());
                            details.setBandCode(bandMap.get(details.getBandName()));
                            details.setOrderTime(orDefault.getOrderTime());
                            details.setLaunchTime(orDefault.getLaunchTime());
                            details.setStyleCategory(orDefault.getStyleCategory());
                            details.setSeasonalPlanningName(seasonalPlanningSaveDto.getName());
                            details.setSkcCount(integerStringHashMap.get(j));
                            details.setColumnIndex(String.valueOf(j));
                            detailsList.add(details);
                        }
                }
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult getSeasonalPlanningDetails(SeasonalPlanningDetails seasonalPlanningDetails) {
        ApiResult apiResult = new ApiResult<>();
        SeasonalPlanningDetailVO planningDetailVO = new SeasonalPlanningDetailVO();
        QueryWrapper<SeasonalPlanningDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seasonal_planning_id", seasonalPlanningDetails.getId());
        queryWrapper.orderBy(true, true, "band_name");
        SeasonalPlanning seasonalPlanning = this.getById(seasonalPlanningDetails.getId());
        String channel = StringUtils.isBlank(seasonalPlanning.getChannelName()) ? "" : seasonalPlanning.getChannelName();
        String lableName = seasonalPlanning.getSeasonName() + channel + "-季节企划";

        // 查询总需求列表
        List<SeasonalPlanningDetails> seasonalPlanningDetailsList = seasonalPlanningDetailsService.list(queryWrapper);

        // 查询实际下单
        QueryOrderDetailDTO dto = new QueryOrderDetailDTO();
        dto.setSeasonId(seasonalPlanning.getSeasonId());
        // t_field_val 表款式详情 code
        dto.setStyleCategory("StyleCategory");
        List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos = orderBookDetailService.querySeasonalPlanningOrder(dto);

        Map<String, Map<String, String>> demandMap = new HashMap<>();
        Map<String, Map<String, String>> orderMap = new HashMap<>();
        buildDemandAndOrderMap(demandMap, orderMap,seasonalPlanningDetailsList, orderBookDetailVos, lableName);
        // 排序
        Map<String, Map<String, String>> sortDemandMap = sort1(demandMap);
        Map<String, Map<String, String>> sortOrderMap = sort1(orderMap);


        // 计算总计行
        sumSumMap(sortDemandMap);
        sumSumMap(sortOrderMap);

        // 缺口下单
        Map<String, Map<String, String>> gapMap = buildGapExcel(sortDemandMap, sortOrderMap);

        // resultMap
        Map<String, Map<String, String>> resultMap = buildResultMap(sortDemandMap, sortOrderMap, gapMap);

        apiResult.setData(resultMap);
        apiResult.setSuccess(true);
        return apiResult;
    }

    private Map<String, Map<String, String>> buildResultMap(Map<String, Map<String, String>> demandMap, Map<String, Map<String, String>> orderMap, Map<String, Map<String, String>> gapMap) {
        Map<String, Map<String, String>> resultMapMap = new LinkedHashMap<>();
        for (String mapKey : demandMap.keySet()) {
            Map<String, String> resultMap = new HashMap<>();
            Map<String, String> mapMap = demandMap.get(mapKey);
            Map<String, String> orderMapMap = orderMap.get(mapKey);
            Map<String, String> gapMapMap = gapMap.get(mapKey);
            Integer n = mapMap.size() ;
            Integer n2 = mapMap.size() + orderMapMap.size() - 3;
            sort(mapMap);
            Integer i = 1;
            Integer j = 1;
            for (String mapMapKey : mapMap.keySet()) {

                resultMap.put(mapMapKey, mapMap.get(mapMapKey));
                if (i > 3) {
                    Integer orderColumn = n + j;
                    Integer gapColumn = n2 + j;
                    resultMap.put(COLUMN + formatString(orderColumn) + orderColumn, orderMapMap.get(mapMapKey));
                    resultMap.put(COLUMN + formatString(gapColumn) + gapColumn, gapMapMap.get(mapMapKey));
                    j ++;
                }
                i ++;
            }
            resultMapMap.put(mapKey, sort(resultMap));
        }
        return resultMapMap;
    }

    // 总合计
    private void sumSumMap(Map<String, Map<String, String>> mapMap) {
        sort1(mapMap);
        List<Map<String, String>> keyList = new ArrayList<>();
        // 取到所有的合计行
        for (String mapKey : mapMap.keySet()) {
            Map<String, String> map = mapMap.get(mapKey);
            for (String mapMapKey : map.keySet()) {
                if (StringUtils.equals(COLUMN + "03", mapMapKey)) {
                    if (StringUtils.equals(map.get(mapMapKey), "合计")) {
                        keyList.add(map);
                    }
                }
            }
        }
        Map<String, String> sumRow = new LinkedHashMap<>();
        sumRow.put(COLUMN + "01", "合计");
        sumRow.put(COLUMN + "02", "合计");
        sumRow.put(COLUMN + "03", "合计");
        for (Map<String, String> sumMap : keyList) {
            Map<String, String> sortMap = sort(sumMap);
            int i = 1;
            for (String key : sortMap.keySet()) {
                if (i > 3) {
                    Integer sktCount = Integer.valueOf(sortMap.get(key));
                    Integer sum = StringUtils.isBlank(sumRow.get(key)) ? 0 : Integer.valueOf(sumRow.get(key));
                    sumRow.put(key, String.valueOf(sum + sktCount));
                }
                i++;
            }
        }
        int rowLine = mapMap.size();
        rowLine = rowLine + 1;
        mapMap.put(ROW + formatString(rowLine) + rowLine, sumRow);
        sort1(mapMap);
    }

    //
    private Integer groupOrderBookDetail(String prodCategoryName, String prodCategory2ndName, String bandName, String styleCategory,
                                         List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos) {
        String routineStyleCategory = "常规";
        List<OrderBookDetailForSeasonPlanningVO> filterOrderList = orderBookDetailVos.stream().filter(o -> StringUtils.isNotBlank(o.getProdCategory())).collect(Collectors.toList());
        Map<String, List<OrderBookDetailForSeasonPlanningVO>> groupByProdCate = filterOrderList.stream()
                .collect(Collectors.groupingBy(
                        OrderBookDetailForSeasonPlanningVO::getProdCategoryName, // 品类分组
                        Collectors.toList()
                ));
        List<OrderBookDetailForSeasonPlanningVO> prodCatefroryList = groupByProdCate.get(prodCategoryName);
        if (CollectionUtils.isNotEmpty(prodCatefroryList)) {
            List<OrderBookDetailForSeasonPlanningVO> orderDetailList = new ArrayList<>();
            // 如果中类为空只统计维度的
            if (StringUtils.isBlank(prodCategory2ndName)) {
                Map<String, List<OrderBookDetailForSeasonPlanningVO>> bandNameMap = prodCatefroryList.stream()
                        .collect(Collectors.groupingBy(
                                OrderBookDetailForSeasonPlanningVO::getBandName, // 品类分组
                                Collectors.toList()
                        ));
                List<OrderBookDetailForSeasonPlanningVO> bandNameList = bandNameMap.get(bandName);
                if (CollectionUtils.isNotEmpty(bandNameList)) {
                    Long size = bandNameList.stream().filter(od -> StringUtils.equals(styleCategory, od.getStyleCategoryName())).count();
                    return Math.toIntExact(size);
                }
            } else {
                Map<String, List<OrderBookDetailForSeasonPlanningVO>> prodCategory2ndMap = prodCatefroryList.stream()
                        .collect(Collectors.groupingBy(
                                OrderBookDetailForSeasonPlanningVO::getProdCategory2ndName, // 品类分组
                                Collectors.toList()
                        ));
                List<OrderBookDetailForSeasonPlanningVO> prodCategory2ndList = prodCategory2ndMap.get(bandName);
                if (CollectionUtils.isNotEmpty(prodCategory2ndMap.get(prodCategory2ndName))) {
                    Map<String, List<OrderBookDetailForSeasonPlanningVO>> bandNameMap = prodCatefroryList.stream()
                            .collect(Collectors.groupingBy(
                                    OrderBookDetailForSeasonPlanningVO::getBandName, // 品类分组
                                    Collectors.toList()
                            ));
                    List<OrderBookDetailForSeasonPlanningVO> bandNameList = bandNameMap.get(bandName);
                    if (CollectionUtils.isNotEmpty(bandNameList)) {
                        Long size = bandNameList.stream().filter(od -> StringUtils.equals(styleCategory, od.getStyleCategoryName())).count();
                        return Math.toIntExact(size);
                    }
                }
            }
        }
        return 0;
    }


    private void buildDemandAndOrderMap(Map<String, Map<String, String>> demandMap, Map<String, Map<String, String>> orderMap,
                                        List<SeasonalPlanningDetails> seasonalPlanningDetailsList, List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos,String lableName) {
        int rowNum = 6;
        int orderCount = 0;
        // 顺序列表记录列顺序
        Map<Integer, String> sortSumMap = new LinkedHashMap<>();

        // 前五行非数据行单独处理
        Map<String, String> row01 = new LinkedHashMap<>();
        Map<String, String> orderRow01 = new LinkedHashMap<>();
        Map<String, String> row02 = new LinkedHashMap<>();
        Map<String, String> row03 = new LinkedHashMap<>();
        Map<String, String> row04 = new LinkedHashMap<>();
        Map<String, String> row05 = new LinkedHashMap<>();

        // 数据行
        Map<String, List<SeasonalPlanningDetails>> groupByProdCate = seasonalPlanningDetailsList.stream()
                .collect(Collectors.groupingBy(
                        SeasonalPlanningDetails::getProdCategoryName, // 品类分组
                        Collectors.toList()
                ));

        for (String prodCategory : groupByProdCate.keySet()) {
            // 分组内最大行号
            Integer maxRowNum = 0;
            Map<String, String> sumRow = new LinkedHashMap<>();
            Map<String, String> orderSumRow = new LinkedHashMap<>();
            List<SeasonalPlanningDetails> prodCategoryList = groupByProdCate.get(prodCategory);

            Map<String, List<SeasonalPlanningDetails>> groupByProdCategory2nd = new LinkedHashMap<>();

            // 中类为空
            if (StringUtils.isBlank(prodCategoryList.get(0).getProdCategory2ndName())) {
                groupByProdCategory2nd.put("-", prodCategoryList);
            } else {
                groupByProdCategory2nd = prodCategoryList.stream()
                        .collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getProdCategory2ndName, // 中类类分组
                                Collectors.toList()
                        ));
            }
            for (String prodCategory2nd : groupByProdCategory2nd.keySet()) {
                Map<String, String> rowData = new LinkedHashMap<>();
                Map<String, String> orderRowData = new LinkedHashMap<>();
                int row = 4;
                List<SeasonalPlanningDetails> prodCategory2ndList = groupByProdCategory2nd.get(prodCategory2nd);
                Map<String, List<SeasonalPlanningDetails>> groupByBand = prodCategory2ndList.stream()
                        .collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getBandName, // 波段
                                Collectors.toList()
                        ));
                Map<String, Integer> columnSumMap = new LinkedHashMap<>();
                Map<String, Integer> orderColumnSumMap = new LinkedHashMap<>();
                for (String band : groupByBand.keySet()) {
                    List<SeasonalPlanningDetails> bandList = groupByBand.get(band);
                    Map<String, List<SeasonalPlanningDetails>> groupByStyleCategory = bandList.stream()
                            .collect(Collectors.groupingBy(
                                    SeasonalPlanningDetails::getStyleCategory, // 款式类别
                                    Collectors.toList()
                            ));
                    for (String styleCategory : groupByStyleCategory.keySet()) {
                        List<SeasonalPlanningDetails> styleCategoryList = groupByStyleCategory.get(styleCategory);
                        Integer index = 0;
                        for (SeasonalPlanningDetails details : styleCategoryList) {
                            // 前三列 大类-品类-中类
                            rowData.put(COLUMN + "01", details.getProdCategory1stName());
                            rowData.put(COLUMN + "02", prodCategory);
                            rowData.put(COLUMN + "03", prodCategory2nd);
                            orderRowData.put(COLUMN + "01", details.getProdCategory1stName());
                            orderRowData.put(COLUMN + "02", prodCategory);
                            orderRowData.put(COLUMN + "03", prodCategory2nd);
                            String columnNum = details.getColumnIndex();
                            if (StringUtils.isBlank(columnNum)) {
                                break;
                            }
                            index = Integer.valueOf(columnNum);
                            index = index + 1;
                            String n = "";
                            if (index < 10) {
                                n = "0";
                            }
                            rowData.put(COLUMN + n + index, details.getSkcCount());
                            Integer orderSize = 0;
                            if (CollectionUtils.isNotEmpty(orderBookDetailVos)) {
                                String prodCategory2ndStr = StringUtils.equals("-", prodCategory2nd) ? null : prodCategory2nd;
                                Integer size = groupOrderBookDetail(prodCategory, prodCategory2ndStr, band, styleCategory, orderBookDetailVos);
                                orderSize = size;
                                orderRowData.put(COLUMN + n + index, String.valueOf(orderSize));
                            } else {
                                orderRowData.put(COLUMN + n + index, "0");
                            }

                            row01.put(COLUMN + n + index, "总需求");
                            orderRow01.put(COLUMN + n + index, "实际下单");
                            row02.put(COLUMN + n + index, band);
                            row03.put(COLUMN + n + index, details.getOrderTime());
                            row04.put(COLUMN + n + index, details.getLaunchTime());
                            row05.put(COLUMN + n + index, styleCategory);

                            Integer sum = Integer.valueOf(details.getSkcCount());
                            Integer countRow = columnSumMap.get(styleCategory);
                            Integer countOrderRow = orderColumnSumMap.get(styleCategory);
                            if (null == countRow) {
                                countRow = 0;
                            }
                            if (null == countOrderRow) {
                                countOrderRow = 0;
                            }
                            columnSumMap.put(styleCategory, countRow + sum);
                            orderColumnSumMap.put(styleCategory, countOrderRow + orderSize);

                            // 品类-波段-类型 列的行数
                            rowNum = Integer.valueOf(details.getRowIndex());
                            if (rowNum > maxRowNum) {
                                maxRowNum = rowNum;
                            }
                            row++;
                        }
                    }
                }
                int rowSum = row;
                for (String styleCat : columnSumMap.keySet()) {
                    String s = "";
                    if (row < 10) {
                        s = "0";
                    }
                    row01.put(COLUMN + s + rowSum, "总需求");
                    orderRow01.put(COLUMN + s + rowSum, "实际下单");
                    row02.put(COLUMN + s + rowSum, "合计");
                    row03.put(COLUMN + s + rowSum, "-");
                    row04.put(COLUMN + s + rowSum, "-");
                    row05.put(COLUMN + s + rowSum, styleCat);
                    rowData.put(COLUMN + s + rowSum, String.valueOf(columnSumMap.get(styleCat)));
                    orderRowData.put(COLUMN + s + rowSum, String.valueOf(orderColumnSumMap.get(styleCat)));
                    sortSumMap.put(rowSum, styleCat);
                    rowSum++;
                }
                rowNum = rowNum + 1;
                if (rowNum < 10) {
                    demandMap.put(ROW + "0" + rowNum, sort(rowData));
                    orderMap.put(ROW + "0" + rowNum, sort(orderRowData));
                } else {
                    demandMap.put(ROW + rowNum, sort(rowData));
                    orderMap.put(ROW + rowNum, sort(orderRowData));
                }
            }

            // 品类款式合计
            Map<String, List<SeasonalPlanningDetails>> bandSumMap = prodCategoryList.stream()
                    .collect(Collectors.groupingBy(
                            SeasonalPlanningDetails::getBandName, // 波段
                            Collectors.toList()
                    ));
            String prodCategory1stName = null;
            String prodCategoryName = null;
            Map<String, Integer> sumSumColumnMap = new LinkedHashMap<>();
            Map<String, Integer> sumSumOrderColumnMap = new LinkedHashMap<>();
            for (String s : bandSumMap.keySet()) {
                List<SeasonalPlanningDetails> l  = bandSumMap.get(s);
                Map<String, List<SeasonalPlanningDetails>> sumStyleCa = l.stream()
                        .collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getStyleCategory, // 款式类别
                                Collectors.toList()
                        ));
                for (String ss : sumStyleCa.keySet()) {
                    List<SeasonalPlanningDetails> sl = sumStyleCa.get(ss);
                    SeasonalPlanningDetails seasonalPlanningDetails = sl.get(0);
                    prodCategory1stName = seasonalPlanningDetails.getProdCategory1stName();
                    prodCategoryName = seasonalPlanningDetails.getProdCategoryName();
                    int sum = sl.stream().mapToInt(sp -> StringUtils.isBlank(sp.getSkcCount()) ? 0 : Integer.valueOf(sp.getSkcCount())).sum();
                    Integer index = Integer.valueOf(seasonalPlanningDetails.getColumnIndex());
                    index = index + 1;
                    Integer orderSum = 0;
                    for (SeasonalPlanningDetails spd : sl) {
                        orderSum = orderSum + groupOrderBookDetail(prodCategory, spd.getProdCategory2ndName(), s, ss, orderBookDetailVos);
                    }

                    sumRow.put(COLUMN + formatString(index) + index, String.valueOf(sum));
                    orderSumRow.put(COLUMN + formatString(index) + index, String.valueOf(orderSum));
                    Integer countRow = sumSumColumnMap.get(seasonalPlanningDetails.getStyleCategory());
                    Integer orderCountRow = sumSumColumnMap.get(seasonalPlanningDetails.getStyleCategory());
                    if (null == countRow) {
                        countRow = 0;
                    }
                    if (null == orderCountRow) {
                        orderCountRow = 0;
                    }
                    sumSumColumnMap.put(seasonalPlanningDetails.getStyleCategory(), countRow + sum);
                    sumSumOrderColumnMap.put(seasonalPlanningDetails.getStyleCategory(), orderCountRow + orderSum);
                }
                sumRow.put(COLUMN + "01", prodCategory1stName);
                sumRow.put(COLUMN + "02", prodCategoryName);
                sumRow.put(COLUMN + "03", "合计");
                orderSumRow.put(COLUMN + "01", prodCategory1stName);
                orderSumRow.put(COLUMN + "02", prodCategoryName);
                orderSumRow.put(COLUMN + "03", "合计");
            }
            Integer groupMapRow = maxRowNum + 1;
            for (Integer size : sortSumMap.keySet()) {
                sumRow.put(COLUMN + formatString(size) + size, String.valueOf(sumSumColumnMap.get(sortSumMap.get(size))));
                orderSumRow.put(COLUMN + formatString(size) + size, String.valueOf(sumSumOrderColumnMap.get(sortSumMap.get(size))));
            }
            demandMap.put(ROW + formatString(groupMapRow) + groupMapRow + "-c", sort(sumRow));
            orderMap.put(ROW + formatString(groupMapRow) + groupMapRow + "-c", sort(orderSumRow));
        }
        for (int row = 1; row < 4; row++) {
            row01.put(COLUMN + "0" + row, lableName);
            row02.put(COLUMN + "0" + row, "上市波段");
            row03.put(COLUMN + "0" + row, "下单时间");
            row04.put(COLUMN + "0" + row, "上市时间");
            row05.put(COLUMN + "0" + row, "款式类别");
        }
        demandMap.put(ROW + "01", sort(row01));
        demandMap.put(ROW + "02", sort(row02));
        demandMap.put(ROW + "03", sort(row03));
        demandMap.put(ROW + "04", sort(row04));
        demandMap.put(ROW + "05", sort(row05));
        orderMap.put(ROW + "01", sort(orderRow01));
        orderMap.put(ROW + "02", sort(row02));
        orderMap.put(ROW + "03", sort(row03));
        orderMap.put(ROW + "04", sort(row04));
        orderMap.put(ROW + "05", sort(row05));
    }

    private String formatString(Integer index) {
        String n = "";
        if (index < 10) {
            n = "0";
        }
        return n;
    }

    private Map<String, Map<String, String>> buildGapExcel(Map<String, Map<String, String>> demandMap, Map<String, Map<String, String>> orderMap) {
        Map<String, Map<String, String>> gapMap = new LinkedHashMap<>();
        int row = 1;
        for (String key : demandMap.keySet()) {
            int column = 1;
            Map<String, String> columnMap = demandMap.get(key);
            Map<String, String> orderRowMap = orderMap.get(key);
            Map<String, String> orderColumnMap = new LinkedHashMap<>();
            for (String columnKey : columnMap.keySet()) {
                if (row < 6) {
                    if (row == 1 && column > 3) {
                        orderColumnMap.put(columnKey, "缺口下单");
                    } else {
                        orderColumnMap.put(columnKey, columnMap.get(columnKey));
                    }
                } else {
                    if (column < 4) {
                        orderColumnMap.put(columnKey, columnMap.get(columnKey));
                    } else {
                        Integer gap = Integer.valueOf(columnMap.get(columnKey)) - Integer.valueOf(orderRowMap.get(columnKey));
                        orderColumnMap.put(columnKey, String.valueOf(gap));
                    }
                }
                column++;
            }
            gapMap.put(key, orderColumnMap);
            row++;
        }

        return gapMap;
    }

    private Map<String, String> sort(Map<String, String> unsortedMap) {
        return unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // 合并函数，这里我们保留旧值（实际上在这个例子中不会用到）
                        LinkedHashMap::new // 使用LinkedHashMap来保持顺序
                ));
    }

    private Map<String, Map<String, String>> sort1(Map<String, Map<String, String>> unsortedMap) {
        return unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // 合并函数，这里我们保留旧值（实际上在这个例子中不会用到）
                        LinkedHashMap::new // 使用LinkedHashMap来保持顺序
                ));
    }

    private ApiResult checkParam(SeasonalPlanningSaveDto seasonalPlanningSaveDto) {
        if (null == seasonalPlanningSaveDto) {
            return ApiResult.error("产品季节参数为空", 500);
        }
        if (StringUtils.isBlank(seasonalPlanningSaveDto.getSeasonName())) {
            return ApiResult.error("产品季名称为空", 500);
        }
        if (StringUtils.isBlank(seasonalPlanningSaveDto.getSeasonCode())) {
            return ApiResult.error("产品季渠道为空", 500);
        }
        return ApiResult.success("校验通过");
    }

    private ApiResult checkCategory(Map<String, String> prodCategoryMap, String prodCategory1stName, String prodCategoryName, String prodCategory2ndName) {
        if (!prodCategoryMap.containsKey(prodCategory1stName)) {
            return ApiResult.error("大类名称输入错误,请检查后再导入", 500);
        }
        if (StringUtils.isNotBlank(prodCategoryName)) {
            if (!prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName)) {
                return ApiResult.error("品类名称输入错误,请检查后再导入", 500);
            }
        }
        if (StringUtils.isNotBlank(prodCategory2ndName)) {
            if (!prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName + "_" + prodCategory2ndName)) {
                return ApiResult.error("中类:" + prodCategory2ndName + " 输入错误，请检查后输入", 500);
            }
        }
        return ApiResult.success("校验通过");
    }

    // 品类校验 是否配置第一维度，是否生成品类企划
    private ApiResult getDimensionalityList(Map<String, String> prodCategoryMap, SeasonalPlanningSaveDto seasonalPlanningSaveDto, String prodCategory1stName, String prodCategoryName) {
        DimensionLabelsSearchDto dimensionLabelsSearchDto = new DimensionLabelsSearchDto();
        if (!prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName)) {
            return ApiResult.error("未查到品类code", 500);
        }
        String[] s = prodCategoryMap.get(prodCategory1stName + "_" + prodCategoryName).split("_");
        String prodCategory = s[1];
        QueryWrapper<PlanningDimensionality> planningDimensionalityQueryWrapper = new QueryWrapper<>();
        planningDimensionalityQueryWrapper.eq("channel", seasonalPlanningSaveDto.getChannelCode());
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonalPlanningSaveDto.getSeasonId());
        planningDimensionalityQueryWrapper.eq("prod_category", prodCategory);
        planningDimensionalityQueryWrapper.eq("dimensionality_grade", "1");
        List<PlanningDimensionality> planningDimensionalities = planningDimensionalityService.list(planningDimensionalityQueryWrapper);
        if (CollectionUtils.isEmpty(planningDimensionalities)) {
            return ApiResult.error("品类:" + prodCategoryName + "未配置第一维度数据", 500);
        }

        if (StringUtils.isNotBlank(seasonalPlanningSaveDto.getId())) {
            // 是否生成品类企划
            QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("seasonal_planning_id", seasonalPlanningSaveDto.getId());
            queryWrapper.eq("status", "0");
            List<CategoryPlanning> categoryPlannings = categoryPlanningService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(categoryPlannings)) {
                CategoryPlanning categoryPlanning = categoryPlannings.get(0);
                // 品类企划是否撤回
                QueryWrapper<CategoryPlanningDetails> detailQueryWrapper = new QueryWrapper<>();
                detailQueryWrapper.eq("seasonal_planning_id", seasonalPlanningSaveDto.getId());
                detailQueryWrapper.eq("category_planning_id", categoryPlanning.getId());
                detailQueryWrapper.eq("prod_category_code", prodCategory);
                List<CategoryPlanningDetails> categoryPlanningDetailss = categoryPlanningDetailsService.list(detailQueryWrapper);
                if (CollectionUtils.isNotEmpty(categoryPlanningDetailss)) {
                    if (!StringUtils.equals(categoryPlanningDetailss.get(0).getIsGenerate(), "2")) {
                        return ApiResult.error("品类:" + prodCategoryName + "已生成品类企划", 500);
                    }
                }
            }
        }
        return ApiResult.success("success");
    }

    private SeasonalPlanning saveSeasonalPlanning(SeasonalPlanningSaveDto seasonalPlanningSaveDto) {
        // 先去查有没有启用的
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", seasonalPlanningSaveDto.getCompanyCode());
        queryWrapper.eq("status", "0");
        queryWrapper.eq("season_id", seasonalPlanningSaveDto.getSeasonId());
        queryWrapper.eq("channel_code", seasonalPlanningSaveDto.getChannelCode());
        List<SeasonalPlanning> l = this.list(queryWrapper);
        // 如果存在有启用的企划，全部设置为停用
        if (CollectionUtils.isNotEmpty(l)) {
            List<SeasonalPlanning> stopSeasonalPlanning = l.stream().map(seasonalPlanning -> {
                seasonalPlanning.setStatus("1");
                return seasonalPlanning;
                }).collect(Collectors.toList());
            this.updateBatchById(stopSeasonalPlanning);
        }
        seasonalPlanningSaveDto.setStatus("0");
        this.save(seasonalPlanningSaveDto);
        return getById(seasonalPlanningSaveDto.getId());
    }

    @Override
    @Transactional
    public void importExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) throws IOException {
        List<HashMap<Integer, String>> hashMaps = EasyExcel.read(file.getInputStream()).headRowNumber(0).doReadAllSync();

        JSONArray jsonArray = new JSONArray();
        List<BasicBaseDict> c8Band = ccmFeignService.getDictInfoToList("C8_Band");
        BasicDictDependsQueryDto basicDictDependsQueryDto  = new BasicDictDependsQueryDto();
        basicDictDependsQueryDto.setPageNum(1);
        basicDictDependsQueryDto.setPageSize(9999);
        basicDictDependsQueryDto.setDictTypeName("月份");
        //获取字典依赖管理的配置
        List<BasicDictDepend> dictDependsList = ccmFeignService.getDictDependsList(basicDictDependsQueryDto);
        Map<String, BasicDictDepend> dictDependsMap = dictDependsList.stream().collect(Collectors.toMap(map->map.getDictCode()+map.getDependCode(), Function.identity()));

        List<String> bandNames = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        List<String> markets = new ArrayList<>();
        List<String> styleCategorieList = new ArrayList<>();
        List<String> sums = new ArrayList<>();
        List<HashMap<String, String>> hashMapList = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < hashMaps.size(); i++) {

            JSONObject jsonObject = new JSONObject();
            Set<Integer> keySet = hashMaps.get(i).keySet();
            for (Integer s : keySet) {
                jsonObject.put(String.valueOf(s), hashMaps.get(i).get(s) == null ? "" : hashMaps.get(i).get(s));
            }
            TreeSet<Integer> integerTreeSet = new TreeSet<>();
            TreeSet<Integer> orderTime = new TreeSet<>();
            TreeSet<Integer> listingTime = new TreeSet<>();
            TreeSet<Integer> styleCategories = new TreeSet<>();
            TreeSet<Integer> middleClassSet = new TreeSet<>();
            switch (i) {
                case 0:
                    // 标题
                    String name = jsonObject.getString("0");
                    seasonalPlanningSaveDto.setName(name);
                    break;
                case 1:
                    // 上市波段
                    int s =0;
                    //防止顺序错乱,不能用jsonObject.keySet()
                    Map<String, String> bandMap = new HashMap<>();
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("上市波段:" + jsonObject.getString(s));
                            String bandName = jsonObject.getString(String.valueOf(s));
                            if (bandMap.containsKey(bandName)){
                                throw new RuntimeException("上市波段重复:" + bandName);
                            }else {
                                bandMap.put(bandName,bandName);
                            }

                            String s1 = bandName.split("")[0];
                            if (Integer.parseInt(s1) <10){
                                s1 ="0"+s1;
                            }
                            String seasonCode = seasonalPlanningSaveDto.getSeasonCode();
                            BasicDictDepend basicDictDepend = dictDependsMap.get(  s1+seasonCode);

                            if (basicDictDepend==null){
                                throw new RuntimeException("波段"+bandName+"在字典依赖月份配置不存在");
                            }

                            bandNames.add(jsonObject.getString(String.valueOf(s)));
                            bandNames.add(jsonObject.getString(String.valueOf(s)));
                        }
                        integerTreeSet.add(s);
                        s++;
                    }

                    break;
                case 2:
                     s =0;
                    // 下单时间
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            orders.add(jsonObject.getString(String.valueOf(s)));
                            orders.add(jsonObject.getString(String.valueOf(s)));
                        }
                        orderTime.add(s);
                        s++;
                    }
                    break;
                case 3:
                    // 上市时间
                     s =0;
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            markets.add(jsonObject.getString(String.valueOf(s)));
                            markets.add(jsonObject.getString(String.valueOf(s)));
                        }
                        listingTime.add(s);
                        s++;
                    }
                    break;
                case 4:
                    // 款式类别
                     s=0;
                    while (jsonObject.containsKey(String.valueOf(s))){
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            styleCategorieList.add(jsonObject.getString(String.valueOf(s)));
                        }
                        styleCategories.add(s);
                        s++;
                    }
                    break;
                default:
                    List<BasicCategoryDot> categoryDotList = new ArrayList<>();
                    // int sum = 0;
                    s=0;

                    while (jsonObject.containsKey(String.valueOf(s))){

                        if (s == 0 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("大类:" + jsonObject.getString(s));

                            List<BasicCategoryDot> list = ccmFeignService.getCategorySByNameAndLevel("品类", jsonObject.getString(String.valueOf(s)), "0");
                            if (list != null && !list.isEmpty()) {
                                categoryDotList.add(list.get(0));
                            }else {
                                throw new RuntimeException("大类:"+jsonObject.getString(String.valueOf(s))+"不存在");
                            }
                        }
                        if (s == 1 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("品类:" + jsonObject.getString(s));
                            List<BasicCategoryDot> list = ccmFeignService.getCategorySByNameAndLevel("品类", jsonObject.getString(String.valueOf(s)), "1");
                            if (list != null && !list.isEmpty()) {
                                categoryDotList.add(list.get(0));
                            }else {
                                throw new RuntimeException("品类:"+jsonObject.getString(String.valueOf(s))+"不存在");
                            }

                        }
                        if (s == 2) {
                            // System.out.println("中类:" + jsonObject.getString(s));
                            List<BasicCategoryDot> list =new ArrayList<>();
                            if (StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))){
                                list  = ccmFeignService.getCategorySByNameAndLevel("品类", jsonObject.getString(String.valueOf(s)), "2");

                            }
                            if (list != null && !list.isEmpty()) {
                                categoryDotList.add(list.get(0));
                            }else {
                                if (!"\\".equals(jsonObject.getString(String.valueOf(s))) && !"/".equals(jsonObject.getString(String.valueOf(s)))){
                                    throw new RuntimeException("中类:"+jsonObject.getString(String.valueOf(s))+"不存在");

                                }
                            }
                        }
                        if (s > 2 && StringUtils.isNotBlank(jsonObject.getString(String.valueOf(s)))) {
                            // System.out.println("数量:" + jsonObject.getString(s));
                            // sum += Integer.parseInt(jsonObject.getString(s));
                            String s1 = jsonObject.getString(String.valueOf(s));

                            int i1 = 0;
                            try {
                                i1 = Integer.parseInt(s1);
                            } catch (NumberFormatException e) {
                                throw new RuntimeException("输入的数字不规范");
                            }
                            if (i1<0){
                                throw new RuntimeException("数量不能小于0");
                            }
                            sums.add(s1);
                        }
                        middleClassSet.add(s);
                        s++;
                    }

                    // System.out.println("SKC数量"+sum);


                    for (BasicCategoryDot basicCategoryDot : categoryDotList) {
                        // 每次都清掉中类信息
                        hashMap.put("中类名称", "");
                        hashMap.put("中类编码", "");
                        Integer level = basicCategoryDot.getLevel();
                        if (level == 0) {
                            hashMap.put("大类名称", basicCategoryDot.getName());
                            hashMap.put("大类编码", basicCategoryDot.getValue());
                        }
                        if (level == 1) {
                            hashMap.put("品类名称", basicCategoryDot.getName());
                            hashMap.put("品类编码", basicCategoryDot.getValue());
                        }
                        if (level == 2) {
                            hashMap.put("中类名称", basicCategoryDot.getName());
                            hashMap.put("中类编码", basicCategoryDot.getValue());
                        }
                    }
                    // System.out.println(hashMap);
                    HashMap<String, String> hashMap1 = new HashMap<>(hashMap);
                    hashMapList.add(hashMap1);
            }

            if (!integerTreeSet.isEmpty()) {
                jsonObject.put(String.valueOf(integerTreeSet.last() + 1), "合计");
                jsonObject.put(String.valueOf(integerTreeSet.last() + 2), "");
            }
            if (!orderTime.isEmpty()) {
                jsonObject.put(String.valueOf(orderTime.last() + 1), "-");
                jsonObject.put(String.valueOf(orderTime.last() + 2), "");
            }
            if (!listingTime.isEmpty()) {
                jsonObject.put(String.valueOf(listingTime.last() + 1), "-");
                jsonObject.put(String.valueOf(listingTime.last() + 2), "");
            }
            if (!styleCategories.isEmpty()) {
                jsonObject.put(String.valueOf(styleCategories.last() + 1), "常规");
                jsonObject.put(String.valueOf(styleCategories.last() + 2), "高奢");
            }
            if (!middleClassSet.isEmpty()) {
                int sum = 0;
                int sum1 = 0;
                for (String s : jsonObject.keySet()) {
                    int i1 = Integer.parseInt(s);
                    if (i1 > 2) {
                        // System.out.println(jsonObject.getString(s));
                        if (i1 % 2 == 0) {
                            sum1 += Integer.parseInt(jsonObject.getString(s));
                        } else {
                            sum += Integer.parseInt(jsonObject.getString(s));
                        }
                    }
                }

                jsonObject.put(String.valueOf(middleClassSet.last() + 1), sum);
                jsonObject.put(String.valueOf(middleClassSet.last() + 2), sum1);
            }
            jsonArray.add(jsonObject);
        }
        // System.out.println(band);
        // System.out.println(order);
        // System.out.println(market);

        // 竖排合计
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("2", "合计");
        for (int i = 0; i < jsonArray.size(); i++) {

            if (i > 4) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                for (String s : jsonObject.keySet()) {
                    int i1 = Integer.parseInt(s);
                    if (i1 > 2) {
                        int sum;
                        if (jsonObject2.getInteger(s) != null) {
                            sum = jsonObject.getInteger(s) + jsonObject2.getInteger(s);
                        } else {
                            sum = jsonObject.getInteger(s);
                        }
                        jsonObject2.put(s, sum);
                    }
                }

            }
        }
        jsonArray.add(jsonObject2);
        String dataJson = JSON.toJSONString(jsonArray, SerializerFeature.WriteMapNullValue);

        seasonalPlanningSaveDto.setDataJson(dataJson);

        // 先去查有没有启用的，如果有就将状态设为停用
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", seasonalPlanningSaveDto.getCompanyCode());
        queryWrapper.eq("status", "0");
        queryWrapper.eq("season_id", seasonalPlanningSaveDto.getSeasonId());
        queryWrapper.eq("channel_code", seasonalPlanningSaveDto.getChannelCode());
        long l = this.count(queryWrapper);
        seasonalPlanningSaveDto.setStatus(l > 0 ? "1" : "0");
        this.save(seasonalPlanningSaveDto);


        List<SeasonalPlanningDetails> detailsList = new ArrayList<>();
        // System.out.println(hashMapList);
        //如果品类相同,则只存一个,并且将中类内容用逗号分开相加
        List<HashMap<String, String> > hashMapList1 = new ArrayList<>();
        HashMap<String, HashMap<String,String>> hashMap1 =new HashMap<>();
        for (HashMap<String, String> hashMap2 : hashMapList) {
            String name =hashMap2.get("大类名称")+","+hashMap2.get("品类名称");
            if (hashMap1.containsKey(name)) {
                HashMap<String, String> stringStringHashMap = hashMap1.get(name);
                String prodCategoryCode2Name = stringStringHashMap.get("中类名称")+","+hashMap2.get("中类名称");
                String prodCategoryCode2Code = stringStringHashMap.get("中类编码")+","+hashMap2.get("中类编码");
                hashMap2.put("中类名称",prodCategoryCode2Name);
                hashMap2.put("中类编码",prodCategoryCode2Code);
                hashMap1.put(name,hashMap2);
            }else {
                hashMap1.put(name,hashMap2);
            }

        }

        for (String s : hashMap1.keySet()) {
            hashMapList1.add(hashMap1.get(s));
        }
        List<BasicStructureTreeVo> basicStructureTreeVos = ccmFeignService.basicStructureTreeByCode("品类", null, "0,1,2");
        Map<String, BasicStructureTreeVo> map = basicStructureTreeVos.stream().collect(Collectors.toMap(BasicStructureTreeVo::getValue, basicStructureTreeVo -> basicStructureTreeVo));
        for (HashMap<String, String> stringStringHashMap : hashMapList1) {
            SeasonalPlanningDetails seasonalPlanningDetails = new SeasonalPlanningDetails();
            BasicStructureTreeVo basicStructureTreeVo = map.get(stringStringHashMap.get("大类编码"));
            if (basicStructureTreeVo==null){
                throw new RuntimeException("大类"+stringStringHashMap.get("大类名称")+"不存在");
            }
            if (basicStructureTreeVo.getChildren() ==null || basicStructureTreeVo.getChildren().isEmpty()){
                throw new RuntimeException(stringStringHashMap.get("大类名称")+"下的品类"+ stringStringHashMap.get("品类名称")+"不存在");
            }

            Map<String, BasicStructureTreeVo> map1 = basicStructureTreeVo.getChildren().stream().collect(Collectors.toMap(BasicStructureTreeVo::getValue, basicStructureTreeVo1 -> basicStructureTreeVo1));
            BasicStructureTreeVo basicStructureTreeVo1 = map1.get(stringStringHashMap.get("品类编码"));
            if (basicStructureTreeVo1==null){
                throw new RuntimeException(stringStringHashMap.get("大类名称")+"下的品类"+ stringStringHashMap.get("品类名称")+"不存在");
            }

            if (!"\\".equals(stringStringHashMap.get("中类名称")) && !"/".equals(stringStringHashMap.get("中类名称"))){
                if (basicStructureTreeVo1.getChildren() ==null || basicStructureTreeVo1.getChildren().isEmpty()){
                    throw new RuntimeException(stringStringHashMap.get("品类名称")+"下的中类"+ stringStringHashMap.get("中类名称")+"不存在");
                }

                Map<String, BasicStructureTreeVo> map2 = basicStructureTreeVo1.getChildren().stream().collect(Collectors.toMap(BasicStructureTreeVo::getValue, basicStructureTreeVo2 -> basicStructureTreeVo2));

                for (String s : stringStringHashMap.get("中类编码").split(",")) {
                    BasicStructureTreeVo basicStructureTreeVo2 = map2.get(s);
                    if (basicStructureTreeVo2==null && StringUtils.isNotBlank(s)){
                        throw new RuntimeException(stringStringHashMap.get("品类名称")+"下的中类"+ stringStringHashMap.get("中类名称")+"不存在");
                    }
                }

            }

            seasonalPlanningDetails.setProdCategory1stCode(stringStringHashMap.get("大类编码"));
            seasonalPlanningDetails.setProdCategory1stName(stringStringHashMap.get("大类名称"));
            seasonalPlanningDetails.setProdCategory2ndCode(stringStringHashMap.get("中类编码"));
            seasonalPlanningDetails.setProdCategory2ndName(stringStringHashMap.get("中类名称"));
            seasonalPlanningDetails.setProdCategoryCode(stringStringHashMap.get("品类编码"));
            seasonalPlanningDetails.setProdCategoryName(stringStringHashMap.get("品类名称"));
            seasonalPlanningDetails.setBandName(String.join(",", bandNames));
            seasonalPlanningDetails.setStyleCategory(String.join(",", styleCategorieList));
            List<String> bandCodes = new ArrayList<>();
            Map<String, BasicBaseDict> dictMap = c8Band.stream().collect(Collectors.toMap(BasicBaseDict::getName, band -> band));
            for (String bandName : bandNames) {
                BasicBaseDict basicBaseDict = dictMap.get(bandName);
                if (basicBaseDict == null) {
                    throw new RuntimeException("找不到"+bandName+"的编码");
                }
                bandCodes.add(basicBaseDict.getValue());
            }

            seasonalPlanningDetails.setBandCode(String.join(",", bandCodes));
            seasonalPlanningDetails.setSeasonalPlanningId(seasonalPlanningSaveDto.getId());
            seasonalPlanningDetails.setSeasonalPlanningName(seasonalPlanningSaveDto.getName());
            //获取当前品类所有的数据
            List<JSONObject> jsonObjects=new ArrayList<>();
            String name = "";
            String prodCategoryName = stringStringHashMap.get("品类名称");
            for (int i = 4; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String string = jsonObject.getString("1");
                if (StringUtils.isNotBlank(string)) {
                    name = string;
                }
                if (name.equals(prodCategoryName) && !"合计".equals(jsonObject.getString("2"))) {
                    jsonObjects.add(jsonObject);
                }
            }
            //获取合计列索引
            int index = 0;
            JSONObject jsonObject1 = jsonArray.getJSONObject(1);
            for (String s : jsonObject1.keySet()) {
                if ("合计".equals(jsonObject1.get(s))){
                     index = Integer.parseInt(s);
                }
            }


            List<String> sums1 = new ArrayList<>();
            for (JSONObject jsonObject : jsonObjects) {
                for (String s : jsonObject.keySet()) {
                    int i = Integer.parseInt(s);
                    if (i>2 && !String.valueOf(index).equals(s) && !String.valueOf(index+1).equals(s)){
                        sums1.add(jsonObject.getString(s));
                    }
                }

            }
            seasonalPlanningDetails.setSkcCount(String.join(",",sums1));
            seasonalPlanningDetails.setLaunchTime(String.join(",", markets));
            seasonalPlanningDetails.setOrderTime(String.join(",", orders));

            detailsList.add(seasonalPlanningDetails);
        }
        seasonalPlanningDetailsService.saveBatch(detailsList);

        saveDetailsList(seasonalPlanningSaveDto, hashMaps, basicStructureTreeVos, c8Band);

        // seasonalPlanningDetails.setSkcCount(sum);
        // seasonalPlanningDetails.setSeasonalPlanningId();

    }

    private void saveDetailsList(SeasonalPlanningSaveDto seasonalPlanningSaveDto, List<HashMap<Integer, String>> hashMaps, List<BasicStructureTreeVo> basicStructureTreeVos, List<BasicBaseDict> c8Band) {
        Map<Integer, SeasonalPlanningDetails> detailsMap = new HashMap<>();
        List<SeasonalPlanningDetails> detailsList = new ArrayList<>();

        //大类名称
        String prodCategory1stName = null;
        //品类名称
        String prodCategoryName = null;
        //中类名称
        String prodCategory2ndName = null;

        Map<String, String> prodCategoryMap = new HashMap<>();
        for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVos) {
            //大类
            for (BasicStructureTreeVo child : basicStructureTreeVo.getChildren()) {
                //品类
                for (BasicStructureTreeVo childChild : child.getChildren()) {
                    //中类
                    prodCategoryMap.put(basicStructureTreeVo.getName() + "_" + child.getName() + "_" + childChild.getName(),
                            basicStructureTreeVo.getValue() + "_" + child.getValue() + "_" + childChild.getValue());
                }
                prodCategoryMap.put(basicStructureTreeVo.getName() + "_" + child.getName(),
                        basicStructureTreeVo.getValue() + "_" + child.getValue());
            }
        }
        Map<String, String> bandMap = c8Band.stream().collect(Collectors.toMap(BasicBaseDict::getName, BasicBaseDict::getValue));

        for (int i = 1; i < hashMaps.size(); i++) {
            HashMap<Integer, String> integerStringHashMap = hashMaps.get(i);
            for (int j = 0; j < integerStringHashMap.size(); j++) {
                switch (i) {
                    case 1: //上市波段
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setBandName(integerStringHashMap.get(j));
                            } else {
                                orDefault.setBandName(integerStringHashMap.get(j - 1));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    case 2: //下单时间
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setOrderTime(integerStringHashMap.get(j));
                            } else {
                                orDefault.setOrderTime(integerStringHashMap.get(j - 1));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    case 3: //上市时间
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setLaunchTime(integerStringHashMap.get(j));
                            } else {
                                orDefault.setLaunchTime(integerStringHashMap.get(j - 1));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    case 4: //款式类别
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                orDefault.setStyleCategory(integerStringHashMap.get(j));
                            } else {
                                orDefault.setStyleCategory(integerStringHashMap.get(j - 1));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    default:
                        //数据列
                        SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                        if (j == 0) {
                            //大类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategory1stName = integerStringHashMap.get(j);
                                //大类有值之后重置，品类和中类
                                prodCategoryName = null;
                                prodCategory2ndName = null;
                            }
                        } else if (j == 1) {
                            //品类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategoryName = integerStringHashMap.get(j);
                            }
                        } else if (j == 2) {
                            //中类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategory2ndName = integerStringHashMap.get(j);
                            }
                        } else {
                            SeasonalPlanningDetails details = new SeasonalPlanningDetails();
                            details.setProdCategory1stName(prodCategory1stName);
                            details.setProdCategoryName(prodCategoryName);
                            details.setProdCategory2ndName(prodCategory2ndName);
                            if (prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName + "_" + prodCategory2ndName)) {
                                String[] s = prodCategoryMap.get(prodCategory1stName + "_" + prodCategoryName + "_" + prodCategory2ndName).split("_");
                                details.setProdCategory1stCode(s[0]);
                                details.setProdCategoryCode(s[1]);
                                details.setProdCategory2ndCode(s[2]);
                            } else if (prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName)) {
                                String[] s = prodCategoryMap.get(prodCategory1stName + "_" + prodCategoryName).split("_");
                                details.setProdCategory1stCode(s[0]);
                                details.setProdCategoryCode(s[1]);
                            }
                            details.setBandName(orDefault.getBandName());
                            details.setBandCode(bandMap.get(details.getBandName()));
                            details.setOrderTime(orDefault.getOrderTime());
                            details.setLaunchTime(orDefault.getLaunchTime());
                            details.setStyleCategory(orDefault.getStyleCategory());
                            details.setSeasonalPlanningId(seasonalPlanningSaveDto.getName());
                            details.setSeasonalPlanningName(seasonalPlanningSaveDto.getId());
                            details.setSkcCount(integerStringHashMap.get(j));
                            detailsList.add(details);
                        }
                }
            }
        }
        seasonalPlanningDetailsService.saveBatch(detailsList);
    }

    @Override
    public List<SeasonalPlanningVo> queryList(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        BaseQueryWrapper<SeasonalPlanning> queryWrapper = this.buildQueryWrapper(seasonalPlanningQueryDto);
        return baseMapper.listByQueryWrapper(queryWrapper);
    }

    @Override
    public List<SeasonalPlanningVo> queryPage(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        PageHelper.startPage(seasonalPlanningQueryDto);
        return this.queryList(seasonalPlanningQueryDto);
    }

    @Override
    public SeasonalPlanningVo getDetailById(String id) {
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tsp.id",id);
        List<SeasonalPlanningVo> list = baseMapper.listByQueryWrapper(queryWrapper);
        SeasonalPlanningVo seasonalPlanningVo = list.get(0);
        String dataJson = this.addTotal(seasonalPlanningVo.getDataJson());
        seasonalPlanningVo.setDataJson(dataJson);
        List<SeasonalPlanningDetails> detailsList = seasonalPlanningDetailsService.listByField("seasonal_planning_id", seasonalPlanningVo.getId());

        //拆分成中类维度
        List<SeasonalPlanningDetails> list1 =new ArrayList<>();
        List<SeasonalPlanningDetails> list2 =new ArrayList<>();
        // List<SeasonalPlanningDetails> list3 =new ArrayList<>();
        for (SeasonalPlanningDetails seasonalPlanningDetails : detailsList) {
            String prodCategory2ndCode = seasonalPlanningDetails.getProdCategory2ndCode();
            if (StringUtils.isNotBlank(prodCategory2ndCode)&& prodCategory2ndCode.split(",").length>1) {
                String[] split = prodCategory2ndCode.split(",");
                for (int i = 0; i < split.length; i++) {
                    if (StringUtils.isNotBlank(split[i])){
                        SeasonalPlanningDetails seasonalPlanningDetails1 =new SeasonalPlanningDetails();
                        BeanUtil.copyProperties(seasonalPlanningDetails,seasonalPlanningDetails1);
                        seasonalPlanningDetails1.setProdCategory2ndCode(split[i]);
                        seasonalPlanningDetails1.setProdCategory2ndName(seasonalPlanningDetails.getProdCategory2ndName().split(",")[i]);
                        list1.add(seasonalPlanningDetails1);
                    }

                }
            } else  {
                list1.add(seasonalPlanningDetails);
            }
        }
        //拆分成波段维度
        for (SeasonalPlanningDetails seasonalPlanningDetails : list1) {
            String bandCode = seasonalPlanningDetails.getBandCode();
            if(StringUtils.isNotBlank(bandCode) && bandCode.split(",").length>1) {
                String[] split = bandCode.split(",");
                //去重
                // List<String> arrayList =  Arrays.stream(split).distinct().collect(Collectors.toList());
                List<String> arrayList = Arrays.asList(split);
                for (int i = 0; i < arrayList.size(); i++) {
                    SeasonalPlanningDetails seasonalPlanningDetails1 = new SeasonalPlanningDetails();
                    BeanUtil.copyProperties(seasonalPlanningDetails, seasonalPlanningDetails1);
                    seasonalPlanningDetails1.setBandCode(arrayList.get(i));
                    seasonalPlanningDetails1.setBandName(seasonalPlanningDetails.getBandName().split(",")[i]);
                    list2.add(seasonalPlanningDetails1);
                }
            }
        }
        for (int i = 0; i < list2.size(); i++) {
            SeasonalPlanningDetails seasonalPlanningDetails = list2.get(i);
            seasonalPlanningDetails.setOrderTime("");
            seasonalPlanningDetails.setSkcCount("0");
            String prodCategory2ndCode = seasonalPlanningDetails.getProdCategory2ndCode();
            String[] split = prodCategory2ndCode.split(",");
            for (String s : split) {
                prodCategory2ndCode = s;
            }

            String prodCategory1stCode = seasonalPlanningDetails.getProdCategory1stCode();
            String prodCategoryCode = seasonalPlanningDetails.getProdCategoryCode();
            String bandCode = seasonalPlanningDetails.getBandCode();

            //查询设计款号
            BaseQueryWrapper<Style> styleQueryWrapper = new BaseQueryWrapper<>();
            styleQueryWrapper.select("id");
            styleQueryWrapper.eq("planning_season_id",seasonalPlanningVo.getSeasonId());
            styleQueryWrapper.eq("prod_category1st",prodCategory1stCode);
            styleQueryWrapper.eq("prod_category",prodCategoryCode);
            // styleQueryWrapper.eq("band_code",bandCode);
            styleQueryWrapper.notEmptyEq("prod_category2nd",prodCategory2ndCode);
            List<Style> styles = styleService.list(styleQueryWrapper);
            List<String> styleIds = styles.stream().map(Style::getId).collect(Collectors.toList());

            if (styleIds.isEmpty()){
                continue;
            }
            //查询大货款号
            QueryWrapper<StyleColor> styleColorQueryWrapper = new QueryWrapper<>();
            styleColorQueryWrapper.select("id","style_no");
            styleColorQueryWrapper.eq("band_code",bandCode);
            styleColorQueryWrapper.in("style_id",styleIds);
            List<StyleColor> styleColors = styleColorService.list(styleColorQueryWrapper);
            if (styleColors.isEmpty()){
                continue;
            }

            // highLuxuryItems
            //大货款号有-,并且-往前三位是S,则是高奢款
            List<StyleColor>  styleColorList =new ArrayList<>();
            for (StyleColor styleColor : styleColors) {
                if (i%2!=0){
                    seasonalPlanningDetails.setStyleCategory("高奢");
                    int i1 = styleColor.getStyleNo().indexOf("-");
                    System.out.println(styleColor.getStyleNo());
                    System.out.println(styleColor.getStyleNo().charAt(styleColor.getStyleNo().length()-3));
                    if(i1==-1 && 'S' == styleColor.getStyleNo().charAt(styleColor.getStyleNo().length()-3)){
                        styleColorList.add(styleColor);
                    }
                }else {
                    styleColorList.add(styleColor);
                    seasonalPlanningDetails.setStyleCategory("常规");
                }
            }
            List<String> styleColorIds = styleColorList.stream().map(StyleColor::getId).collect(Collectors.toList());
            if (styleColorIds.isEmpty()){
                continue;
            }
            BaseQueryWrapper<OrderBookDetail> bookDetailBaseQueryWrapper = new BaseQueryWrapper<>();
            bookDetailBaseQueryWrapper.in("style_color_id",styleColorIds);
            bookDetailBaseQueryWrapper.eq("is_order","1");
            long l = orderBookDetailService.count(bookDetailBaseQueryWrapper);
            seasonalPlanningDetails.setSkcCount(String.valueOf(l));
        }
        seasonalPlanningVo.setSeasonalPlanningDetailsList(list2);
        return seasonalPlanningVo;
    }


    private BaseQueryWrapper<SeasonalPlanning> buildQueryWrapper(SeasonalPlanningQueryDto seasonalPlanningQueryDto) {
        BaseQueryWrapper<SeasonalPlanning> baseQueryWrapper = new BaseQueryWrapper<>();
        baseQueryWrapper.notEmptyEq("tsp.season_id", seasonalPlanningQueryDto.getSeasonId());
        baseQueryWrapper.notEmptyEq("tsp.channel_code", seasonalPlanningQueryDto.getChannelCode());
        baseQueryWrapper.notEmptyLike("tsp.season_name", seasonalPlanningQueryDto.getYearName());
        baseQueryWrapper.orderByDesc("tsp.id");
        return baseQueryWrapper;
    }

    /**
     * 增加合计
     */
    private String addTotal(String dataJson) {
        JSONArray jsonArray = JSON.parseArray(dataJson);
        JSONObject jsonObject1 = jsonArray.getJSONObject(jsonArray.size() - 1);
        jsonArray.remove(jsonArray.size()-1);
        JSONArray jsonArray1 =new JSONArray();

        JSONObject object =new JSONObject();
        boolean b =false;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (i<=4){
                jsonArray1.add(jsonObject);
                continue;
            }

            if (StringUtils.isNotBlank(jsonObject.getString("1"))){
                if (b){
                    object.put("2","合计");
                    jsonArray1.add(object);
                    object=new JSONObject();
                }
                b=true;
            }

            for (String s : jsonObject.keySet()) {
                int si= Integer.parseInt(s);
                if (si>2){
                    Integer integer = jsonObject.getInteger(s);
                    Integer integer1 = object.getInteger(s);
                    if (integer1==null){
                        object.put(s, integer);
                    } else {
                        object.put(s, integer + integer1);
                    }
                }
            }

            jsonArray1.add(jsonObject);
            if (i== (jsonArray.size())-1){
                object.put("2","合计");
                jsonArray1.add(object);
                object=new JSONObject();
            }
        }
        jsonArray1.add(jsonObject1);
        jsonObject1.put("0","合计");
        jsonObject1.put("1","合计");

        return jsonArray1.toJSONString();
    }
}
