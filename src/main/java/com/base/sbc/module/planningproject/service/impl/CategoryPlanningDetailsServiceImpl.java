package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailDTO;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.entity.*;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningDetailsMapper;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailVO;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-22 11:29:23
 * @mail 247967116@qq.com
 */
@Service
public class CategoryPlanningDetailsServiceImpl extends BaseServiceImpl<CategoryPlanningDetailsMapper, CategoryPlanningDetails> implements CategoryPlanningDetailsService {
    @Autowired
    private PlanningDimensionalityService planningDimensionalityService;
    @Autowired
    private FieldOptionConfigService fieldOptionConfigService;
    @Autowired
    @Lazy
    private PlanningProjectService planningProjectService;
    @Autowired
    private PlanningProjectDimensionService planningProjectDimensionService;
    @Autowired
    private PlanningProjectPlankService planningProjectPlankService;
    @Autowired
    private CategoryPlanningService categoryPlanningService;
    @Autowired
    private FieldManagementService fieldManagementService;
    @Autowired
    private CcmFeignService ccmFeignService;
    @Autowired
    private SeasonalPlanningService seasonalPlanningService;
    @Autowired
    @Lazy
    private SeasonalPlanningDetailsService seasonalPlanningDetailsService;

    @Override
    public PageInfo<CategoryPlanningDetailsVo> queryPage(CategoryPlanningDetailsQueryDto dto) {
        PageHelper.startPage(dto);
        List<CategoryPlanningDetailsVo> categoryPlanningDetailsVos = this.queryList(dto);
        for (CategoryPlanningDetailsVo categoryPlanningDetailsVo : categoryPlanningDetailsVos) {
            if ("1".equals(categoryPlanningDetailsVo.getIsGenerate()) || StringUtils.isBlank(categoryPlanningDetailsVo.getDataJson())) {
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
                            // 先去查品类的,如果品类没有,就去查中类
                            List<PlanningProjectDimension> list = planningProjectDimensionService.list(queryWrapper1);
                            if (list.isEmpty()) {
                                queryWrapper1.eq("prod_category2nd_code", prodCategory2ndCodes);
                                list = planningProjectDimensionService.list(queryWrapper1);
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
            // 先去查品类的,如果品类没有,就去查中类
            String[] split1 = null;
            String[] split11 = null;
            if (StringUtils.isNotBlank(prodCategory2ndCode)) {
                split1 = prodCategory2ndCode.split(",");
                split11 = prodCategory2ndName.split(",");
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
            if (list.isEmpty()) {
                list = planningDimensionalityService.list(queryWrapper1);
            } else {
                // 如果按照品类查询的数据,存在中类,并且未填写中类,提示错误 请将需求细化到中类
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


                    if (split1 != null) {
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
                    } else {
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
        boolean b = updateById(categoryPlanningDetailsVo);

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
     * @param categoryPlanningDetailDTO 查询条件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryPlanningDetailVO getDetail(CategoryPlanningDetailDTO categoryPlanningDetailDTO) {
        // 初始化返回的对象
        CategoryPlanningDetailVO categoryPlanningDetailVO = new CategoryPlanningDetailVO();
        String categoryPlanningId = categoryPlanningDetailDTO.getCategoryPlanningId();
        String prodCategoryCode = categoryPlanningDetailDTO.getProdCategoryCode();
        String dimensionIds = categoryPlanningDetailDTO.getDimensionIds();
        if (ObjectUtil.isEmpty(categoryPlanningId)) {
            throw new OtherException("请选择品类企划！");
        }
        if (ObjectUtil.isEmpty(prodCategoryCode)) {
            throw new OtherException("请选择品类！");
        }
        if (ObjectUtil.isEmpty(dimensionIds)) {
            throw new OtherException("请选择维度数据！");
        }
        CategoryPlanning categoryPlanning = categoryPlanningService.getById(categoryPlanningId);
        if (ObjectUtil.isEmpty(categoryPlanning)) {
            throw new OtherException("品类企划数据不存在，请刷新后重试！");
        }
        if (count(
                new LambdaQueryWrapper<CategoryPlanningDetails>()
                        .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId())
                        .eq(CategoryPlanningDetails::getDelFlag, BaseGlobal.NO)
        ) < 1) {
            return categoryPlanningDetailVO;
        }

        // 根据传入的维度 id 判断是否有不能存在的维度 如果有 则新增品类企划数据
        List<CategoryPlanningDetails> dimensionality = getDimensionalitySelf(categoryPlanningDetailDTO, categoryPlanning);
        if (ObjectUtil.isNotEmpty(dimensionality)) {
            Map<String, CategoryPlanningDetails> categoryPlanningDetailsMap = dimensionality.stream()
                    .collect(Collectors.toMap(CategoryPlanningDetails::getDimensionId, item -> item));
            List<String> dimensionIdList = CollUtil.newArrayList(dimensionIds.split(","));
            dimensionIdList = dimensionIdList.stream()
                    .filter(item -> ObjectUtil.isEmpty(categoryPlanningDetailsMap.get(item))).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(dimensionIdList)) {
                // 新增此维度的数据
                // 查询季节企划信息
                SeasonalPlanning seasonalPlanning = seasonalPlanningService.getById(categoryPlanning.getSeasonalPlanningId());
                if (ObjectUtil.isNotEmpty(seasonalPlanning)) {
                    List<SeasonalPlanningDetails> seasonalPlanningDetailsList = seasonalPlanningDetailsService.list(
                            new LambdaQueryWrapper<SeasonalPlanningDetails>()
                                    .eq(SeasonalPlanningDetails::getSeasonalPlanningId, seasonalPlanning.getId())
                                    .eq(SeasonalPlanningDetails::getProdCategoryCode, categoryPlanningDetailDTO.getProdCategoryCode())
                    );
                    if (ObjectUtil.isNotEmpty(seasonalPlanningDetailsList)) {
                        List<CategoryPlanningDetails> categoryPlanningDetailsList = categoryPlanningService.generationCategoryPlanningDetails(
                                seasonalPlanningDetailsList,
                                seasonalPlanning,
                                categoryPlanning,
                                dimensionIdList
                        );
                        if (ObjectUtil.isNotEmpty(categoryPlanningDetailsList)) {
                            boolean saveFlag = saveBatch(categoryPlanningDetailsList);
                            if (!saveFlag) {
                                throw new OtherException("同步最新维度数据出现错误，请刷新后重试！");
                            }
                        }
                    }
                }
            }
        }

        {
            // 查询所有的品类企划详情数据 修改维度等等级
            List<CategoryPlanningDetails> categoryPlanningDetailsList = list(
                    new LambdaQueryWrapper<CategoryPlanningDetails>()
                            .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId())
            );
            // 最新的维度配置
            List<CategoryPlanningDetails> dynamicCategoryPlanningDetailsList = getDimensionality(categoryPlanningDetailDTO);
            Map<String, CategoryPlanningDetails> categoryPlanningDetailsMap = dynamicCategoryPlanningDetailsList.stream()
                    .collect(Collectors.toMap(CategoryPlanningDetails::getDimensionId, item -> item));
            for (CategoryPlanningDetails categoryPlanningDetails : categoryPlanningDetailsList) {
                CategoryPlanningDetails details = categoryPlanningDetailsMap.get(categoryPlanningDetails.getDimensionId());
                if (ObjectUtil.isNotEmpty(details)) {
                    categoryPlanningDetails.setDimensionalityGrade(details.getDimensionalityGrade());
                    categoryPlanningDetails.setDimensionalityGradeName(details.getDimensionalityGradeName());
                }
            }

            updateBatchById(categoryPlanningDetailsList);
        }


        LambdaQueryWrapper<CategoryPlanningDetails> queryWrapper = new LambdaQueryWrapper<>();
        // 查询出筛选后的品类企划
        {
            assembleFilters(queryWrapper, categoryPlanning, prodCategoryCode, dimensionIds);
            queryWrapper.select(
                    CategoryPlanningDetails::getId,
                    CategoryPlanningDetails::getNumber,
                    CategoryPlanningDetails::getIsGenerate,
                    CategoryPlanningDetails::getBandName,
                    CategoryPlanningDetails::getProdCategory1stName,
                    CategoryPlanningDetails::getProdCategoryName,
                    CategoryPlanningDetails::getProdCategory2ndName,
                    CategoryPlanningDetails::getDimensionName,
                    CategoryPlanningDetails::getDimensionCode,
                    CategoryPlanningDetails::getSkcCount,
                    CategoryPlanningDetails::getTotal
            );
            List<CategoryPlanningDetails> categoryPlanningDetailsList = list(queryWrapper);

            // 增加波段合计的数据
            Map<String, List<CategoryPlanningDetails>> brandCategoryPlanningDetailsMap = categoryPlanningDetailsList.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getProdCategoryName()
                                    + "-" + item.getProdCategory2ndName()
                                    + "-" + item.getDimensionName()
                                    + "-" + item.getBandName()
                            , LinkedHashMap::new, Collectors.toList()
                    ));

            for (Map.Entry<String, List<CategoryPlanningDetails>> stringListEntry : brandCategoryPlanningDetailsMap.entrySet()) {
                List<CategoryPlanningDetails> list = stringListEntry.getValue();
                CategoryPlanningDetails categoryPlanningDetails = list.get(0);
                CategoryPlanningDetails totalCategoryPlanningDetails = new CategoryPlanningDetails();
                totalCategoryPlanningDetails.setSkcCount(categoryPlanningDetails.getSkcCount());
                totalCategoryPlanningDetails.setBandName(categoryPlanningDetails.getBandName());
                totalCategoryPlanningDetails.setProdCategory1stName(categoryPlanningDetails.getProdCategory1stName());
                totalCategoryPlanningDetails.setProdCategoryName(categoryPlanningDetails.getProdCategoryName());
                totalCategoryPlanningDetails.setProdCategory2ndName(categoryPlanningDetails.getProdCategory2ndName());
                totalCategoryPlanningDetails.setDimensionName(categoryPlanningDetails.getDimensionName());
                totalCategoryPlanningDetails.setDimensionCode("合计");
                categoryPlanningDetailsList.add(totalCategoryPlanningDetails);
            }

            // 增加维度名称合计的数据
            Map<String, List<CategoryPlanningDetails>> dimensioncategoryPlanningDetailsMap = categoryPlanningDetailsList.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getProdCategoryName()
                                    + "-" + item.getProdCategory2ndName()
                                    + "-" + item.getDimensionName()
                            , LinkedHashMap::new, Collectors.toList()
                    ));
            for (Map.Entry<String, List<CategoryPlanningDetails>> stringListEntry : dimensioncategoryPlanningDetailsMap.entrySet()) {
                List<CategoryPlanningDetails> list = stringListEntry.getValue();
                CategoryPlanningDetails categoryPlanningDetails = list.get(0);
                CategoryPlanningDetails totalCategoryPlanningDetails = new CategoryPlanningDetails();
                totalCategoryPlanningDetails.setTotal(categoryPlanningDetails.getTotal());
                totalCategoryPlanningDetails.setBandName("合计");
                totalCategoryPlanningDetails.setProdCategory1stName(categoryPlanningDetails.getProdCategory1stName());
                totalCategoryPlanningDetails.setProdCategoryName(categoryPlanningDetails.getProdCategoryName());
                totalCategoryPlanningDetails.setProdCategory2ndName(categoryPlanningDetails.getProdCategory2ndName());
                totalCategoryPlanningDetails.setDimensionName(categoryPlanningDetails.getDimensionName());
                totalCategoryPlanningDetails.setDimensionCode("合计");
                categoryPlanningDetailsList.add(totalCategoryPlanningDetails);
            }

            categoryPlanningDetailVO.setCategoryPlanningDetailsList(categoryPlanningDetailsList);
        }

        // 查询品类企划根据 大/品/中/维度类型/维度值 分组后的数据
        {
            assembleFilters(queryWrapper, categoryPlanning, prodCategoryCode, dimensionIds);
            queryWrapper.select(
                    CategoryPlanningDetails::getProdCategory1stName,
                    CategoryPlanningDetails::getProdCategoryName,
                    CategoryPlanningDetails::getProdCategory2ndName,
                    CategoryPlanningDetails::getDimensionName,
                    CategoryPlanningDetails::getDimensionCode);
            queryWrapper.orderByAsc(CategoryPlanningDetails::getId);
            queryWrapper.groupBy(
                    CategoryPlanningDetails::getProdCategory1stName,
                    CategoryPlanningDetails::getProdCategoryName,
                    CategoryPlanningDetails::getProdCategory2ndName,
                    CategoryPlanningDetails::getDimensionName,
                    CategoryPlanningDetails::getDimensionCode);
            List<CategoryPlanningDetails> groupByDimensionalityValueList = list(queryWrapper);

            // 重新排序
            {

                Map<String, List<CategoryPlanningDetails>> categoryPlanningDetailsMap = groupByDimensionalityValueList.stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getProdCategoryName()
                                        + "-" + item.getProdCategory2ndName()
                                , LinkedHashMap::new, Collectors.toList()
                        ));
                groupByDimensionalityValueList = new ArrayList<>();
                for (Map.Entry<String, List<CategoryPlanningDetails>> stringListEntry : categoryPlanningDetailsMap.entrySet()) {
                    groupByDimensionalityValueList.addAll(stringListEntry.getValue());
                }
            }

