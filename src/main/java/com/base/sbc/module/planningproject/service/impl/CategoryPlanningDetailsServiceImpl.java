package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
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
import com.base.sbc.module.planningproject.entity.CategoryPlanningDetails;
import com.base.sbc.module.planningproject.mapper.CategoryPlanningDetailsMapper;
import com.base.sbc.module.planningproject.service.CategoryPlanningDetailsService;
import com.base.sbc.module.planningproject.vo.CategoryPlanningDetailsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
                        List<String> codeList =  list1.stream().map(FieldOptionConfig::getOptionCode).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                        for (int i = 0; i < nameList.size(); i++) {
                            for (FieldOptionConfig fieldOptionConfig : list1) {
                                if (fieldOptionConfig.getOptionName().equals(nameList.get(i))){
                                    JSONObject jsonObject =new JSONObject();

                                    jsonObject.put("name",nameList.get(i));
                                    jsonObject.put("code",codeList.get(i));
                                    // jsonObject.put("skuCount",categoryPlanningDetailsVo.getSkcCount());
                                    jsonObject.put("prodCategory1stName",prodCategory1stName);
                                    jsonObject.put("prodCategory1stCode",prodCategory1stCode);

                                    jsonObject.put("prodCategoryCode",fieldOptionConfig.getCategoryCode());
                                    jsonObject.put("prodCategoryName",fieldOptionConfig.getCategoryName());

                                    jsonObject.put("prodCategory2ndCode",fieldOptionConfig.getProdCategory2nd());
                                    jsonObject.put("prodCategory2ndName",fieldOptionConfig.getProdCategory2ndName());
                                    jsonObjects.add(jsonObject);
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
