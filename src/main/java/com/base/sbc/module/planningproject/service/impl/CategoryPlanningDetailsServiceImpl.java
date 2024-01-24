package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldOptionConfig;
import com.base.sbc.module.formtype.service.FieldOptionConfigService;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.dto.CategoryPlanningDetailsQueryDto;
import com.base.sbc.module.planningproject.dto.PlanningProjectSaveDTO;
import com.base.sbc.module.planningproject.entity.*;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningDetailsMapper;
import com.base.sbc.module.planningproject.service.*;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2024-01-22 11:29:23
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class CategoryPlanningDetailsServiceImpl extends BaseServiceImpl<CategoryPlanningDetailsMapper,CategoryPlanningDetails> implements CategoryPlanningDetailsService {
    private final PlanningDimensionalityService planningDimensionalityService;
    private final FieldOptionConfigService fieldOptionConfigService;
    private final PlanningProjectService planningProjectService;

    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectPlankService planningProjectPlankService;
    @Resource
    @Lazy
    private  CategoryPlanningService categoryPlanningService;

    @Override
    public PageInfo<CategoryPlanningDetailsVo> queryPage(CategoryPlanningDetailsQueryDto dto) {
        PageHelper.startPage(dto);
        List<CategoryPlanningDetailsVo> categoryPlanningDetailsVos = this.queryList(dto);
        return new PageInfo<>(categoryPlanningDetailsVos);
    }

    @Override
    public List<CategoryPlanningDetailsVo> queryList(CategoryPlanningDetailsQueryDto dto) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper = this.buildQueryWrapper(dto);
        return  this.baseMapper.listByQueryWrapper(queryWrapper);
    }

    @Override
    public CategoryPlanningDetailsVo getDetailById(String id) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.eq("tcpd.id",id);
        List<CategoryPlanningDetailsVo> categoryPlanningDetailsVos = this.baseMapper.listByQueryWrapper(queryWrapper);
        if(!categoryPlanningDetailsVos.isEmpty()){

            CategoryPlanningDetailsVo categoryPlanningDetailsVo = categoryPlanningDetailsVos.get(0);


            String bandName = categoryPlanningDetailsVo.getBandName();
            String prodCategoryName = categoryPlanningDetailsVo.getProdCategoryName();
            String prodCategory2ndName = categoryPlanningDetailsVo.getProdCategory2ndName();
            String prodCategory2ndCode = categoryPlanningDetailsVo.getProdCategory2ndCode();
            String skcCount = categoryPlanningDetailsVo.getSkcCount();
            JSONObject object = new JSONObject();
            String[] split = skcCount.split(",");
            int i = 0;
            for (String string : bandName.split(",")) {
                for (String s : prodCategoryName.split(",")) {
                    if (StringUtils.isBlank(prodCategory2ndName)) {
                        String s1 = object.getString(string + "-" + s);
                        if (StringUtils.isBlank(s1)){
                            object.put(string + "-" + s ,split[i]);
                        }else {
                            int i1 = Integer.parseInt(s1);
                            i1 = i1 + Integer.parseInt(split[i]);
                            object.put(string + "-" + s , String.valueOf(i1));
                        }

                        i++;
                    } else {
                        for (String s1 : prodCategory2ndName.split(",")) {

                            String s2 = object.getString(string + "-" + s+ "-" + s1);
                            if (StringUtils.isBlank(s2)){
                                object.put(string + "-" + s + "-" + s1, split[i]);
                            }else {
                                int i1 = Integer.parseInt(s2);
                                i1 = i1 + Integer.parseInt(split[i]);

                                object.put(string + "-" + s + "-" + s1, String.valueOf(i1));
                            }
                            i++;
                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(categoryPlanningDetailsVo.getDataJson())){
                return categoryPlanningDetailsVo;
            }
            //获取第一维度与纬度值
            String seasonId = categoryPlanningDetailsVo.getSeasonId();
            String prodCategory1stCode = categoryPlanningDetailsVo.getProdCategory1stCode();
            String prodCategory1stName = categoryPlanningDetailsVo.getProdCategory1stName();
            String prodCategoryCode = categoryPlanningDetailsVo.getProdCategoryCode();
            String channelCode = categoryPlanningDetailsVo.getChannelCode();

            QueryWrapper<PlanningDimensionality> queryWrapper1 =new QueryWrapper<>();
            queryWrapper1.eq("channel",channelCode);
            queryWrapper1.eq("planning_season_id",seasonId);
            queryWrapper1.eq("prod_category1st",prodCategory1stCode);
            queryWrapper1.eq("prod_category",prodCategoryCode);
            queryWrapper1.eq("dimensionality_grade","1");
            if (StringUtils.isNotBlank(prodCategory2ndCode)){
                String[] split1 = prodCategory2ndCode.split(",");
                queryWrapper1.in("prod_category2nd",Arrays.asList(split1));
            }
            List<JSONObject> jsonObjects =new ArrayList<>();
            List<PlanningDimensionality> list = planningDimensionalityService.list(queryWrapper1);
            if (!list.isEmpty()){
                for (PlanningDimensionality planningDimensionality : list) {
                    //获取配置
                    QueryWrapper<FieldOptionConfig> queryWrapper2 =new QueryWrapper<>();
                    queryWrapper2.eq("field_management_id",planningDimensionality.getFieldId());
                    List<FieldOptionConfig> list1 = fieldOptionConfigService.list(queryWrapper2);
                    if (!list1.isEmpty()){
                        List<String> nameList = list1.stream().map(FieldOptionConfig::getOptionName).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());;
                        // List<String> codeList =  list1.stream().map(FieldOptionConfig::getOptionCode).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                        // List<String> ids =  list1.stream().map(FieldOptionConfig::getFieldManagementId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());

                        for (String s : nameList) {
                            for (FieldOptionConfig fieldOptionConfig : list1) {
                                if (fieldOptionConfig.getOptionName().equals(s)) {
                                    JSONObject jsonObject = new JSONObject();
                                    //     dimensionTypeCode
                                    jsonObject.put("dimensionCode", fieldOptionConfig.getOptionName());
                                    jsonObject.put("dimensionValue", fieldOptionConfig.getOptionCode());
                                    jsonObject.put("dimensionId", planningDimensionality.getFieldId());
                                    jsonObject.put("dimensionName", planningDimensionality.getDimensionalityName());
                                    jsonObject.put("dimensionTypeCode", fieldOptionConfig.getFormTypeId());
                                    jsonObject.put("skuCount",categoryPlanningDetailsVo.getSkcCount());
                                    jsonObject.put("prodCategory1stName", prodCategory1stName);
                                    jsonObject.put("prodCategory1stCode", prodCategory1stCode);
                                    jsonObject.put("maxSum",object.toJSONString());
                                    jsonObject.put("prodCategoryCode", fieldOptionConfig.getCategoryCode());
                                    jsonObject.put("prodCategoryName", fieldOptionConfig.getCategoryName());

                                    jsonObject.put("prodCategory2ndCode", planningDimensionality.getProdCategory2nd());
                                    jsonObject.put("prodCategory2ndName", planningDimensionality.getProdCategory2ndName());
                                    jsonObjects.add(jsonObject);
                                    break;
                                }
                            }

                        }

                    }
                }
                categoryPlanningDetailsVo.setDataJson(JSON.toJSONString(jsonObjects,SerializerFeature.WriteNullStringAsEmpty));
            }



            return categoryPlanningDetailsVo;
        }
        throw new RuntimeException("未找到数据");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDetail(CategoryPlanningDetailsVo categoryPlanningDetailsVo) {

        CategoryPlanningDetails categoryPlanningDetails1 = this.getById(categoryPlanningDetailsVo.getId());
        if (StringUtils.isNotBlank(categoryPlanningDetails1.getDataJson())){
            throw  new RuntimeException("数据已经存在,无法修改");
        }

        CategoryPlanning categoryPlanning = categoryPlanningService.getById(categoryPlanningDetailsVo.getCategoryPlanningId());
        QueryWrapper<PlanningProject> queryWrapper =new QueryWrapper<>();

        //如果不存在相关的企划看板,则创建
        queryWrapper.eq("category_planning_id",categoryPlanningDetails1.getId());
        PlanningProject planningProject = planningProjectService.getOne(queryWrapper);
        if (planningProject==null){
            QueryWrapper<PlanningProject> queryWrapper1 = new QueryWrapper<>();
            queryWrapper.eq("status", "0");
            queryWrapper.eq("seasonal_id", categoryPlanning.getSeasonId());
            queryWrapper.eq("channel_code", categoryPlanning.getChannelCode());
            queryWrapper.eq("company_code", categoryPlanning.getCompanyCode());
            long l1 = planningProjectService.count(queryWrapper1);


            planningProject =new PlanningProject();
            planningProject.setCategoryPlanningId(categoryPlanning.getId());
            planningProject.setSeasonId(categoryPlanning.getSeasonId());
            planningProject.setSeasonName(categoryPlanning.getSeasonName());
            planningProject.setPlanningChannelCode(categoryPlanning.getChannelCode());
            planningProject.setPlanningChannelName(categoryPlanning.getChannelName());
            planningProject.setPlanningProjectName(categoryPlanningDetailsVo.getCategoryPlanningName());
            planningProject.setStatus(l1 == 0 ? "0" : "1");
            planningProjectService.save(planningProject);
        }

        //修改数据
        boolean b = this.updateById(categoryPlanningDetailsVo);



        List<PlanningProjectDimension> planningProjectDimensionList = new ArrayList<>();

        String dataJson = categoryPlanningDetailsVo.getDataJson();
        if (StringUtils.isNotBlank(dataJson)){
            JSONArray jsonArray = JSON.parseArray(dataJson);
            // List<String> prodCategory1stNames = new ArrayList<>();
            // List<String> prodCategory1stCodes = new ArrayList<>();
            // Map<String,String> map = new HashMap<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject band = jsonObject.getJSONObject("band");
                if(band!=null){
                    JSONArray bandNames = band.getJSONArray("bandName");
                    JSONArray bandCodes = band.getJSONArray("bandCode");
                    JSONArray numbers = band.getJSONArray("number");
                    for (int i1 = 0; i1 < bandNames.size(); i1++) {
                        String bandName = bandNames.getString(i);
                        String bandCode = bandCodes.getString(i);
                        String number = numbers.getString(i);

                        //企划看板维度数据
                        PlanningProjectDimension planningProjectDimension =new PlanningProjectDimension();

                        planningProjectDimension.setPlanningProjectId(planningProject.getId());
                        planningProjectDimension.setBandName(bandName);
                        planningProjectDimension.setBandCode(bandCode);
                        planningProjectDimension.setNumber(number);

                        planningProjectDimension.setProdCategory1stCode(jsonObject.getString("prodCategory1stCode"));
                        planningProjectDimension.setProdCategory1stName(jsonObject.getString("prodCategory1stName"));
                        planningProjectDimension.setProdCategory2ndCode(jsonObject.getString("prodCategory2ndCode"));
                        planningProjectDimension.setProdCategory2ndName(jsonObject.getString("prodCategory2ndName"));
                        planningProjectDimension.setProdCategoryCode(jsonObject.getString("prodCategoryCode"));
                        planningProjectDimension.setProdCategoryName(jsonObject.getString("prodCategoryName"));


                        planningProjectDimension.setDimensionId(jsonObject.getString("dimensionId"));
                        planningProjectDimension.setDimensionName(jsonObject.getString("dimensionName"));

                        planningProjectDimension.setDimensionCode(jsonObject.getString("dimensionCode"));
                        planningProjectDimension.setDimensionValue(jsonObject.getString("dimensionValue"));
                        planningProjectDimension.setDimensionTypeCode(jsonObject.getString("dimensionTypeCode"));


                        planningProjectDimensionList.add(planningProjectDimension);
                        planningProjectDimensionService.save(planningProjectDimension);

                    }
                }
                //生成坑位
                for (PlanningProjectDimension planningProjectDimension : planningProjectDimensionList) {
                    List<PlanningProjectPlank> planningProjectPlanks = new ArrayList<>();
                    for (int j = 0; j <  Integer.parseInt(planningProjectDimension.getNumber()); j++) {
                        PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                        planningProjectPlank.setPlanningProjectId(categoryPlanning.getId());
                        planningProjectPlank.setMatchingStyleStatus("0");
                        planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
                        planningProjectPlank.setBandName(planningProjectDimension.getBandName());
                        planningProjectPlank.setPlanningProjectDimensionId(planningProjectDimension.getId());
                        planningProjectPlanks.add(planningProjectPlank);
                    }
                    planningProjectPlankService.saveBatch(planningProjectPlanks);
                }
            }
        }

        return b;
    }

    /**
     * 构造查询期
     */
    public BaseQueryWrapper<CategoryPlanningDetails> buildQueryWrapper(CategoryPlanningDetailsQueryDto dto) {
        BaseQueryWrapper<CategoryPlanningDetails> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tcpd.category_planning_id",dto.getCategoryPlanningId());
        queryWrapper.notEmptyEq("tcpd.seasonal_planning_id",dto.getSeasonalPlanningId());
        return queryWrapper;
    }


}