            // 在每个维度名称后面增加合计的数据
            Map<String, List<CategoryPlanningDetails>> categoryPlanningDetailsMap = groupByDimensionalityValueList.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getProdCategoryName()
                                    + "-" + item.getProdCategory2ndName()
                                    + "-" + item.getDimensionName()
                            , LinkedHashMap::new, Collectors.toList()
                    ));


            // 设置初始化的索引位置
            int index = 0;
            Iterator<Map.Entry<String, List<CategoryPlanningDetails>>> iterator = categoryPlanningDetailsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<CategoryPlanningDetails>> entry = iterator.next();
                List<CategoryPlanningDetails> list = entry.getValue();
                CategoryPlanningDetails categoryPlanningDetails = list.get(0);
                CategoryPlanningDetails totalCategoryPlanningDetails = new CategoryPlanningDetails();
                totalCategoryPlanningDetails.setBandName(categoryPlanningDetails.getBandName());
                totalCategoryPlanningDetails.setProdCategory1stName(categoryPlanningDetails.getProdCategory1stName());
                totalCategoryPlanningDetails.setProdCategoryName(categoryPlanningDetails.getProdCategoryName());
                totalCategoryPlanningDetails.setProdCategory2ndName(categoryPlanningDetails.getProdCategory2ndName());
                totalCategoryPlanningDetails.setDimensionName(categoryPlanningDetails.getDimensionName());
                totalCategoryPlanningDetails.setDimensionCode("合计");
                // 如果不是最后一个 添加到相应索引后面 如果是最后一个 直接添加
                if (iterator.hasNext()) {
                    index += list.size();
                    groupByDimensionalityValueList.add(index, totalCategoryPlanningDetails);
                    index++;
                } else {
                    groupByDimensionalityValueList.add(totalCategoryPlanningDetails);

                }
            }
            categoryPlanningDetailVO.setGroupByDimensionalityValueList(groupByDimensionalityValueList);
        }

        // 查询品类企划数据根据波段分组后的数据
        // 初始化 数据
        {
            assembleFilters(queryWrapper, categoryPlanning, prodCategoryCode, dimensionIds);
            queryWrapper.select(CategoryPlanningDetails::getBandName);
            queryWrapper.groupBy(CategoryPlanningDetails::getBandName);
            List<CategoryPlanningDetails> groupByBandList = list(queryWrapper);
            if (ObjectUtil.isNotEmpty(groupByBandList)) {
                // 增加合计的数据
                CategoryPlanningDetails categoryPlanningDetails = new CategoryPlanningDetails();
                categoryPlanningDetails.setBandName("合计");
                groupByBandList.add(categoryPlanningDetails);
            }
            categoryPlanningDetailVO.setGroupByBandList(groupByBandList);
        }
        return categoryPlanningDetailVO;
    }

    private void assembleFilters(LambdaQueryWrapper<CategoryPlanningDetails> queryWrapper, CategoryPlanning categoryPlanning, String prodCategoryCode, String dimensionIds) {
        queryWrapper.clear();
        queryWrapper.eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId());
        queryWrapper.eq(CategoryPlanningDetails::getDelFlag, BaseGlobal.DEL_FLAG_NORMAL);
        queryWrapper.eq(CategoryPlanningDetails::getProdCategoryCode, prodCategoryCode);
        queryWrapper.in(CategoryPlanningDetails::getDimensionId, CollUtil.newArrayList(dimensionIds.split(",")));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void staging(List<CategoryPlanningDetails> categoryPlanningDetailsList) {
        if (ObjectUtil.isEmpty(categoryPlanningDetailsList)) {
            throw new OtherException("保存数据不能为空！");
        }

        // 根据 id 集合查询品类企划详情数据
        List<String> categoryPlanningDetailsIdList = categoryPlanningDetailsList.stream()
                .map(CategoryPlanningDetails::getId).collect(Collectors.toList());
        List<CategoryPlanningDetails> detailsList = listByIds(categoryPlanningDetailsIdList);

        if (ObjectUtil.isEmpty(detailsList)) {
            throw new OtherException("品类企划数据不存在，请刷新后重试！");
        }

        List<String> dimensionNameList = detailsList.stream()
                .filter(item -> "2".equals(item.getIsGenerate()))
                .map(CategoryPlanningDetails::getDimensionName)
                .distinct()
                .collect(Collectors.toList());
        if (!dimensionNameList.isEmpty()) {
            // 说明此品类已经作废 无法保存
            throw new OtherException("「" + CollUtil.join(dimensionNameList, ",") + "」维度数据已作废，无法保存！");
        }
        // 判断保存的数据是否已经审核
        List<String> stringList = categoryPlanningDetailsList.stream()
                .filter(item -> "1".equals(item.getIsGenerate()))
                .map(CategoryPlanningDetails::getDimensionName)
                .collect(Collectors.toList());
        if (!stringList.isEmpty()) {
            // 说明部分维度已经审核 无法保存
            throw new OtherException("「" + CollUtil.join(stringList, ",") + "」维度数据已审核，无法保存！");
        }
        updateBatchById(categoryPlanningDetailsList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void preservation(List<CategoryPlanningDetails> categoryPlanningDetailsList) {
        if (ObjectUtil.isEmpty(categoryPlanningDetailsList)) {
            throw new OtherException("审核数据不能为空！");
        }
        // 根据 id 集合查询品类企划详情数据
        Map<String, CategoryPlanningDetails> categoryPlanningDetailsMap
                = categoryPlanningDetailsList.stream().collect(Collectors.toMap(CategoryPlanningDetails::getId, item -> item));
        List<String> categoryPlanningDetailsIdList = categoryPlanningDetailsList.stream().map(CategoryPlanningDetails::getId).collect(Collectors.toList());
        List<CategoryPlanningDetails> detailsList = listByIds(categoryPlanningDetailsIdList);

        if (ObjectUtil.isEmpty(detailsList)) {
            throw new OtherException("品类企划数据不存在，请刷新后重试！");
        }

        List<String> dimensionNameList = detailsList.stream()
                .filter(item -> "2".equals(item.getIsGenerate()))
                .map(CategoryPlanningDetails::getDimensionName)
                .distinct()
                .collect(Collectors.toList());
        if (!dimensionNameList.isEmpty()) {
            // 说明此品类已经作废 无法审核
            throw new OtherException("「" + CollUtil.join(dimensionNameList, ",") + "」维度数据已作废，无法审核！");
        }
        // 判断保存的数据是否已经审核
        List<String> stringList = categoryPlanningDetailsList.stream()
                .filter(item -> "1".equals(item.getIsGenerate()))
                .map(CategoryPlanningDetails::getDimensionName)
                .collect(Collectors.toList());
        if (!stringList.isEmpty()) {
            // 说明部分维度已经审核 无法审核
            throw new OtherException("「" + CollUtil.join(stringList, ",") + "」维度数据已审核，无法审核！");
        }

        // 然后设置需求数
        for (CategoryPlanningDetails categoryPlanningDetails : detailsList) {
            CategoryPlanningDetails details = categoryPlanningDetailsMap.get(categoryPlanningDetails.getId());
            if (ObjectUtil.isNotEmpty(details)) {
                categoryPlanningDetails.setNumber(details.getNumber());
            } else {
                categoryPlanningDetails.setNumber("0");
            }
        }
        categoryPlanningDetailsList = detailsList;

        CategoryPlanningDetails categoryPlanningDetails = categoryPlanningDetailsList.get(0);
        // 根据品类企划 id 查询品类企划主表信息
        CategoryPlanning categoryPlanning = categoryPlanningService.getById(categoryPlanningDetails.getCategoryPlanningId());
        if (ObjectUtil.isEmpty(categoryPlanning)) {
            throw new OtherException("品类企划数据不存在，请刷新后重试！");
        }
        // 将品类企划详情数据改成已经生成的状态
        categoryPlanningDetailsList.forEach(item -> item.setIsGenerate("1"));
        updateBatchById(categoryPlanningDetailsList);

        // 如果不存在相关的企划看板，则创建
        QueryWrapper<PlanningProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_planning_id", categoryPlanning.getId());
        PlanningProject planningProject = planningProjectService.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(planningProject)) {
            // 清空搜索条件 并且查询是否有启用状态的企划看板
            queryWrapper.clear();
            queryWrapper.eq("status", "0");
            queryWrapper.eq("season_id", categoryPlanning.getSeasonId());
            queryWrapper.eq("planning_channel_code", categoryPlanning.getChannelCode());
            queryWrapper.eq("company_code", categoryPlanning.getCompanyCode());
            long count = planningProjectService.count(queryWrapper);

            // 生成企划看版
            planningProject = new PlanningProject();
            planningProject.setCategoryPlanningId(categoryPlanning.getId());
            planningProject.setSeasonId(categoryPlanning.getSeasonId());
            planningProject.setSeasonName(categoryPlanning.getSeasonName());
            planningProject.setPlanningChannelCode(categoryPlanning.getChannelCode());
            planningProject.setPlanningChannelName(categoryPlanning.getChannelName());
            planningProject.setPlanningProjectName(categoryPlanning.getName());
            planningProject.setStatus(count > 0 || "1".equals(categoryPlanning.getStatus()) ? "1" : "0");
            planningProjectService.save(planningProject);
        }

        // 初始化企划看板维度数据
        List<PlanningProjectDimension> planningProjectDimensionList = new ArrayList<>();
        // 生成企划看板维度数据
        for (CategoryPlanningDetails planningDetails : categoryPlanningDetailsList) {
            PlanningProjectDimension planningProjectDimension = assemblePlanningProjectDimension(planningDetails, planningProject);
            planningProjectDimensionList.add(planningProjectDimension);
        }
        planningProjectDimensionService.saveBatch(planningProjectDimensionList);

        // 生成企划看板 坑位信息
        List<PlanningProjectPlank> planningProjectPlanks = new ArrayList<>();
        for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
            if (ObjectUtil.isNotEmpty(planningProjectDimension.getNumber())) {
                for (int i = 0; i < Integer.parseInt(planningProjectDimension.getNumber()); i++) {
                    // 组装企划看板 坑位信息
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

    /**
     * 获取维度配置中的维度数据
     *
     * @param categoryPlanningDetailDTO 要保存的数据
     * @return
     */
    @Override
    public List<CategoryPlanningDetails> getDimensionality(CategoryPlanningDetailDTO categoryPlanningDetailDTO) {
        // 先根据品类企划 id 查询品类企划信息
        CategoryPlanning categoryPlanning = categoryPlanningService.getById(categoryPlanningDetailDTO.getCategoryPlanningId());
        if (ObjectUtil.isEmpty(categoryPlanning)) {
            throw new OtherException("品类企划信息不存在，请刷新后重试！");
        }
        String prodCategoryCode = categoryPlanningDetailDTO.getProdCategoryCode();
        if (ObjectUtil.isEmpty(prodCategoryCode)) {
            throw new OtherException("请选择品类数据！");
        }

        CategoryPlanningDetails details = getOne(
                new LambdaQueryWrapper<CategoryPlanningDetails>()
                        .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId())
                        .eq(CategoryPlanningDetails::getProdCategoryCode, prodCategoryCode)
                        .last("limit 1"));

        // 查询此品类所有的维度信息
        List<PlanningDimensionality> planningDimensionalityList = categoryPlanningService
                .getPlanningDimensionalitieList(CollUtil.newArrayList(details.getProdCategoryName()), categoryPlanning.getChannelCode(), categoryPlanning.getSeasonId());

        List<CategoryPlanningDetails> dynamicCategoryPlanningDetailsList = planningDimensionalityList.stream().map(
                item -> {
                    CategoryPlanningDetails categoryPlanningDetails = new CategoryPlanningDetails();
                    categoryPlanningDetails.setProdCategoryCode(categoryPlanningDetailDTO.getProdCategoryCode());
                    categoryPlanningDetails.setDimensionId(item.getFieldId());
                    categoryPlanningDetails.setDimensionName(item.getDimensionalityName());
                    categoryPlanningDetails.setDimensionalityGrade(item.getDimensionalityGrade());
                    categoryPlanningDetails.setDimensionalityGradeName(item.getDimensionalityGradeName());
                    return categoryPlanningDetails;
                }
        ).collect(Collectors.toList());
        return dynamicCategoryPlanningDetailsList;
    }

    /**
     * 获取已经存在的维度数据
     *
     * @param categoryPlanningDetailDTO
     * @param categoryPlanning
     * @return
     */
    public List<CategoryPlanningDetails> getDimensionalitySelf(CategoryPlanningDetailDTO categoryPlanningDetailDTO, CategoryPlanning categoryPlanning) {
        if (ObjectUtil.isEmpty(categoryPlanning)) {
            throw new OtherException("品类企划信息不存在，请刷新后重试！");
        }
        String prodCategoryCode = categoryPlanningDetailDTO.getProdCategoryCode();
        if (ObjectUtil.isEmpty(prodCategoryCode)) {
            throw new OtherException("请选择品类数据！");
        }
        LambdaQueryWrapper<CategoryPlanningDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanningDetailDTO.getCategoryPlanningId());
        queryWrapper.eq(CategoryPlanningDetails::getProdCategoryCode, prodCategoryCode);
        queryWrapper.select(
                CategoryPlanningDetails::getProdCategoryCode,
                CategoryPlanningDetails::getDimensionId,
                CategoryPlanningDetails::getDimensionName,
                CategoryPlanningDetails::getDimensionalityGrade,
                CategoryPlanningDetails::getDimensionalityGradeName
        );
        queryWrapper.isNotNull(CategoryPlanningDetails::getDimensionName);
        queryWrapper.ne(CategoryPlanningDetails::getDimensionName, "");
        queryWrapper.groupBy(CategoryPlanningDetails::getProdCategoryCode, CategoryPlanningDetails::getDimensionId);
        List<CategoryPlanningDetails> categoryPlanningDetailsList = list(queryWrapper);
        if (ObjectUtil.isEmpty(categoryPlanningDetailsList)) {
            throw new OtherException("当前品类类型数据不存在，请刷新后重试！");
        }
        return categoryPlanningDetailsList;
    }

    /**
     * @param categoryPlanningDetailDTO 要作废的数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void revocation(CategoryPlanningDetailDTO categoryPlanningDetailDTO) {
        // 先根据品类企划 id 查询品类企划信息
        CategoryPlanning categoryPlanning = categoryPlanningService.getById(categoryPlanningDetailDTO.getCategoryPlanningId());
        if (ObjectUtil.isEmpty(categoryPlanning)) {
            throw new OtherException("品类企划信息不存在，请刷新后重试！");
        }
        String prodCategoryCode = categoryPlanningDetailDTO.getProdCategoryCode();
        if (ObjectUtil.isEmpty(prodCategoryCode)) {
            throw new OtherException("请选择品类数据！");
        }
        // 查询品类企划详情 品类级别
        List<CategoryPlanningDetails> categoryPlanningDetailsList = list(
                new LambdaQueryWrapper<CategoryPlanningDetails>()
                        .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId())
                        .eq(CategoryPlanningDetails::getProdCategoryCode, prodCategoryCode)
        );

        if (ObjectUtil.isEmpty(categoryPlanningDetailsList)) {
            throw new OtherException("品类企划信息不存在，请刷新后重试！");
        }

        // 只有暂存的数据能被作废 其他的不允许作废
        long count = categoryPlanningDetailsList.stream().filter(item -> !"0".equals(item.getIsGenerate())).count();
        if (count > 0) {
            throw new OtherException("已审核/已作废数据无法作废");
        }

        for (CategoryPlanningDetails categoryPlanningDetails : categoryPlanningDetailsList) {
            // 设置成已作废的状态
            categoryPlanningDetails.setIsGenerate("2");
        }
        boolean updateBatchFlag = updateBatchById(categoryPlanningDetailsList);
        if (!updateBatchFlag) {
            throw new OtherException("作废失败，请重试！");
        }
    }

    /**
     * @param categoryPlanningDetailsList 要反审核的数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void reverseAudit(List<CategoryPlanningDetails> categoryPlanningDetailsList) {
        if (ObjectUtil.isEmpty(categoryPlanningDetailsList)) {
            throw new OtherException("反审核数据不能为空！");
        }
        // 根据 id 集合查询品类企划详情数据
        List<String> categoryPlanningDetailsIdList = categoryPlanningDetailsList.stream().map(CategoryPlanningDetails::getId).collect(Collectors.toList());
        List<CategoryPlanningDetails> detailsList = listByIds(categoryPlanningDetailsIdList);

        if (ObjectUtil.isEmpty(detailsList)) {
            throw new OtherException("品类企划数据不存在，请刷新后重试！");
        }

        List<String> dimensionNameList = detailsList.stream()
                .filter(item -> "2".equals(item.getIsGenerate()))
                .map(CategoryPlanningDetails::getDimensionName)
                .distinct()
                .collect(Collectors.toList());
        if (!dimensionNameList.isEmpty()) {
            // 说明此品类已经作废 无法反审核
            throw new OtherException("「" + CollUtil.join(dimensionNameList, ",") + "」维度数据已作废，无法反审核！");
        }

        // 判断保存的数据是否未审核
        List<String> stringList = categoryPlanningDetailsList.stream()
                .filter(item -> "0".equals(item.getIsGenerate()))
                .map(CategoryPlanningDetails::getDimensionName)
                .collect(Collectors.toList());
        if (!stringList.isEmpty()) {
            // 说明部分维度未审核 无法反审核
            throw new OtherException("「" + CollUtil.join(stringList, ",") + "」维度数据未审核，无法反审核！");
        }

        for (CategoryPlanningDetails details : detailsList) {
            // 将已审核的数据改成未审核状态
            details.setIsGenerate("0");
        }

        // 更新状态
        boolean updateFlag = updateBatchById(detailsList);
        if (!updateFlag) {
            throw new OtherException("反审核失败，请刷新后重试！");
        }

        // 根据 品类-中类-维度名 删除对应企划看板
        Map<String, List<CategoryPlanningDetails>> map = detailsList.stream()
                .collect(Collectors.groupingBy(item ->
                        item.getProdCategoryCode() + "-"
                                + item.getProdCategory2ndCode() + "-"
                                + item.getDimensionName() + "-"
                ));

        PlanningProject planningProject = planningProjectService.getOne(
                new LambdaQueryWrapper<PlanningProject>()
                        .eq(PlanningProject::getCategoryPlanningId, detailsList.get(0).getCategoryPlanningId())
        );
        if (ObjectUtil.isNotEmpty(planningProject)) {
            // 查询该企划看板该品类下的数据 缩小范围
            List<PlanningProjectDimension> projectDimensionList = planningProjectDimensionService.list(
                    new LambdaQueryWrapper<PlanningProjectDimension>()
                            .eq(PlanningProjectDimension::getPlanningProjectId, planningProject.getId())
                            .eq(PlanningProjectDimension::getProdCategoryCode, detailsList.get(0).getProdCategoryCode())
            );
            if (ObjectUtil.isNotEmpty(projectDimensionList)) {
                // 筛选出需要删除的企划看板数据
                List<PlanningProjectDimension> planningProjectDimensions = projectDimensionList.stream().filter(
                        item ->
                                ObjectUtil.isNotEmpty(map.get(item.getProdCategoryCode() + "-"
                                        + item.getProdCategory2ndCode() + "-"
                                        + item.getDimensionName() + "-"))

                ).collect(Collectors.toList());
                boolean removeFlag = planningProjectDimensionService.removeBatchByIds(planningProjectDimensions);
                if (!removeFlag) {
                    throw new OtherException("反审核失败，请刷新后重试！");
                }
            }
        }
    }

    /**
     * @param seasonalPlanningDetailsList 季节企划重新导入后 需要修改和需要新增的季节企划的数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DuplicationCheck
    public void updateBySeasonalPlanning(
            List<SeasonalPlanningDetails> seasonalPlanningDetailsList,
            List<SeasonalPlanningDetails> removeSeasonPlaningDetailList
    ) {
        if (ObjectUtil.isEmpty(seasonalPlanningDetailsList) && ObjectUtil.isEmpty(removeSeasonPlaningDetailList)) {
            throw new OtherException("更新数据不能为空！");
        }
        // 更新和新增的数据
        String seasonalPlanningId;
        if (ObjectUtil.isNotEmpty(seasonalPlanningDetailsList)) {
            // 拿到季节企划的 id
            seasonalPlanningId = seasonalPlanningDetailsList.get(0).getSeasonalPlanningId();
        } else {
            // 拿到季节企划的 id
            seasonalPlanningId = removeSeasonPlaningDetailList.get(0).getSeasonalPlanningId();
        }

        // 查询季节企划数据
        SeasonalPlanning seasonalPlanning = seasonalPlanningService.getById(seasonalPlanningId);
        if (ObjectUtil.isEmpty(seasonalPlanning)) {
            throw new OtherException("季节企划数据不存在，请刷新后重试！");
        }
        // 根据季节企划的 id 查询启用的品类企划
        CategoryPlanning categoryPlanning = categoryPlanningService.getOne(
                new LambdaQueryWrapper<CategoryPlanning>()
                        .eq(CategoryPlanning::getSeasonalPlanningId, seasonalPlanningId)
                        .eq(CategoryPlanning::getDelFlag, BaseGlobal.NO)
                        .eq(CategoryPlanning::getStatus, "0")
        );
        if (ObjectUtil.isEmpty(categoryPlanning)) {
            throw new OtherException("暂无已启用的品类企划需要更新！");
        }

        // 更新和新增的数据
        if (ObjectUtil.isNotEmpty(seasonalPlanningDetailsList)) {
            // 拿到需要更新或者新增的品类 因为数据不会为空 所以品类也不会为空
            List<String> prodCategoryCodeList = seasonalPlanningDetailsList
                    .stream().map(SeasonalPlanningDetails::getProdCategoryCode).distinct().collect(Collectors.toList());

            // 查询此品类下的品类企划 只能查已撤回的数据
            List<CategoryPlanningDetails> categoryPlanningDetailsList = list(
                    new LambdaQueryWrapper<CategoryPlanningDetails>()
                            .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId())
                            .in(CategoryPlanningDetails::getProdCategoryCode, prodCategoryCodeList)
                            .eq(CategoryPlanningDetails::getDelFlag, BaseGlobal.NO)
            );

            if (ObjectUtil.isNotEmpty(categoryPlanningDetailsList)) {
                // 只要有一个不是已撤回的数据 那么报错
                if (categoryPlanningDetailsList.stream().anyMatch(item -> !"2".equals(item.getIsGenerate()))) {
                    throw new OtherException("未作废数据无法更新，请刷新后重试！");
                }

                // 如果没有已撤回的数据那么删除这些数据 然后进行新增
                boolean removeFlag = removeBatchByIds(categoryPlanningDetailsList);
                if (!removeFlag) {
                    throw new OtherException("更新品类企划失败，请刷新后重试！");
                }
            }

            // 组装生成要新增的品类企划详情数据
            List<CategoryPlanningDetails> newCategoryPlanningDetailsList = categoryPlanningService.generationCategoryPlanningDetails(
                    seasonalPlanningDetailsList,
                    seasonalPlanning,
                    categoryPlanning,
                    null
            );

            if (ObjectUtil.isNotEmpty(newCategoryPlanningDetailsList)) {
                boolean saveFlag = saveBatch(newCategoryPlanningDetailsList);
                if (!saveFlag) {
                    throw new OtherException("更新品类企划失败，请刷新后重试！");
                }
            }
        }
        // 删除的数据
        if (ObjectUtil.isNotEmpty(removeSeasonPlaningDetailList)) {
            // 按照品类-中类删除数据
            List<String> prodCategory2ndCodeList = removeSeasonPlaningDetailList
                    .stream().map(SeasonalPlanningDetails::getProdCategory2ndCode)
                    .distinct().collect(Collectors.toList());
            // 查询需要删除的数据
            List<CategoryPlanningDetails> categoryPlanningDetailsList = list(
                    new LambdaQueryWrapper<CategoryPlanningDetails>()
                            .eq(CategoryPlanningDetails::getCategoryPlanningId, categoryPlanning.getId())
                            .in(CategoryPlanningDetails::getProdCategory2ndCode, prodCategory2ndCodeList)
                            .eq(CategoryPlanningDetails::getDelFlag, BaseGlobal.NO)
            );
            if (ObjectUtil.isNotEmpty(categoryPlanningDetailsList)) {
                boolean removeFlag = removeBatchByIds(categoryPlanningDetailsList);
                if (!removeFlag) {
                    throw new OtherException("更新品类企划失败，请刷新后重试！");
                }
            }
        }
    }

    /**
     * 组装企划看板维度数据
     *
     * @param planningDetails 品类企划详情数据
     * @param planningProject 企划看板数据
     * @return 组装后的企划看板维度数据
     */
    private PlanningProjectDimension assemblePlanningProjectDimension(CategoryPlanningDetails planningDetails, PlanningProject planningProject) {
        // 组装企划看板维度数据
        PlanningProjectDimension planningProjectDimension = new PlanningProjectDimension();
        planningProjectDimension.setPlanningProjectId(planningProject.getId());
        planningProjectDimension.setBandName(planningDetails.getBandName());
        planningProjectDimension.setBandCode(planningDetails.getBandCode());
        planningProjectDimension.setNumber(planningDetails.getNumber());
        planningProjectDimension.setProdCategory1stCode(planningDetails.getProdCategory1stCode());
        planningProjectDimension.setProdCategory1stName(planningDetails.getProdCategory1stName());
        planningProjectDimension.setProdCategory2ndCode(planningDetails.getProdCategory2ndCode());
        planningProjectDimension.setProdCategory2ndName(planningDetails.getProdCategory2ndName());
        planningProjectDimension.setCategoryPlanningDetailsId(planningDetails.getId());
        planningProjectDimension.setProdCategoryCode(planningDetails.getProdCategoryCode());
        planningProjectDimension.setProdCategoryName(planningDetails.getProdCategoryName());
        planningProjectDimension.setDimensionId(planningDetails.getDimensionId());
        planningProjectDimension.setDimensionName(planningDetails.getDimensionName());
        planningProjectDimension.setDimensionCode(planningDetails.getDimensionCode());
        planningProjectDimension.setDimensionValue(planningDetails.getDimensionValue());
        planningProjectDimension.setDimensionTypeCode(planningDetails.getDimensionTypeCode());
        planningProjectDimension.setDimensionalityGrade(planningDetails.getDimensionalityGrade());
        planningProjectDimension.setDimensionalityGradeName(planningDetails.getDimensionalityGradeName());
        return planningProjectDimension;
    }

    /**
     * 构造查询
     */
    public BaseQueryWrapper<CategoryPlanningDetails> buildQueryWrapper(CategoryPlanningDetailsQueryDto dto) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tcpd.category_planning_id", dto.getCategoryPlanningId());
        queryWrapper.notEmptyEq("tcpd.seasonal_planning_id", dto.getSeasonalPlanningId());
        queryWrapper.notEmptyIn("tcpd.prod_category_name", dto.getProdCategoryNames());
        return queryWrapper;
    }


}
