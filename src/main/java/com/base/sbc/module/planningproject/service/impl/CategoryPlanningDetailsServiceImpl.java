package com.base.sbc.module.planningproject.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.*;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningDetailsMapper;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-22 11:29:23
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class CategoryPlanningDetailsServiceImpl extends BaseServiceImpl<CategoryPlanningDetailsMapper, CategoryPlanningDetails> implements CategoryPlanningDetailsService {
    private final PlanningDimensionalityService planningDimensionalityService;
    private final FieldOptionConfigService fieldOptionConfigService;
    @Lazy
    private final PlanningProjectService planningProjectService;

    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectPlankService planningProjectPlankService;
    @Resource
    @Lazy
    private CategoryPlanningService categoryPlanningService;
    private final FieldManagementService fieldManagementService;
    private final CcmFeignService ccmFeignService;

    @Override
    public PageInfo<CategoryPlanningDetailsVo> queryPage(CategoryPlanningDetailsQueryDto dto) {
        PageHelper.startPage(dto);
        List<CategoryPlanningDetailsVo> categoryPlanningDetailsVos = this.queryList(dto);
        for (CategoryPlanningDetailsVo categoryPlanningDetailsVo : categoryPlanningDetailsVos) {
            if ("1".equals(categoryPlanningDetailsVo.getIsGenerate()) || StringUtils.isBlank(categoryPlanningDetailsVo.getDataJson())){
                categoryPlanningDetailsVo.setDataJson(this.getDetailById(categoryPlanningDetailsVo.getId()).getDataJson());
            }

        }
        return new PageInfo<>(categoryPlanningDetailsVos);
    }

    @Override
    public List<CategoryPlanningDetailsVo> queryList(CategoryPlanningDetailsQueryDto dto) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper = this.buildQueryWrapper(dto);
        return this.baseMapper.listByQueryWrapper(queryWrapper);
    }

    @Override
    public CategoryPlanningDetailsVo getDetailById(String id) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.eq("tcpd.id", id);
        List<CategoryPlanningDetailsVo> categoryPlanningDetailsVos = this.baseMapper.listByQueryWrapper(queryWrapper);
        if (!categoryPlanningDetailsVos.isEmpty()) {

            CategoryPlanningDetailsVo categoryPlanningDetailsVo = categoryPlanningDetailsVos.get(0);


            String bandName = categoryPlanningDetailsVo.getBandName();
            String prodCategoryName = categoryPlanningDetailsVo.getProdCategoryName();
            String prodCategory2ndName = categoryPlanningDetailsVo.getProdCategory2ndName();
            String[] split2 = prodCategory2ndName.split(",");
            List<String> arr = new ArrayList<>();
            for (String s : split2) {
                if (StringUtils.isNotBlank(s)) {
                    arr.add(s);
                }
            }
            prodCategory2ndName = StringUtils.convertListToString(arr);
            String prodCategory2ndCode = categoryPlanningDetailsVo.getProdCategory2ndCode();
            String skcCount = categoryPlanningDetailsVo.getSkcCount();
            JSONObject object = new JSONObject();
            String[] split = skcCount.split(",");
            int i = 0;

            for (String s : prodCategoryName.split(",")) {
                if (StringUtils.isBlank(prodCategory2ndName)) {
                    for (String string : bandName.split(",")) {
                        String s1 = object.getString(string + "-" + s);
                        if (StringUtils.isBlank(s1)) {
                            object.put(string + "-" + s, split[i]);
                        } else {
                            int i1 = Integer.parseInt(s1);
                            i1 = i1 + Integer.parseInt(split[i]);
                            object.put(string + "-" + s, String.valueOf(i1));
                        }
                    }
                    i++;
                } else {
                    for (String s1 : prodCategory2ndName.split(",")) {
                        for (String string : bandName.split(",")) {
                            String s2 = object.getString(string + "-" + s + "-" + s1);
                            if (StringUtils.isBlank(s2)) {
                                object.put(string + "-" + s + "-" + s1, split[i]);
                            } else {
                                int i1 = Integer.parseInt(s2);
                                i1 = i1 + Integer.parseInt(split[i]);

                                object.put(string + "-" + s + "-" + s1, String.valueOf(i1));
                            }
                            i++;
                        }

                    }
                }
            }
            String dataJson = categoryPlanningDetailsVo.getDataJson();
            if (StringUtils.isNotBlank(dataJson)) {
                JSONArray jsonArrays = new JSONArray();
                JSONArray jsonArray = JSON.parseArray(dataJson);

                for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i1);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("band");
                    if (jsonObject1 != null) {
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("bandCode");
                        List<String> bandCodes = new ArrayList<>();
                        for (int i11 = 0; i11 < jsonArray1.size(); i11++) {
                            bandCodes.add(jsonArray1.getString(i11));
                        }

                        JSONArray jsonArray3 = jsonObject1.getJSONArray("number");
                        List<Integer> numbers = new ArrayList<>();
                        for (int i11 = 0; i11 < jsonArray3.size(); i11++) {
                            numbers.add(jsonArray3.getInteger(i11));
                        }

                        // 重新计算数量
                        for (int i11 = 0; i11 < bandCodes.size(); i11++) {
                            String bandCodeStr = bandCodes.get(i11);

                            String prodCategory1stCodes = jsonObject.getString("prodCategory1stCode");
                            String prodCategory2ndCodes = jsonObject.getString("prodCategory2ndCode");
                            String prodCategoryCodes = jsonObject.getString("prodCategoryCode");

                            String dimensionIds = jsonObject.getString("dimensionId");
                            String dimensionNames = jsonObject.getString("dimensionName");
                            String dimensionCodes = jsonObject.getString("dimensionCode");
                            String dimensionValues = jsonObject.getString("dimensionValue");
                            String dimensionTypeCodes = jsonObject.getString("dimensionTypeCode");
                            // 查询当前详情生成所有品类维度
                            BaseQueryWrapper<PlanningProjectDimension> queryWrapper1 = new BaseQueryWrapper<>();
                            queryWrapper1.eq("category_planning_details_id", id);
                            queryWrapper1.eq("prod_category1st_code", prodCategory1stCodes);
                            // queryWrapper1.notEmptyEq("prod_category2nd_code", prodCategory2ndCodes);
                            queryWrapper1.eq("prod_category_code", prodCategoryCodes);
                            queryWrapper1.eq("band_code", bandCodeStr);
                            queryWrapper1.eq("dimension_code", dimensionCodes);
                            queryWrapper1.eq("dimension_value", dimensionValues);
                            //先去查品类的,如果品类没有,就去查中类
                            List<PlanningProjectDimension> list = planningProjectDimensionService.list(queryWrapper1);
                            if (list.isEmpty()){
                                 queryWrapper1.eq("prod_category2nd_code", prodCategory2ndCodes);
                                list= planningProjectDimensionService.list(queryWrapper1);
                            }


                            List<String> collect = list.stream().map(PlanningProjectDimension::getNumber).collect(Collectors.toList());
                            int n = 0;
                            for (String s : collect) {
                                if (StringUtils.isNotBlank(s)) {
                                    n = n + Integer.parseInt(s);
                                }

                            }
                            numbers.set(i11, n);

                            // for (PlanningProjectDimension planningProjectDimension : list) {
                            //     if (planningProjectDimension.getBandCode().equals(bandCodeStr) && planningProjectDimension.getDimensionCode().equals(dimensionCodes)
                            //     && planningProjectDimension.getDimensionValue().equals(dimensionValues) && planningProjectDimension.getDimensionName().equals(dimensionNames)
                            //     && planningProjectDimension.getDimensionTypeCode().equals(dimensionTypeCodes) && dimensionIds.equals(planningProjectDimension.getDimensionId())
                            //      && prodCategory1stCodes.equals(planningProjectDimension.getProdCategory1stCode()) && prodCategoryCodes.equals(planningProjectDimension.getProdCategoryCode())){
                            //
                            //         if (StringUtils.isNotBlank(prodCategory2ndCodes)){
                            //             if (planningProjectDimension.getProdCategory2ndCode().equals(prodCategory2ndCodes)){
                            //                 numbers.set(i11, Integer.valueOf(planningProjectDimension.getNumber()));
                            //             }
                            //         }else {
                            //             numbers.set(i11, Integer.valueOf(planningProjectDimension.getNumber()));
                            //         }
                            //     }
                            // }
                        }
                        JSONArray jsonArray2 = (JSONArray) JSON.toJSON(numbers);
                        jsonObject1.put("number", jsonArray2);
                        jsonObject.put("band", jsonObject1);
                        jsonArrays.add(jsonObject);
                    }

                    categoryPlanningDetailsVo.setDataJson(jsonArrays.toJSONString());


                }

                return categoryPlanningDetailsVo;
            }
            // 获取第一维度与纬度值
            String seasonId = categoryPlanningDetailsVo.getSeasonId();
            String prodCategory1stCode = categoryPlanningDetailsVo.getProdCategory1stCode();
            String prodCategory1stName = categoryPlanningDetailsVo.getProdCategory1stName();
            String prodCategoryCode = categoryPlanningDetailsVo.getProdCategoryCode();
            String channelCode = categoryPlanningDetailsVo.getChannelCode();

            QueryWrapper<PlanningDimensionality> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("channel", channelCode);
            queryWrapper1.eq("planning_season_id", seasonId);
            queryWrapper1.eq("prod_category1st", prodCategory1stCode);
            queryWrapper1.eq("prod_category", prodCategoryCode);
            queryWrapper1.eq("dimensionality_grade", "1");

            List<JSONObject> jsonObjects = new ArrayList<>();
            List<PlanningDimensionality> list = planningDimensionalityService.list(queryWrapper1);
            //先去查品类的,如果品类没有,就去查中类
            String[] split1=null;
            String[] split11=null;
            if (StringUtils.isNotBlank(prodCategory2ndCode)) {
                split1 = prodCategory2ndCode.split(",");
                split11=prodCategory2ndName.split(",");
                List<String> list2 = new ArrayList<>();
                for (String s : split1) {
                    if (StringUtils.isNotBlank(s)) {
                        list2.add(s);
                    }
                }
                if (!list2.isEmpty()) {
                    queryWrapper1.in("prod_category2nd", Arrays.asList(split1));
                }
            }
            if (list.isEmpty()){
                list = planningDimensionalityService.list(queryWrapper1);
            }else {
                //如果按照品类查询的数据,存在中类,并且未填写中类,提示错误 请将需求细化到中类
                if (StringUtils.isBlank(prodCategory2ndCode)) {
                    for (PlanningDimensionality planningDimensionality : list) {
                        if (StringUtils.isNotBlank(planningDimensionality.getProdCategory2nd())) {
                            throw new RuntimeException("请将需求细化到中类");
                        }
                    }
                }
            }

            if (!list.isEmpty()) {
                for (PlanningDimensionality planningDimensionality : list) {

                    // 获取配置
                    BaseQueryWrapper<FieldOptionConfig> queryWrapper2 = new BaseQueryWrapper<>();
                    queryWrapper2.eq("field_management_id", planningDimensionality.getFieldId());
                    queryWrapper2.notEmptyEq("company_code", planningDimensionality.getCompanyCode());
                    queryWrapper2.notEmptyEq("channel", planningDimensionality.getChannel());
                    queryWrapper2.notEmptyIn("prod_category1st", planningDimensionality.getProdCategory1st());
                    queryWrapper2.notEmptyIn("prod_category", planningDimensionality.getProdCategory());
                    queryWrapper2.notEmptyIn("prod_category2nd", planningDimensionality.getProdCategory2nd());
                    List<FieldOptionConfig> list1 = fieldOptionConfigService.list(queryWrapper2);
                    // 如果没有配置就去全部的字段值
                    FieldManagement fieldManagement = fieldManagementService.getById(planningDimensionality.getFieldId());

                    if (list1.isEmpty()) {
                        String isOption = fieldManagement.getIsOption();

                        if ("1".equals(isOption)) {
                            // 从字典获取
                            String dimensionTypeCode = fieldManagement.getOptionDictKey();
                            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap(dimensionTypeCode);
                            for (String s : dictInfoToMap.keySet()) {
                                Map<String, String> map = dictInfoToMap.get(s);
                                for (String string : map.keySet()) {
                                    FieldOptionConfig fieldOptionConfig = new FieldOptionConfig();
                                    fieldOptionConfig.setFieldManagementId(fieldManagement.getId());
                                    fieldOptionConfig.setOptionName(string);
                                    fieldOptionConfig.setOptionCode(map.get(string));
                                    list1.add(fieldOptionConfig);
                                }
                            }

                        }
                        if ("2".equals(isOption)) {
                            String optionDictKey = fieldManagement.getOptionDictKey();
                            List<BasicStructureTreeVo> basicStructureTreeVos = ccmFeignService.basicStructureTreeByCode(optionDictKey, null, fieldManagement.getStructureTier());
                            // List<FieldOptionConfig> fieldOptionConfigs = new ArrayList<>();
                            for (BasicStructureTreeVo basicStructureTreeVo : basicStructureTreeVos) {
                                FieldOptionConfig fieldOptionConfig = new FieldOptionConfig();
                                fieldOptionConfig.setOptionName(basicStructureTreeVo.getName());
                                fieldOptionConfig.setOptionCode(basicStructureTreeVo.getValue());
                                fieldOptionConfig.setFieldManagementId(fieldManagement.getId());
                                // fieldOptionConfigs.add(fieldOptionConfig);
                                list1.add(fieldOptionConfig);
                            }
                            // list1 = fieldOptionConfigs;
                        }

                    }
                    List<String> nameList = list1.stream().map(FieldOptionConfig::getOptionName).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                    ;
                    // List<String> codeList =  list1.stream().map(FieldOptionConfig::getOptionCode).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                    // List<String> ids =  list1.stream().map(FieldOptionConfig::getFieldManagementId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());


                    if (split1 !=null){
                        for (int i2 = 0; i2 < split1.length; i2++) {
                            for (String s : nameList) {
                                for (FieldOptionConfig fieldOptionConfig : list1) {
                                    if (fieldOptionConfig.getOptionName().equals(s)) {
                                        JSONObject jsonObject = new JSONObject();
                                        //     dimensionTypeCode
                                        jsonObject.put("dimensionCode", fieldOptionConfig.getOptionName());
                                        jsonObject.put("dimensionValue", fieldOptionConfig.getOptionCode());
                                        jsonObject.put("dimensionId", planningDimensionality.getFieldId());
                                        jsonObject.put("dimensionName", planningDimensionality.getDimensionalityName());
                                        jsonObject.put("dimensionTypeCode", fieldManagement.getFormTypeId());
                                        jsonObject.put("skuCount", categoryPlanningDetailsVo.getSkcCount());
                                        jsonObject.put("prodCategory1stName", prodCategory1stName);
                                        jsonObject.put("prodCategory1stCode", prodCategory1stCode);
                                        jsonObject.put("maxSum", object.toJSONString());
                                        jsonObject.put("prodCategoryCode", planningDimensionality.getProdCategory());
                                        jsonObject.put("prodCategoryName", planningDimensionality.getProdCategoryName());
                                        jsonObject.put("prodCategory2ndCode", split1[i2]);
                                        jsonObject.put("prodCategory2ndName", split11[i2]);
                                        jsonObjects.add(jsonObject);
                                    }

                                }

                            }
                        }
                    }else {
                        for (String s : nameList) {
                            for (FieldOptionConfig fieldOptionConfig : list1) {
                                if (fieldOptionConfig.getOptionName().equals(s)) {
                                    JSONObject jsonObject = new JSONObject();
                                    //     dimensionTypeCode
                                    jsonObject.put("dimensionCode", fieldOptionConfig.getOptionName());
                                    jsonObject.put("dimensionValue", fieldOptionConfig.getOptionCode());
                                    jsonObject.put("dimensionId", planningDimensionality.getFieldId());
                                    jsonObject.put("dimensionName", planningDimensionality.getDimensionalityName());
                                    jsonObject.put("dimensionTypeCode", fieldManagement.getFormTypeId());
                                    jsonObject.put("skuCount", categoryPlanningDetailsVo.getSkcCount());
                                    jsonObject.put("prodCategory1stName", prodCategory1stName);
                                    jsonObject.put("prodCategory1stCode", prodCategory1stCode);
                                    jsonObject.put("maxSum", object.toJSONString());
                                    jsonObject.put("prodCategoryCode", planningDimensionality.getProdCategory());
                                    jsonObject.put("prodCategoryName", planningDimensionality.getProdCategoryName());
                                    jsonObjects.add(jsonObject);
                                }

                            }

                        }
                    }


                }
                categoryPlanningDetailsVo.setDataJson(JSON.toJSONString(jsonObjects, SerializerFeature.WriteNullStringAsEmpty));
            }


            return categoryPlanningDetailsVo;
        }
        throw new RuntimeException("未找到数据");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDetail(CategoryPlanningDetailsVo categoryPlanningDetailsVo) {

        CategoryPlanningDetails categoryPlanningDetails1 = this.getById(categoryPlanningDetailsVo.getId());
        if ("1".equals(categoryPlanningDetails1.getIsGenerate())) {
            throw new RuntimeException("数据已经生成,无法修改");
        }
        if (StringUtils.isEmpty(categoryPlanningDetailsVo.getDataJson()) || "[]".equals(categoryPlanningDetailsVo.getDataJson())) {
            return false;
        }
        categoryPlanningDetailsVo.setIsGenerate("1");
        // 修改数据
        boolean b = this.updateById(categoryPlanningDetailsVo);

        CategoryPlanning categoryPlanning = categoryPlanningService.getById(categoryPlanningDetailsVo.getCategoryPlanningId());
        QueryWrapper<PlanningProject> queryWrapper = new QueryWrapper<>();

        // 如果不存在相关的企划看板,则创建
        queryWrapper.eq("category_planning_id", categoryPlanningDetails1.getCategoryPlanningId());
        PlanningProject planningProject = planningProjectService.getOne(queryWrapper);
        if (planningProject == null) {
            QueryWrapper<PlanningProject> queryWrapper1 = new QueryWrapper<>();
            queryWrapper.eq("status", "0");
            queryWrapper.eq("seasonal_id", categoryPlanning.getSeasonId());
            queryWrapper.eq("channel_code", categoryPlanning.getChannelCode());
            queryWrapper.eq("company_code", categoryPlanning.getCompanyCode());
            long l1 = planningProjectService.count(queryWrapper1);


            planningProject = new PlanningProject();
            planningProject.setCategoryPlanningId(categoryPlanning.getId());
            planningProject.setSeasonId(categoryPlanning.getSeasonId());
            planningProject.setSeasonName(categoryPlanning.getSeasonName());
            planningProject.setPlanningChannelCode(categoryPlanning.getChannelCode());
            planningProject.setPlanningChannelName(categoryPlanning.getChannelName());
            planningProject.setPlanningProjectName(categoryPlanningDetailsVo.getCategoryPlanningName());
            planningProject.setStatus(l1 == 0 ? "0" : "1");
            planningProjectService.save(planningProject);
        }


        List<PlanningProjectDimension> planningProjectDimensionList = new ArrayList<>();

        String dataJson = categoryPlanningDetailsVo.getDataJson();
        if (StringUtils.isNotBlank(dataJson)) {
            JSONArray jsonArray = JSON.parseArray(dataJson);
            // List<String> prodCategory1stNames = new ArrayList<>();
            // List<String> prodCategory1stCodes = new ArrayList<>();
            // Map<String,String> map = new HashMap<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject band = jsonObject.getJSONObject("band");
                if (band != null) {
                    JSONArray bandNames = band.getJSONArray("bandName");
                    JSONArray bandCodes = band.getJSONArray("bandCode");
                    JSONArray numbers = band.getJSONArray("number");
                    for (int i1 = 0; i1 < bandNames.size(); i1++) {
                        String number = numbers.getString(i1);
                        if (StringUtils.isEmpty(number)) {
                            continue;
                        }

                        String bandName = bandNames.getString(i1);
                        String bandCode = bandCodes.getString(i1);


                        // 企划看板维度数据
                        PlanningProjectDimension planningProjectDimension = new PlanningProjectDimension();

                        planningProjectDimension.setPlanningProjectId(planningProject.getId());
                        planningProjectDimension.setBandName(bandName);
                        planningProjectDimension.setBandCode(bandCode);
                        planningProjectDimension.setNumber(number);

                        planningProjectDimension.setProdCategory1stCode(jsonObject.getString("prodCategory1stCode"));
                        planningProjectDimension.setProdCategory1stName(jsonObject.getString("prodCategory1stName"));
                        planningProjectDimension.setProdCategory2ndCode(jsonObject.getString("prodCategory2ndCode"));
                        planningProjectDimension.setProdCategory2ndName(jsonObject.getString("prodCategory2ndName"));
                        planningProjectDimension.setCategoryPlanningDetailsId(categoryPlanningDetailsVo.getId());
                        planningProjectDimension.setProdCategoryCode(jsonObject.getString("prodCategoryCode"));
                        planningProjectDimension.setProdCategoryName(jsonObject.getString("prodCategoryName"));


                        planningProjectDimension.setDimensionId(jsonObject.getString("dimensionId"));
                        planningProjectDimension.setDimensionName(jsonObject.getString("dimensionName"));

                        planningProjectDimension.setDimensionCode(jsonObject.getString("dimensionCode"));
                        planningProjectDimension.setDimensionValue(jsonObject.getString("dimensionValue"));
                        planningProjectDimension.setDimensionTypeCode(jsonObject.getString("dimensionTypeCode"));
                        // planningProjectDimension.setCompanyCode("45646456");

                        planningProjectDimensionList.add(planningProjectDimension);
                        // planningProjectDimensionService.save(planningProjectDimension);

                    }
                }

            }
            planningProjectDimensionService.saveBatch(planningProjectDimensionList);


            // 生成坑位
            List<PlanningProjectPlank> planningProjectPlanks = new ArrayList<>();
            for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {

                if (StringUtils.isNotBlank(planningProjectDimension.getNumber())) {
                    for (int j = 0; j < Integer.parseInt(planningProjectDimension.getNumber()); j++) {
                        PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                        planningProjectPlank.setPlanningProjectId(planningProject.getId());
                        planningProjectPlank.setMatchingStyleStatus("0");
                        planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
                        planningProjectPlank.setBandName(planningProjectDimension.getBandName());
                        planningProjectPlank.setPlanningProjectDimensionId(planningProjectDimension.getId());
                        planningProjectPlanks.add(planningProjectPlank);
                    }
                }


            }
            planningProjectPlankService.saveBatch(planningProjectPlanks);
        }

        return b;
    }

    /**
     * 构造查询期
     */
    public BaseQueryWrapper<CategoryPlanningDetails> buildQueryWrapper(CategoryPlanningDetailsQueryDto dto) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tcpd.category_planning_id", dto.getCategoryPlanningId());
        queryWrapper.notEmptyEq("tcpd.seasonal_planning_id", dto.getSeasonalPlanningId());
        queryWrapper.notEmptyIn("tcpd.prod_category_name", dto.getProdCategoryNames());
        return queryWrapper;
    }


}
