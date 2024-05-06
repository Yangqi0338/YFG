package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicDictDepend;
import com.base.sbc.client.ccm.entity.BasicDictDependsQueryDto;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.BasicCategoryDot;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBrandSeason;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBrandSeasonService;
import com.base.sbc.module.common.dto.BaseDto;
import com.base.sbc.module.common.dto.RemoveDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.orderbook.dto.QueryOrderDetailDTO;
import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.orderbook.service.OrderBookDetailService;
import com.base.sbc.module.orderbook.vo.OrderBookDetailForSeasonPlanningVO;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final static String pattern = "[0-9]\\d*";

    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private SeasonalPlanningDetailsService seasonalPlanningDetailsService;
    @Autowired
    @Lazy
    private OrderBookDetailService orderBookDetailService;
    @Autowired
    private StyleColorService styleColorService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private CategoryPlanningService categoryPlanningService;
    @Autowired
    private CategoryPlanningDetailsService categoryPlanningDetailsService;
    @Autowired
    private PlanningSeasonService planningSeasonService;
    @Autowired
    private BasicsdatumBrandSeasonService basicsdatumBrandSeasonService;

    @Override
    public ApiResult importSeasonalPlanningExcel(MultipartFile file, SeasonalPlanningSaveDto seasonalPlanningSaveDto) {
        ApiResult result = checkParam(seasonalPlanningSaveDto);
        if (!result.getSuccess()) {
            return result;
        }

        // 先去查有没有启用的
        if (StringUtils.isBlank(seasonalPlanningSaveDto.getId())) {
            QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("company_code", seasonalPlanningSaveDto.getCompanyCode());
            queryWrapper.eq("status", "0");
            queryWrapper.eq("season_id", seasonalPlanningSaveDto.getSeasonId());
            queryWrapper.eq("channel_code", seasonalPlanningSaveDto.getChannelCode());
            long l = this.count(queryWrapper);
            if (0 < l) {
                return ApiResult.error("存在已启用的季节企划", 500);
            }
        }
        String name = seasonalPlanningSaveDto.getSeasonName() + "(" + seasonalPlanningSaveDto.getChannelName() + ")";
        seasonalPlanningSaveDto.setName(name);
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
            seasonalPlanningSaveDto.setStatus("0");
            this.save(seasonalPlanningSaveDto);
            SeasonalPlanning seasonalPlanning = getById(seasonalPlanningSaveDto.getId());

            for (SeasonalPlanningDetails details : importDetailsList) {
                details.setSeasonalPlanningId(seasonalPlanning.getId());
            }
            seasonalPlanningDetailsService.saveBatch(importDetailsList);
            return ApiResult.success("导入成功");
        }

        // 比较更新
        QueryWrapper<SeasonalPlanningDetails> detailsQueryWrapper = new QueryWrapper<>();
        detailsQueryWrapper.eq("seasonal_planning_id", seasonalPlanningSaveDto.getId());
        List<SeasonalPlanningDetails> seasonalPlanningDetailsList = seasonalPlanningDetailsService.list(detailsQueryWrapper);
        // 新增list
        List<SeasonalPlanningDetails> addList = new ArrayList<>();
        // 更新list
        List<SeasonalPlanningDetails> updateList = new ArrayList<>();
        // 新增list
        List<SeasonalPlanningDetails> delList = new ArrayList<>();
        compareForUpdate(importDetailsList, seasonalPlanningDetailsList, addList, updateList, delList);
        if (CollectionUtils.isEmpty(addList) && CollectionUtils.isEmpty(updateList) && CollectionUtils.isEmpty(delList)) {
            return ApiResult.error("无数据变更,无需提交更新", 500);
        }
        for (SeasonalPlanningDetails addDetails : addList) {
            addDetails.setSeasonalPlanningId(seasonalPlanningSaveDto.getId());
        }
        seasonalPlanningDetailsService.saveBatch(addList);
        seasonalPlanningDetailsService.updateBatchById(updateList);
        if (CollectionUtils.isNotEmpty(delList)) {
            List<String> ids = delList.stream().map(SeasonalPlanningDetails::getId).collect(Collectors.toList());
            seasonalPlanningDetailsService.removeByIds(ids);
        }

        // 是否生成品类企划
        QueryWrapper<CategoryPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seasonal_planning_id", seasonalPlanningSaveDto.getId());
        queryWrapper.eq("status", "0");
        List<CategoryPlanning> categoryPlannings = categoryPlanningService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(categoryPlannings)) {
            Map<String, List<SeasonalPlanningDetails>> importDetailsMap = importDetailsList.stream().collect(Collectors.groupingBy(
                    SeasonalPlanningDetails::getProdCategoryName, // 品类分组
                    Collectors.toList()
            ));
            Set<String> prdoCategory = importDetailsMap.keySet();
            QueryWrapper<SeasonalPlanningDetails> queryWrapperByProdCa = new QueryWrapper<>();
            queryWrapperByProdCa.eq("seasonal_planning_id", seasonalPlanningSaveDto.getId());
            queryWrapperByProdCa.in("prod_category_name", prdoCategory);
            List<SeasonalPlanningDetails> seasonalPlanningDetailsForUpdate = seasonalPlanningDetailsService.list(queryWrapperByProdCa);

            categoryPlanningDetailsService.updateBySeasonalPlanning(seasonalPlanningDetailsForUpdate, delList);
        }
        return ApiResult.success("更新成功");
    }

    private void compareForUpdate(List<SeasonalPlanningDetails> importDetailsList, List<SeasonalPlanningDetails> oldDetailsList,
                                  List<SeasonalPlanningDetails> addList, List<SeasonalPlanningDetails> updateList, List<SeasonalPlanningDetails> delList) {
        // 更新数据
        Map<String, List<SeasonalPlanningDetails>> importDetailsMap = importDetailsList.stream().collect(Collectors.groupingBy(
                        SeasonalPlanningDetails::getProdCategoryName,
                        Collectors.toList()
                ));
        // 旧数据
        Map<String, List<SeasonalPlanningDetails>> oldDetailsMap = oldDetailsList.stream().collect(Collectors.groupingBy(
                SeasonalPlanningDetails::getProdCategoryName,
                Collectors.toList()
        ));
        // 获取旧数据中的波段信息
        List<String> bandNameList = new ArrayList<>();
        for (String oldDetails : oldDetailsMap.keySet()) {
            List<SeasonalPlanningDetails> seasonalPlanningDetails = oldDetailsMap.get(oldDetails);
            Map<String, List<SeasonalPlanningDetails>> bandMap = seasonalPlanningDetails.stream().collect(Collectors.groupingBy(
                    SeasonalPlanningDetails::getBandName,
                    Collectors.toList()
            ));
            for (String bandName : bandMap.keySet()) {
                bandNameList.add(bandName);
            }
            break;
        }

        /*// 新增新品类，不允许新增波段
                Map<String, List<SeasonalPlanningDetails>> bandMap = importSeasonalList.stream().collect(Collectors.groupingBy(
                        SeasonalPlanningDetails::getBandName,
                        Collectors.toList()
                ));
                for (String bandName : bandMap.keySet()) {
                    if (!bandNameList.contains(bandName)) {
                        throw new RuntimeException("更新新的品类时，不允许新增波段");
                    }
                }*/
        for (String prodCategoryName : importDetailsMap.keySet()) {
            List<SeasonalPlanningDetails> oldSeasonalList = oldDetailsMap.get(prodCategoryName);
            List<SeasonalPlanningDetails> importSeasonalList = importDetailsMap.get(prodCategoryName);
            if (CollectionUtils.isEmpty(oldSeasonalList)) {
                // old数据中不存在的品类 ----新增

                // 导入数据是否为 "0"
                if (StringUtils.isBlank(importSeasonalList.get(0).getProdCategory2ndName())) {
                    int sumOfValues = importSeasonalList.stream()
                            .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                            .sum();
                    if (0 != sumOfValues) {
                        addList.addAll(importSeasonalList);
                    }
                } else {
                    Map<String, List<SeasonalPlanningDetails>> newProdCategoryMap = importSeasonalList.stream().collect(Collectors.groupingBy(
                            SeasonalPlanningDetails::getProdCategoryName, // 中类分组
                            Collectors.toList()
                    ));
                    for (String newProdCategory : newProdCategoryMap.keySet()) {
                        List<SeasonalPlanningDetails> newProdCategoryList = newProdCategoryMap.get(newProdCategory);
                        int sumOfValues = newProdCategoryList.stream()
                                .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                .sum();
                        if (0 != sumOfValues) {
                            addList.addAll(newProdCategoryList);
                        }
                    }
                }
            } else {
                // old数据存在的品类 ----更新
                if (StringUtils.isBlank(importSeasonalList.get(0).getProdCategory2ndName())) {
                    // 中类为空
                    // 1.判断历史数据是否为空
                    // 2.不为空则删除历史数据、更新数据
                    // 3.为空，则更新
                    if (StringUtils.isNotBlank(oldSeasonalList.get(0).getProdCategory2ndName())) {
                        List<SeasonalPlanningDetails> delDetalsList = oldSeasonalList.stream().map(s -> {
                            s.setDelFlag("1");
                            return s;
                        }).collect(Collectors.toList());
                        delList.addAll(delDetalsList);
                        // 导入数据是否为 "0"
                        int sumOfValues = importSeasonalList.stream()
                                .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                .sum();
                        if (0 != sumOfValues) {
                            addList.addAll(importSeasonalList);
                        }
                    } else {
                        // 导入数据是否为 "0"
                        int sumOfValues = importSeasonalList.stream()
                                .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                .sum();
                        if (0 == sumOfValues) {
                            delList.addAll(oldSeasonalList);
                        } else {
                            /*copySeasonalPlanningDetails(importSeasonalList, oldSeasonalList);
                            updateList.addAll(oldSeasonalList);*/
                            List<SeasonalPlanningDetails> delDetalsList = oldSeasonalList.stream().map(s -> {
                                s.setDelFlag("1");
                                return s;
                            }).collect(Collectors.toList());
                            delList.addAll(delDetalsList);
                            addList.addAll(importSeasonalList);
                        }
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
                        Map<String, List<SeasonalPlanningDetails>> oldProdCategory2ndListMap = oldSeasonalList.stream().collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getProdCategory2ndCode, // 中类
                                Collectors.toList()
                        ));
                        for (String improtProdCategory2nd : importProdCategory2ndMap.keySet()) {
                            List<SeasonalPlanningDetails> oldProdCategory2nds = oldProdCategory2ndListMap.get(improtProdCategory2nd);
                            List<SeasonalPlanningDetails> importProdCategory2nds = importProdCategory2ndMap.get(improtProdCategory2nd);
                            if (CollectionUtils.isEmpty(oldProdCategory2nds)) {
                                // 导入数据是否为 "0"
                                int sumOfValues = importProdCategory2nds.stream()
                                        .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                        .sum();
                                if (0 != sumOfValues) {
                                    addList.addAll(importProdCategory2nds);
                                }

                            } else {
                                // 导入数据是否为 "0"
                                int sumOfValues = importProdCategory2nds.stream()
                                        .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                        .sum();
                                if (0 == sumOfValues) {
                                    List<SeasonalPlanningDetails> deloldProdCategory2ndsList = oldProdCategory2nds.stream().map(s -> {
                                        s.setDelFlag("1");
                                        return s;
                                    }).collect(Collectors.toList());
                                    delList.addAll(deloldProdCategory2ndsList);
                                } else {
                                    /*copySeasonalPlanningDetails(importProdCategory2nds, oldProdCategory2nds);
                                    updateList.addAll(oldProdCategory2nds);*/
                                    List<SeasonalPlanningDetails> deloldProdCategory2ndsList = oldProdCategory2nds.stream().map(s -> {
                                        s.setDelFlag("1");
                                        return s;
                                    }).collect(Collectors.toList());
                                    delList.addAll(deloldProdCategory2ndsList);
                                    addList.addAll(importProdCategory2nds);
                                }
                            }
                        }
                    } else {
                        List<SeasonalPlanningDetails> deloldoldSeasonalList = oldSeasonalList.stream().map(s -> {
                            s.setDelFlag("1");
                            return s;
                        }).collect(Collectors.toList());
                        delList.addAll(deloldoldSeasonalList);
                        // 导入数据是否为 "0"
                        int sumOfValues = importSeasonalList.stream()
                                .mapToInt(s -> Integer.valueOf(s.getSkcCount()))
                                .sum();
                        if (0 != sumOfValues) {
                            addList.addAll(importSeasonalList);
                        }

                    }
                }
            }
        }
    }

    // 校验导入的数据是否为0
    private void sumImport() {}

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
        List<BasicBaseDict> styleCategoryList = ccmFeignService.getDictInfoToList("StyleCategory");
        Map<String, String> styleCategoryMap = styleCategoryList.stream().collect(Collectors.toMap(BasicBaseDict::getName, BasicBaseDict::getValue));
        List<BasicBaseDict> c8Band = ccmFeignService.getDictInfoToList("C8_Band");
        Map<String, String> bandMap = c8Band.stream().collect(Collectors.toMap(BasicBaseDict::getName, BasicBaseDict::getValue));
        List<String> months = getSeasonMonths(seasonalPlanningSaveDto.getSeasonId());

        // 处理表格数据
        for (int i = 1; i < hashMaps.size(); i++) {
            HashMap<Integer, String> integerStringHashMap = hashMaps.get(i);
            int index = 0;
            for (int j = 0; j < integerStringHashMap.size(); j++) {
                switch (i) {
                    case 1: //上市波段
                        if (j > 2) {
                            SeasonalPlanningDetails orDefault = detailsMap.getOrDefault(j, new SeasonalPlanningDetails());
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                String seasonCode = seasonalPlanningSaveDto.getSeasonCode();
                                String s1 = integerStringHashMap.get(j).split("")[0];
                                if (Integer.parseInt(s1) <10){
                                    s1 ="0"+s1;
                                }
                                if (!months.contains(s1)) {
                                    return ApiResult.error("波段:" + integerStringHashMap.get(j) + " 所属月份与季节:"+ seasonCode + " 不匹配", 500);
                                }
                                if (StringUtils.isEmpty(bandMap.get(integerStringHashMap.get(j)))) {
                                    return ApiResult.error("波段:" + integerStringHashMap.get(j) + " 不存在,请检查联系管理员-任佳威", 500);
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
                                if (StringUtils.isEmpty(styleCategoryMap.get(integerStringHashMap.get(j)))) {
                                    return ApiResult.error("款式类别:" + integerStringHashMap.get(j) + " 不存在，请联系管理员-任佳威", 500);
                                }
                                orDefault.setStyleCategory(integerStringHashMap.get(j));
                                index = j;
                            } else {
                                orDefault.setStyleCategory(integerStringHashMap.get(index));
                            }
                            detailsMap.put(j, orDefault);
                        }
                        break;
                    default:
                        //数据行 5~i
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
                                apiResult = getDimensionalityList(prodCategoryMap, seasonalPlanningSaveDto, prodCategory1stName, prodCategoryName);
                                if (!apiResult.getSuccess()) {
                                    return apiResult;
                                }
                            }
                        } else if ( j == 2) {
                            //中类名称
                            if (StrUtil.isNotEmpty(integerStringHashMap.get(j))) {
                                prodCategory2ndName = integerStringHashMap.get(j);
                                apiResult = checkCategory(prodCategoryMap, prodCategory1stName, prodCategoryName, prodCategory2ndName);
                                if (!apiResult.getSuccess()) {
                                    return apiResult;
                                }
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
                            details.setRowIndex(String.valueOf(i));
                            details.setColumnIndex(String.valueOf(j));
                            detailsList.add(details);
                        }
                }
            }
        }
        return ApiResult.success();
    }

    private List<String> getSeasonMonths(String planningSeasonId) {
        PlanningSeason planningSeason = planningSeasonService.getById(planningSeasonId);
        if (null == planningSeason) {
            throw new RuntimeException("未查到产品季");
        }
        QueryWrapper<BasicsdatumBrandSeason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand", planningSeason.getBrand());
        queryWrapper.eq("season", planningSeason.getSeason());
        List<BasicsdatumBrandSeason> brandSeasons = basicsdatumBrandSeasonService.list(queryWrapper);
        if (CollectionUtils.isEmpty(brandSeasons)) {
            return null;
        }
        return brandSeasons.stream().map(BasicsdatumBrandSeason::getMonth).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Map<Integer, String>> getSeasonalPlanningDetails(SeasonalPlanningDetails seasonalPlanningDetails) {
        ApiResult apiResult = new ApiResult<>();
        SeasonalPlanningDetailVO planningDetailVO = new SeasonalPlanningDetailVO();
        QueryWrapper<SeasonalPlanningDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seasonal_planning_id", seasonalPlanningDetails.getId());
        queryWrapper.eq("del_flag", "0");
        queryWrapper.orderBy(true, true, "band_name");
        SeasonalPlanning seasonalPlanning = this.getById(seasonalPlanningDetails.getId());
        String channel = StringUtils.isBlank(seasonalPlanning.getChannelName()) ? "" : seasonalPlanning.getChannelName();
        String lableName = seasonalPlanning.getName();

        // 查询总需求列表
        List<SeasonalPlanningDetails> seasonalPlanningDetailsList = seasonalPlanningDetailsService.list(queryWrapper);

        // 查询实际下单
        QueryOrderDetailDTO dto = new QueryOrderDetailDTO();
        dto.setSeasonId(seasonalPlanning.getSeasonId());
        // t_field_val 表款式详情 code
        dto.setStyleCategory("StyleCategory");
        List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos = orderBookDetailService.querySeasonalPlanningOrder(dto);

        Map<Integer, Map<Integer, String>> demandMap = new HashMap<>();
        Map<Integer, Map<Integer, String>> orderMap = new HashMap<>();
        buildDemandAndOrderMap(demandMap, orderMap,seasonalPlanningDetailsList, orderBookDetailVos, lableName);
        // 排序
        Map<Integer, Map<Integer, String>> sortDemandMap = sort1(demandMap);
        Map<Integer, Map<Integer, String>> sortOrderMap = sort1(orderMap);

        // 计算总计行
        sumSumMap(sortDemandMap);
        sumSumMap(sortOrderMap);

        // 缺口下单
        Map<Integer, Map<Integer, String>> gapMap = buildGapExcel(sortDemandMap, sortOrderMap);

        // resultMap
        Map<Integer, Map<Integer, String>> resultMap = buildResultMap(sortDemandMap, sortOrderMap, gapMap);

        return resultMap;
    }

    private Map<Integer, Map<Integer, String>> buildResultMap(Map<Integer, Map<Integer, String>> demandMap, Map<Integer, Map<Integer, String>> orderMap, Map<Integer, Map<Integer, String>> gapMap) {
        Map<Integer, Map<Integer, String>> resultMapMap = new LinkedHashMap<>();
        for (Integer mapKey : demandMap.keySet()) {
            Map<Integer, String> resultMap = new HashMap<>();
            Map<Integer, String> mapMap = demandMap.get(mapKey);
            Map<Integer, String> orderMapMap = orderMap.get(mapKey);
            Map<Integer, String> gapMapMap = gapMap.get(mapKey);
            Integer n = mapMap.size() ;
            Integer n2 = mapMap.size() + orderMapMap.size() - 3;
            sort(mapMap);
            Integer i = 1;
            Integer j = 1;
            for (Integer mapMapKey : mapMap.keySet()) {

                resultMap.put(mapMapKey, mapMap.get(mapMapKey));
                if (i > 3) {
                    Integer orderColumn = n + j;
                    Integer gapColumn = n2 + j;
                    resultMap.put(orderColumn, orderMapMap.get(mapMapKey));
                    resultMap.put(gapColumn, gapMapMap.get(mapMapKey));
                    j ++;
                }
                i ++;
            }
            resultMapMap.put(mapKey, sort(resultMap));
        }
        return resultMapMap;
    }

    // 总合计
    private void sumSumMap(Map<Integer, Map<Integer, String>> mapMap) {
        sort1(mapMap);
        List<Map<Integer, String>> keyList = new ArrayList<>();
        // 取到所有的合计行
        for (Integer mapKey : mapMap.keySet()) {
            Map<Integer, String> map = mapMap.get(mapKey);
            for (Integer mapMapKey : map.keySet()) {
                if (3 == mapMapKey) {
                    if (StringUtils.equals(map.get(mapMapKey), "合计")) {
                        keyList.add(map);
                    }
                }
            }
        }
        Map<Integer, String> sumRow = new LinkedHashMap<>();
        sumRow.put(1, "合计");
        sumRow.put(2, "合计");
        sumRow.put(3, "合计");
        for (Map<Integer, String> sumMap : keyList) {
            Map<Integer, String> sortMap = sort(sumMap);
            int i = 1;
            for (Integer key : sortMap.keySet()) {
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
        mapMap.put(rowLine, sumRow);
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
                                OrderBookDetailForSeasonPlanningVO::getBandName, // 波段分组
                                Collectors.toList()
                        ));
                List<OrderBookDetailForSeasonPlanningVO> bandNameList = bandNameMap.get(bandName);
                if (CollectionUtils.isNotEmpty(bandNameList)) {
                    Long size = bandNameList.stream().filter(od -> StringUtils.equals(styleCategory, od.getStyleName())).count();
                    return Math.toIntExact(size);
                }
            } else {
                Map<String, List<OrderBookDetailForSeasonPlanningVO>> prodCategory2ndMap = prodCatefroryList.stream()
                        .collect(Collectors.groupingBy(
                                OrderBookDetailForSeasonPlanningVO::getProdCategory2ndName, // 中类分组
                                Collectors.toList()
                        ));
                List<OrderBookDetailForSeasonPlanningVO> prodCategory2ndList = prodCategory2ndMap.get(bandName);
                if (CollectionUtils.isNotEmpty(prodCategory2ndMap.get(prodCategory2ndName))) {
                    Map<String, List<OrderBookDetailForSeasonPlanningVO>> bandNameMap = prodCategory2ndList.stream()
                            .collect(Collectors.groupingBy(
                                    OrderBookDetailForSeasonPlanningVO::getBandName, // 品类分组
                                    Collectors.toList()
                            ));
                    List<OrderBookDetailForSeasonPlanningVO> bandNameList = bandNameMap.get(bandName);
                    if (CollectionUtils.isNotEmpty(bandNameList)) {
                        Long size = bandNameList.stream().filter(od -> StringUtils.equals(styleCategory, od.getStyleName())).count();
                        return Math.toIntExact(size);
                    }
                }
            }
        }
        return 0;
    }

    private void buildDemandAndOrderMap(Map<Integer, Map<Integer, String>> demandMap, Map<Integer, Map<Integer, String>> orderMap,
                                        List<SeasonalPlanningDetails> seasonalPlanningDetailsList, List<OrderBookDetailForSeasonPlanningVO> orderBookDetailVos,String lableName) {
        int rowNum = 5;
        int orderCount = 0;
        // 顺序列表记录列顺序
        Map<Integer, String> sortSumMap = new LinkedHashMap<>();

        // 前五行非数据行单独处理
        Map<Integer, String> row01 = new LinkedHashMap<>();
        Map<Integer, String> orderRow01 = new LinkedHashMap<>();
        Map<Integer, String> row02 = new LinkedHashMap<>();
        Map<Integer, String> row03 = new LinkedHashMap<>();
        Map<Integer, String> row04 = new LinkedHashMap<>();
        Map<Integer, String> row05 = new LinkedHashMap<>();

        List<BasicStructureTreeVo> basicStructureTreeVos = ccmFeignService.basicStructureTreeByCode("品类", null, "0,1,2");
        Map<String, Integer> sortMap = new HashMap<>();
        for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVos) {
            //大类
            for (BasicStructureTreeVo child : basicStructureTreeVo.getChildren()) {
                //品类
                for (BasicStructureTreeVo childChild : child.getChildren()) {
                    //中类
                    sortMap.put(basicStructureTreeVo.getName() + "_" + child.getName() + "_" + childChild.getName(),
                            childChild.getSort());
                }
                sortMap.put(basicStructureTreeVo.getName() + "_" + child.getName(),
                        child.getSort());
            }
            sortMap.put(basicStructureTreeVo.getName(), basicStructureTreeVo.getSort());
        }

        // 数据行
        Map<String, List<SeasonalPlanningDetails>> groupByProdCategory1stNameMap = seasonalPlanningDetailsList.stream()
                .collect(Collectors.groupingBy(
                        SeasonalPlanningDetails::getProdCategory1stName, // 大类分组
                        Collectors.toList()
                ));
        Map<String, List<SeasonalPlanningDetails>> orderByProdCategory1stNameMap = new LinkedHashMap<>();
        // 大类排序
        Map<Integer, String> groupByProdCategory1stNameSort = new HashMap<>();
        for (String prodCategory1stName : groupByProdCategory1stNameMap.keySet()) {
            Integer sort = null == sortMap.get(prodCategory1stName) ? 0 : sortMap.get(prodCategory1stName);
            groupByProdCategory1stNameSort.put(sort, prodCategory1stName);
        }
        Map<Integer, String> orderByProdCategory1stName = sort(groupByProdCategory1stNameSort);
        if (orderByProdCategory1stName.size() == groupByProdCategory1stNameMap.size()) {
            for (Integer sort : orderByProdCategory1stName.keySet()) {
                String prodCategory1stName = orderByProdCategory1stName.get(sort);
                orderByProdCategory1stNameMap.put(prodCategory1stName, groupByProdCategory1stNameMap.get(prodCategory1stName));
            }
        } else {
            orderByProdCategory1stNameMap.putAll(groupByProdCategory1stNameMap);
        }

        Map<String, List<SeasonalPlanningDetails>> orderByProdCategoryNameMap = new LinkedHashMap<>();
        for (String prodCategory1stName : orderByProdCategory1stNameMap.keySet()) {
            List<SeasonalPlanningDetails> prodCategory1stNameList = groupByProdCategory1stNameMap.get(prodCategory1stName);
            Map<String, List<SeasonalPlanningDetails>> groupByProdCategoryNameMap = prodCategory1stNameList.stream()
                    .collect(Collectors.groupingBy(
                            SeasonalPlanningDetails::getProdCategoryName, // 品类分组
                            Collectors.toList()
                    ));

            // 品类排序
            Map<Integer, String> groupByProdCategoryNameSort = new HashMap<>();
            for (String prodCategoryName : groupByProdCategoryNameMap.keySet()) {
                Integer sort = null == sortMap.get(prodCategory1stName + "_" + prodCategoryName) ? 0 : sortMap.get(prodCategory1stName);
                groupByProdCategoryNameSort.put(sort, prodCategoryName);
            }
            if (groupByProdCategoryNameMap.size() == groupByProdCategoryNameSort.size()) {
                Map<Integer, String> orderByProdCategoryName = sort(groupByProdCategoryNameSort);
                for (Integer sort : orderByProdCategoryName.keySet()) {
                    String prodCategoryName = orderByProdCategoryName.get(sort);
                    orderByProdCategoryNameMap.put(prodCategoryName, groupByProdCategoryNameMap.get(prodCategoryName));
                }
            } else {
                orderByProdCategoryNameMap.putAll(groupByProdCategoryNameMap);
            }
        }

        for (String prodCategory : orderByProdCategoryNameMap.keySet()) {
            Map<Integer, String> sumRow = new LinkedHashMap<>();
            Map<Integer, String> orderSumRow = new LinkedHashMap<>();
            List<SeasonalPlanningDetails> prodCategoryList = orderByProdCategoryNameMap.get(prodCategory);

            Map<String, List<SeasonalPlanningDetails>> groupByProdCategory2nd = new LinkedHashMap<>();
            // 中类为空
            if (StringUtils.isBlank(prodCategoryList.get(0).getProdCategory2ndName())) {
                groupByProdCategory2nd.put("-", prodCategoryList);
            } else {
                Map<String, List<SeasonalPlanningDetails>> groupByProdCategory2ndMap = prodCategoryList.stream()
                        .collect(Collectors.groupingBy(
                                SeasonalPlanningDetails::getProdCategory2ndName, // 中类类分组
                                Collectors.toList()
                        ));
                // 中类排序
                Map<Integer, String> groupByProdCategory2ndNameSort = new HashMap<>();
                for (String prodCategory2ndName : groupByProdCategory2ndMap.keySet()) {
                    String prodCategory1stName = groupByProdCategory2ndMap.get(prodCategory2ndName).get(0).getProdCategory1stName();
                    Integer sort = null == sortMap.get(prodCategory1stName + "_" + prodCategory + "_" + prodCategory2ndName) ?
                            0 : sortMap.get(prodCategory1stName + "_" + prodCategory + "_" + prodCategory2ndName);
                    groupByProdCategory2ndNameSort.put(sort, prodCategory2ndName);
                }
                if (groupByProdCategory2ndNameSort.size() == groupByProdCategory2ndMap.size()) {
                    Map<Integer, String> orderByProdCategory2ndName = sort(groupByProdCategory2ndNameSort);
                    for (Integer sort : orderByProdCategory2ndName.keySet()) {
                        String prodCategory2ndName = orderByProdCategory2ndName.get(sort);
                        groupByProdCategory2nd.put(prodCategory2ndName, groupByProdCategory2ndMap.get(prodCategory2ndName));
                    }
                } else {
                    groupByProdCategory2nd.putAll(groupByProdCategory2ndMap);
                }
            }
            for (String prodCategory2nd : groupByProdCategory2nd.keySet()) {
                Map<Integer, String> rowData = new LinkedHashMap<>();
                Map<Integer, String> orderRowData = new LinkedHashMap<>();
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
                            rowData.put(1, details.getProdCategory1stName());
                            rowData.put(2, prodCategory);
                            rowData.put(3, prodCategory2nd);
                            orderRowData.put(1, details.getProdCategory1stName());
                            orderRowData.put(2, prodCategory);
                            orderRowData.put(3, prodCategory2nd);
                            String columnNum = details.getColumnIndex();
                            if (StringUtils.isBlank(columnNum)) {
                                break;
                            }
                            index = Integer.valueOf(columnNum);
                            index = index + 1;
                            rowData.put(index, details.getSkcCount());
                            Integer orderSize = 0;
                            if (CollectionUtils.isNotEmpty(orderBookDetailVos)) {
                                String prodCategory2ndStr = StringUtils.equals("-", prodCategory2nd) ? null : prodCategory2nd;
                                Integer size = groupOrderBookDetail(prodCategory, prodCategory2ndStr, band, styleCategory, orderBookDetailVos);
                                orderSize = size;
                                orderRowData.put(index, String.valueOf(orderSize));
                            } else {
                                orderRowData.put(index, "0");
                            }

                            row01.put(index, "总需求");
                            orderRow01.put(index, "实际下单");
                            row02.put(index, band);
                            row03.put(index, details.getOrderTime());
                            row04.put(index, details.getLaunchTime());
                            row05.put(index, styleCategory);

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
                            row++;
                        }
                    }
                }
                int rowSum = row;
                for (String styleCat : columnSumMap.keySet()) {
                    row01.put(rowSum, "总需求");
                    orderRow01.put(rowSum, "实际下单");
                    row02.put(rowSum, "合计");
                    row03.put(rowSum, "-");
                    row04.put(rowSum, "-");
                    row05.put(rowSum, styleCat);
                    rowData.put(rowSum, String.valueOf(columnSumMap.get(styleCat)));
                    orderRowData.put(rowSum, String.valueOf(orderColumnSumMap.get(styleCat)));
                    sortSumMap.put(rowSum, styleCat);
                    rowSum++;
                }
                // 行数 + 1
                rowNum++;
                demandMap.put(rowNum, sort(rowData));
                orderMap.put(rowNum, sort(orderRowData));
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

                    sumRow.put(index, String.valueOf(sum));
                    orderSumRow.put(index, String.valueOf(orderSum));
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
                sumRow.put(1, prodCategory1stName);
                sumRow.put(2, prodCategoryName);
                sumRow.put(3, "合计");
                orderSumRow.put(1, prodCategory1stName);
                orderSumRow.put(2, prodCategoryName);
                orderSumRow.put(3, "合计");
            }
            rowNum++;
            for (Integer size : sortSumMap.keySet()) {
                sumRow.put(size, String.valueOf(sumSumColumnMap.get(sortSumMap.get(size))));
                orderSumRow.put(size, String.valueOf(sumSumOrderColumnMap.get(sortSumMap.get(size))));
            }
            demandMap.put(rowNum, sort(sumRow));
            orderMap.put(rowNum, sort(orderSumRow));

        }
        for (int row = 1; row < 4; row++) {
            row01.put(row, lableName);
            orderRow01.put(row, lableName);
            row02.put(row, "上市波段");
            row03.put(row, "下单时间");
            row04.put(row, "上市时间");
            row05.put(row, "款式类别");
        }
        demandMap.put(1, sort(row01));
        demandMap.put(2, sort(row02));
        demandMap.put(3, sort(row03));
        demandMap.put(4, sort(row04));
        demandMap.put(5, sort(row05));
        orderMap.put(1, sort(orderRow01));
        orderMap.put(2, sort(row02));
        orderMap.put(3, sort(row03));
        orderMap.put(4, sort(row04));
        orderMap.put(5, sort(row05));
    }

    private Map<Integer, Map<Integer, String>> buildGapExcel(Map<Integer, Map<Integer, String>> demandMap, Map<Integer, Map<Integer, String>> orderMap) {
        Map<Integer, Map<Integer, String>> gapMap = new LinkedHashMap<>();
        int row = 1;
        for (Integer key : demandMap.keySet()) {
            int column = 1;
            Map<Integer, String> columnMap = demandMap.get(key);
            Map<Integer, String> orderRowMap = orderMap.get(key);
            Map<Integer, String> orderColumnMap = new LinkedHashMap<>();
            for (Integer columnKey : columnMap.keySet()) {
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

    private Map<Integer, String> sort(Map<Integer, String> unsortedMap) {
        return unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // 合并函数，这里我们保留旧值（实际上在这个例子中不会用到）
                        LinkedHashMap::new // 使用LinkedHashMap来保持顺序
                ));
    }

    private Map<Integer, Map<Integer, String>> sort1(Map<Integer, Map<Integer, String>> unsortedMap) {
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
        if (StringUtils.isNotBlank(prodCategory2ndName)) {
            if (!prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName + "_" + prodCategory2ndName)) {
                return ApiResult.error("中类:" + prodCategory2ndName + " 输入错误，请检查后输入", 500);
            }
        }
        return ApiResult.success("校验通过");
    }

    // 品类校验 是否配置第一维度，是否生成品类企划
    private ApiResult getDimensionalityList(Map<String, String> prodCategoryMap, SeasonalPlanningSaveDto seasonalPlanningSaveDto, String prodCategory1stName, String prodCategoryName) {
        if (StringUtils.isBlank(prodCategory1stName)) {
            return ApiResult.error("请输入大类名称", 500);
        }
        if (!prodCategoryMap.containsKey(prodCategory1stName)) {
            return ApiResult.error("未找到大类: " + prodCategory1stName + "请检查名称是否输入有误", 500);
        }
        if (StringUtils.isBlank(prodCategoryName)) {
            return ApiResult.error("请输入品类名称", 500);
        }
        if (!prodCategoryMap.containsKey(prodCategory1stName + "_" + prodCategoryName)) {
            return ApiResult.error("未找到品类:" + prodCategoryName + " 请检查品类结构或品类名称是否输入正确", 500);
        }
        String[] s = prodCategoryMap.get(prodCategory1stName + "_" + prodCategoryName).split("_");
        String prodCategory = s[1];
        /*QueryWrapper<PlanningDimensionality> planningDimensionalityQueryWrapper = new QueryWrapper<>();
        planningDimensionalityQueryWrapper.eq("channel", seasonalPlanningSaveDto.getChannelCode());
        planningDimensionalityQueryWrapper.eq("planning_season_id", seasonalPlanningSaveDto.getSeasonId());
        planningDimensionalityQueryWrapper.eq("prod_category", prodCategory);
        planningDimensionalityQueryWrapper.eq("dimensionality_grade", "1");
        List<PlanningDimensionality> planningDimensionalities = planningDimensionalityService.list(planningDimensionalityQueryWrapper);
        if (CollectionUtils.isEmpty(planningDimensionalities)) {
            return ApiResult.error("品类:" + prodCategoryName + "未配置第一维度数据", 500);
        }*/

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
                        return ApiResult.error("品类:" + prodCategoryName + " 已生成品类企划, 如需更新请先至品类企划中撤回该品类企划", 500);
                    }
                }
            }
        }
        return ApiResult.success("success");
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

    @Override
    @Transactional
    public void updateStatus(BaseDto baseDto) {
        String ids = baseDto.getIds();
        if ("0".equals(baseDto.getStatus())){
            List<String> idList = Arrays.asList(ids.split(","));
            List<SeasonalPlanning> seasonalPlannings = this.listByIds(idList);
            List<String> seasonIds = seasonalPlannings.stream().map(SeasonalPlanning::getSeasonId).collect(Collectors.toList());
            List<String> channelCodes = seasonalPlannings.stream().map(SeasonalPlanning::getChannelCode).collect(Collectors.toList());

            QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("season_id", seasonIds);
            queryWrapper.in("channel_code", channelCodes);
            queryWrapper.notIn("id",idList);
            queryWrapper.eq("status","0");
            long l = this.count(queryWrapper);
            if (l > 0){
                throw new RuntimeException("已存在启用的季节企划");
            }
        }
        UpdateWrapper<SeasonalPlanning> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", baseDto.getStatus());
        updateWrapper.in("id", Arrays.asList(ids.split(",")));
        Boolean updateFlag = this.update(updateWrapper);
        if (!updateFlag) {
            throw new OtherException("季节企划更新失败，请刷新后重试！");
        }
        // 同步更新品类企划及企划看板
        QueryWrapper<CategoryPlanning> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("seasonal_planning_id",ids);
        categoryQueryWrapper.orderBy(true, false, "create_date");
        List<CategoryPlanning> categoryPlannings = categoryPlanningService.list(categoryQueryWrapper);
        if (CollectionUtils.isEmpty(categoryPlannings)) {
            return;
        }
        if ("0".equals(baseDto.getStatus())) {
            baseDto.setIds(categoryPlannings.get(0).getId());
        } else {
            List<String> cpIds = categoryPlannings.stream().map(CategoryPlanning::getId).collect(Collectors.toList());
            baseDto.setIds(CollUtil.join(cpIds, ","));
        }
        categoryPlanningService.updateStatus(baseDto);
    }

    @Override
    @Transactional
    public void delFlag(RemoveDto removeDto) {
        String ids = removeDto.getIds();
        List<String> list = Arrays.asList(ids.split(","));
        QueryWrapper<SeasonalPlanning> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", list);
        queryWrapper.eq("status","0");
        long l = count(queryWrapper);
        if (l > 0){
            throw new RuntimeException("存在启用的季节企划,不能删除");
        }
        QueryWrapper<CategoryPlanning> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("seasonal_planning_id",ids);
        List<CategoryPlanning> categoryPlannings = categoryPlanningService.list(categoryQueryWrapper);
        if (CollectionUtils.isNotEmpty(categoryPlannings)) {
            throw new OtherException("已生成品类企划，请先删除对应的品类企划");
        }
        Boolean removeFlag = removeByIds(removeDto);
        if (!removeFlag) {
            throw new OtherException("季节企划删除失败，请刷新后重试！");
        }
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
