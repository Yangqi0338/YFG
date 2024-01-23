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
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectMaxCategory;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningDetailsMapper;
import com.base.sbc.module.planningproject.service.CategoryPlanningDetailsService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CategoryPlanningDetailsServiceImpl extends BaseServiceImpl<CategoryPlanningDetailsMapper,CategoryPlanningDetails> implements CategoryPlanningDetailsService {
    private final PlanningDimensionalityService planningDimensionalityService;
    private final FieldOptionConfigService fieldOptionConfigService;
    private final PlanningProjectService planningProjectService;
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
                                    // jsonObject.put("skuCount",categoryPlanningDetailsVo.getSkcCount());
                                    jsonObject.put("prodCategory1stName", prodCategory1stName);
                                    jsonObject.put("prodCategory1stCode", prodCategory1stCode);

                                    jsonObject.put("prodCategoryCode", fieldOptionConfig.getCategoryCode());
                                    jsonObject.put("prodCategoryName", fieldOptionConfig.getCategoryName());

                                    jsonObject.put("prodCategory2ndCode", fieldOptionConfig.getProdCategory2nd());
                                    jsonObject.put("prodCategory2ndName", fieldOptionConfig.getProdCategory2ndName());
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
        //如果已经存在有关联的企划看板,则不做操作
        QueryWrapper<PlanningProject> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("season_id",categoryPlanningDetailsVo.getSeasonId());
        queryWrapper.eq("planning_channel_code",categoryPlanningDetailsVo.getChannelCode());
        queryWrapper.eq("status",categoryPlanningDetailsVo.getCategoryPlanningStatus());
        if (planningProjectService.count(queryWrapper)>0){
            throw  new RuntimeException("企划看板数据已经存在,无法修改");
        }

        boolean b = this.updateById(categoryPlanningDetailsVo);

        PlanningProjectSaveDTO planningProjectSaveDTO = new PlanningProjectSaveDTO();
        planningProjectSaveDTO.setPlanningProjectName(categoryPlanningDetailsVo.getCategoryPlanningName());
        planningProjectSaveDTO.setPlanningChannelCode(categoryPlanningDetailsVo.getChannelCode());
        planningProjectSaveDTO.setPlanningChannelName(categoryPlanningDetailsVo.getChannelName());
        planningProjectSaveDTO.setSeasonId(categoryPlanningDetailsVo.getSeasonId());
        planningProjectSaveDTO.setSeasonName(categoryPlanningDetailsVo.getSeasonName());
        planningProjectSaveDTO.setStatus(categoryPlanningDetailsVo.getCategoryPlanningStatus());

        List<PlanningProjectMaxCategory> planningProjectMaxCategoryList = new ArrayList<>();
        List<PlanningProjectDimension> planningProjectDimensionList = new ArrayList<>();

        String dataJson = categoryPlanningDetailsVo.getDataJson();
        if (StringUtils.isNotBlank(dataJson)){
            JSONArray jsonArray = JSON.parseArray(dataJson);
            List<String> prodCategory1stNames = new ArrayList<>();
            List<String> prodCategory1stCodes = new ArrayList<>();
            Map<String,String> map = new HashMap<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String prodCategory1stName = jsonObject.getString("prodCategory1stName");
                prodCategory1stNames.add(prodCategory1stName);
                String prodCategory1ndCode = jsonObject.getString("prodCategory1ndCode");
                map.put(prodCategory1ndCode,jsonObject.getString("prodCategory1ndSum"));
                prodCategory1stCodes.add(prodCategory1ndCode);

                PlanningProjectDimension planningProjectDimension =new PlanningProjectDimension();
                planningProjectDimension.setProdCategory1stCode(jsonObject.getString("prodCategory1stCode"));
                planningProjectDimension.setProdCategory1stName(jsonObject.getString("prodCategory1stName"));
                planningProjectDimension.setProdCategory2ndCode(jsonObject.getString("prodCategory2ndCode"));
                planningProjectDimension.setProdCategory2ndName(jsonObject.getString("prodCategory2ndName"));
                planningProjectDimension.setProdCategoryCode(jsonObject.getString("prodCategoryCode"));
                planningProjectDimension.setProdCategoryName(jsonObject.getString("prodCategoryName"));
                planningProjectDimension.setNumber(jsonObject.getString("num"));


                planningProjectDimension.setDimensionId(jsonObject.getString("dimensionId"));
                planningProjectDimension.setDimensionName(jsonObject.getString("dimensionName"));

                planningProjectDimension.setDimensionCode(jsonObject.getString("dimensionCode"));
                planningProjectDimension.setDimensionValue(jsonObject.getString("dimensionValue"));
                planningProjectDimension.setDimensionTypeCode(jsonObject.getString("dimensionTypeCode"));

                planningProjectDimensionList.add(planningProjectDimension);


            }
            //去重
            List<String> collect1 = prodCategory1stNames.stream().distinct().collect(Collectors.toList());
            List<String> collect2 = prodCategory1stCodes.stream().distinct().collect(Collectors.toList());

            for (int i = 0; i < collect1.size(); i++) {
                PlanningProjectMaxCategory planningProjectMaxCategory = new PlanningProjectMaxCategory();
                planningProjectMaxCategory.setProdCategory1stName(collect1.get(i));
                planningProjectMaxCategory.setProdCategory1stCode(collect2.get(i));
                planningProjectMaxCategory.setNumber(map.get(collect2.get(i)));
                planningProjectMaxCategoryList.add(planningProjectMaxCategory);
            }


            planningProjectSaveDTO.setPlanningProjectDimensionList(planningProjectDimensionList);
            planningProjectSaveDTO.setPlanningProjectMaxCategoryList(planningProjectMaxCategoryList);
            // planningProjectService.save(planningProjectSaveDTO);

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
