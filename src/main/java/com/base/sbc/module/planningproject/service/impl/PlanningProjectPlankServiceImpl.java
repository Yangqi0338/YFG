package com.base.sbc.module.planningproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.StylePicUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.service.BasicsdatumColourLibraryService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FieldVal;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FieldValService;
import com.base.sbc.module.formtype.utils.FieldValDataGroupConstant;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planningproject.dto.PlanningProjectPlankPageDto;
import com.base.sbc.module.planningproject.entity.PlanningProject;
import com.base.sbc.module.planningproject.entity.PlanningProjectDimension;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlank;
import com.base.sbc.module.planningproject.entity.PlanningProjectPlankDimension;
import com.base.sbc.module.planningproject.mapper.PlanningProjectPlankMapper;
import com.base.sbc.module.planningproject.service.PlanningProjectDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankDimensionService;
import com.base.sbc.module.planningproject.service.PlanningProjectPlankService;
import com.base.sbc.module.planningproject.service.PlanningProjectService;
import com.base.sbc.module.planningproject.vo.PlanningProjectPlankVo;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.vo.StyleColorVo;
import com.base.sbc.module.tablecolumn.vo.TableColumnVo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/11/17 9:45:19
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
public class PlanningProjectPlankServiceImpl extends BaseServiceImpl<PlanningProjectPlankMapper, PlanningProjectPlank> implements PlanningProjectPlankService {
    private final StyleColorService styleColorService;
    private final StylePicUtils stylePicUtils;
    private final StylePricingMapper stylePricingMapper;
    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;
    private final FieldValService fieldValService;
    private final PlanningProjectDimensionService planningProjectDimensionService;
    private final PlanningProjectPlankDimensionService planningProjectPlankDimensionService;
    private final PlanningDimensionalityService planningDimensionalityService;
    private final FieldManagementService fieldManagementService;
    @Resource
    @Lazy
    private  PlanningProjectService planningProjectService ;
    @Override
    public  Map<String,Object> ListByDto(PlanningProjectPlankPageDto dto) {
        Map<String,Object> hashMap =new HashMap<>();
        BaseQueryWrapper<PlanningProjectPlank> queryWrapper =new BaseQueryWrapper<>();
        queryWrapper.notEmptyEq("tppp.planning_project_id",dto.getPlanningProjectId());
        queryWrapper.notEmptyEq("tppd.prod_category_code",dto.getProdCategoryCode());
        queryWrapper.notEmptyEq("tppd.prod_category2nd_code",dto.getProdCategory2ndCode());
        queryWrapper.notEmptyEq("tppd.prod_category1st_code",dto.getProdCategory1stCode());
        if (StringUtils.isNotEmpty(dto.getPlanningBandCode())) {
            String[] split = dto.getPlanningBandCode().split(",");
            queryWrapper.in("tppp.band_code", Arrays.asList(split));
        }

        queryWrapper.notEmptyEq("tppp.bulk_style_no",dto.getPlanningBulkStyleNo());
        queryWrapper.orderBy(true,true,"tppp.band_code");

        PlanningProject planningProject = planningProjectService.getById(dto.getPlanningProjectId());

        List<PlanningProjectPlankVo> list = this.baseMapper.queryPage(queryWrapper);
        List<String> styleColorIds = list.stream().map(PlanningProjectPlankVo::getStyleColorId).collect(Collectors.toList());
        List<FieldVal> fieldValList = styleColorService.ListDynamicDataByIds(styleColorIds);
        //匹配
        // this.match(list);
        List<TableColumnVo> tableColumnVos =new ArrayList<>();
        Map<String, Integer> map =new TreeMap<>();
        Map<String, JSONObject> map1 =new TreeMap<>();

        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            //获取所有波段,当作列
            if (StringUtils.isNotEmpty(planningProjectPlankVo.getBandName())){
                Integer i = map.get(planningProjectPlankVo.getBandName()+","+planningProjectPlankVo.getBandCode());
                if (i == null) {
                    if (StringUtils.isNotEmpty(planningProjectPlankVo.getId())){
                        map.put(planningProjectPlankVo.getBandName()+","+planningProjectPlankVo.getBandCode(), 1);
                    }else {
                        map.put(planningProjectPlankVo.getBandName()+","+planningProjectPlankVo.getBandCode(), 0);
                    }
                }else {
                    if (StringUtils.isNotEmpty(planningProjectPlankVo.getId())){
                        i++;
                    }
                    map.put(planningProjectPlankVo.getBandName()+","+planningProjectPlankVo.getBandCode(), i);
                }
            }
            //获取所有的第一维度
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dimensionId", planningProjectPlankVo.getDimensionId());
            jsonObject.put("dimensionValue", planningProjectPlankVo.getDimensionValue());
            jsonObject.put("dimensionName", planningProjectPlankVo.getDimensionName());
            jsonObject.put("dimensionCode", planningProjectPlankVo.getDimensionCode());
            jsonObject.put("planningProjectDimensionId",planningProjectPlankVo.getPlanningProjectDimensionId());
            jsonObject.put("bandName",planningProjectPlankVo.getBandName());
            jsonObject.put("bandCode",planningProjectPlankVo.getBandCode());

            map1.put(planningProjectPlankVo.getDimensionValue()+planningProjectPlankVo.getDimensionCode()+planningProjectPlankVo.getDimensionName()+planningProjectPlankVo.getDimensionId(), jsonObject);
            // map2.put(planningProjectPlankVo.getDimensionValue()+planningProjectPlankVo.getDimensionCode()+planningProjectPlankVo.getDimensionName()+planningProjectPlankVo.getDimensionId()+planningProjectPlankVo.getBandName()+planningProjectPlankVo.getBandCode(), jsonObject);


            if (StringUtils.isNotEmpty(planningProjectPlankVo.getStyleColorId())) {
                List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(planningProjectPlankVo.getStyleColorId());
                planningProjectPlankVo.setFieldManagementVos(fieldManagementVos);
            }

            if (StringUtils.isNotEmpty(planningProjectPlankVo.getHisDesignNo())) {
                StyleColor styleColor = styleColorService.getOne(new QueryWrapper<StyleColor>().eq("style_no", planningProjectPlankVo.getHisDesignNo()).select("id","style_color_pic"));
                StyleColorVo styleColorVo =new StyleColorVo();
                BeanUtil.copyProperties(styleColor,styleColorVo);
                String styleUrl = stylePicUtils.getStyleUrl(styleColorVo.getStyleColorPic());
                styleColorVo.setStyleColorPic(styleUrl);
                planningProjectPlankVo.setOldStyleColor(styleColorVo);
            }

            //获取当前配置的维度字段
            DimensionLabelsSearchDto dimensionLabelsSearchDto =new DimensionLabelsSearchDto();
            dimensionLabelsSearchDto.setPlanningSeasonId(planningProject.getSeasonId());
            dimensionLabelsSearchDto.setProdCategory(planningProjectPlankVo.getProdCategoryCode());

            dimensionLabelsSearchDto.setChannel(planningProject.getPlanningChannelCode());
            if (StringUtils.isNotEmpty(planningProjectPlankVo.getProdCategory2ndCode())){
                dimensionLabelsSearchDto.setCategoryFlag("0");
                dimensionLabelsSearchDto.setProdCategory2nd(planningProjectPlankVo.getProdCategory2ndCode());
            }
            List<PlanningDimensionality> planningDimensionalities = planningDimensionalityService.getDimensionalityList(dimensionLabelsSearchDto).getPlanningDimensionalities();
            //查询字段管理配置
            List<String> fieldManagementIds = planningDimensionalities.stream().map(PlanningDimensionality::getFieldId).collect(Collectors.toList());
            List<FieldManagement> fieldManagements = fieldManagementService.listByIds(fieldManagementIds);
            Map<String, FieldManagement> fieldManagementMap = fieldManagements.stream().collect(Collectors.toMap(FieldManagement::getId, f -> f));

            //获取维度列表
            QueryWrapper<PlanningProjectPlankDimension> dimensionQueryWrapper = new QueryWrapper<>();
            dimensionQueryWrapper.eq("planning_project_plank_id", planningProjectPlankVo.getId());
            List<PlanningProjectPlankDimension> list1 = planningProjectPlankDimensionService.list(dimensionQueryWrapper);

            List<PlanningProjectPlankDimension> list2=new ArrayList<>();
            Map<String, PlanningProjectPlankDimension> plankDimensionMap = list1.stream().collect(Collectors.toMap(PlanningProjectPlankDimension::getDimensionName, planningProjectPlankDimension -> planningProjectPlankDimension));
            for (PlanningDimensionality planningDimensionality : planningDimensionalities) {
                PlanningProjectPlankDimension planningProjectPlankDimension = plankDimensionMap.get(planningDimensionality.getDimensionalityName());
                if (planningProjectPlankDimension == null){
                    planningProjectPlankDimension=new PlanningProjectPlankDimension();
                    planningProjectPlankDimension.setPlanningProjectPlankId(planningProjectPlankVo.getId());
                    planningProjectPlankDimension.setDimensionName(planningDimensionality.getDimensionalityName());
                    planningProjectPlankDimension.setDimensionalityGrade(planningDimensionality.getDimensionalityGrade());
                    planningProjectPlankDimension.setDimensionalityGradeName(planningDimensionality.getDimensionalityGradeName());
                    //如果没有编辑就从配色取,
                    for (FieldVal fieldVal : fieldValList) {
                        String styleColorId;
                        if ("0".equals(planningProjectPlankVo.getMatchingStyleStatus())){
                            break;
                        }
                        if("3".equals(planningProjectPlankVo.getMatchingStyleStatus())){
                            styleColorId=planningProjectPlankVo.getOldStyleColor().getId();
                        }else {
                            styleColorId=planningProjectPlankVo.getStyleColorId();
                        }
                        if (StringUtils.isNotBlank(fieldVal.getForeignId()) && fieldVal.getForeignId().equals(styleColorId) && planningDimensionality.getDimensionalityName().equals(fieldVal.getFieldExplain())){
                            planningProjectPlankDimension.setDimensionCode(fieldVal.getFieldName());
                            planningProjectPlankDimension.setDimensionValue(fieldVal.getVal());
                            break;
                        }
                    }
                }
                FieldManagement fieldManagement = fieldManagementMap.get(planningDimensionality.getFieldId());
                planningProjectPlankDimension.setGroupName(fieldManagement.getGroupName());
                planningProjectPlankDimension.setFieldManagement(fieldManagement);
                list2.add(planningProjectPlankDimension);
            }


            Map<String, List<PlanningProjectPlankDimension>> groupedList = list2.stream()
                    .collect(Collectors.groupingBy(PlanningProjectPlankDimension::getGroupName));

            planningProjectPlankVo.setDimensionList(groupedList);
        }

        //生成表格列
        for (Map.Entry<String, Integer> stringStringEntry : map.entrySet()) {
            TableColumnVo tableColumnVo =new TableColumnVo();
            String[] split = stringStringEntry.getKey().split(",");
            tableColumnVo.setTitle(split[0]);
            tableColumnVo.setColKey(split[1]);
            tableColumnVo.setNum(String.valueOf(stringStringEntry.getValue()));
            tableColumnVos.add(tableColumnVo);
        }

        List<JSONObject> jsonObjects =new ArrayList<>();
        // List<JSONObject> jsonObjects2 =new ArrayList<>();
        for (String s : map1.keySet()) {
            jsonObjects.add(map1.get(s));
        }
        // for (String s : map2.keySet()) {
        //     jsonObjects2.add(map2.get(s));
        // }

        stylePicUtils.setStyleColorPic2(list, "pic");
        hashMap.put("list",list);
        hashMap.put("tableColumnVos",tableColumnVos);
        hashMap.put("map",jsonObjects);
        QueryWrapper<PlanningProjectDimension> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("planning_project_id",dto.getPlanningProjectId());
        List<PlanningProjectDimension> list1 = planningProjectDimensionService.list(queryWrapper1);
        hashMap.put("map2",list1);
        return hashMap;
    }

    /**
     * 坑位匹配
     *
     * @param list 坑位列表
     */
    @Override
    public void match(List<PlanningProjectPlankVo> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        PlanningProject planningProject = planningProjectService.getById(list.get(0).getPlanningProjectId());
        //查询已经匹配的大货款号
        List<PlanningProjectPlank> projectPlanks = this.list(new QueryWrapper<PlanningProjectPlank>().select("bulk_style_no","his_design_no").isNotNull("bulk_style_no").ne("bulk_style_no","").or().isNotNull("his_design_no").ne("his_design_no",""));
        List<String>  bulkNos = projectPlanks.stream().map(PlanningProjectPlank::getBulkStyleNo).distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<String> getHisDesignNos = projectPlanks.stream().map(PlanningProjectPlank::getHisDesignNo).distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        bulkNos.addAll(getHisDesignNos);
        //匹配实体
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {
            // //如果已经匹配上了,就不再匹配
            if (!"0".equals(planningProjectPlankVo.getMatchingStyleStatus()) && !"3".equals(planningProjectPlankVo.getMatchingStyleStatus())){
                continue;
            }
            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配
            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProject.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectPlankVo.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectPlankVo.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectPlankVo.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectPlankVo.getProdCategoryCode());
            styleColorQueryWrapper.eq("tsc.band_code", planningProjectPlankVo.getBandCode());
            styleColorQueryWrapper.eq("tsc.order_flag", "1");
            if (!bulkNos.isEmpty()){
                styleColorQueryWrapper.notIn("tsc.style_no", bulkNos);
            }

            List<StyleColorVo> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper);
            if (styleColorList == null || styleColorList.isEmpty()) {
                continue;
            }
            List<String> styleColorIds = styleColorList.stream().map(StyleColorVo::getId).collect(Collectors.toList());
            //  初步匹配上,再去筛选维度
            List<FieldVal> fieldVals = fieldValService.list(new QueryWrapper<FieldVal>().eq("data_group", FieldValDataGroupConstant.STYLE_COLOR).in("foreign_id", styleColorIds));
            Map<String, List<FieldVal>> map= fieldVals.stream().collect(Collectors.groupingBy(FieldVal::getForeignId));

            //  初步匹配上,再去筛选维度
            out:
            for (StyleColorVo styleColorVo : styleColorList) {
                // List<FieldManagementVo> fieldManagementVos = styleColorService.getStyleColorDynamicDataById(styleColorVo.getId());
                List<FieldVal> fieldVals1 = map.get(styleColorVo.getId());
                if (fieldVals1 == null || fieldVals1.isEmpty()) {
                    continue;
                }
                for (FieldVal fieldVal : fieldVals1) {
                    if (fieldVal.getFieldId().equals(planningProjectPlankVo.getDimensionId()) && fieldVal.getVal().equals(planningProjectPlankVo.getDimensionValue())) {
                        // 说明匹配上了
                        planningProjectPlankVo.setBulkStyleNo(styleColorVo.getStyleNo());
                        planningProjectPlankVo.setMatchingStyleStatus("2");
                        planningProjectPlankVo.setPic(styleColorVo.getStyleColorPic());
                        // planningProjectPlankVo.setBandCode(styleColorVo.getBandCode());
                        // planningProjectPlankVo.setBandName(styleColorVo.getBandName());
                        planningProjectPlankVo.setStyleColorId(styleColorVo.getId());
                        BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColorVo.getColorCode()));
                        if (colourLibrary != null) {
                            planningProjectPlankVo.setColorSystem(colourLibrary.getColorType());
                        }

                        PlanningProjectPlank planningProjectPlank = new PlanningProjectPlank();
                        BeanUtil.copyProperties(planningProjectPlankVo, planningProjectPlank);
                        this.updateById(planningProjectPlank);
                        bulkNos.add(planningProjectPlankVo.getBulkStyleNo());
                        planningProject.setIsMatch("1");
                        break out;
                    }
                }
            }
        }

        List<PlanningProjectPlankVo> list1 =new ArrayList<>();
        //匹配虚拟
        JSONObject jsonObject =new JSONObject();
        for (PlanningProjectPlankVo planningProjectPlankVo : list) {

            // 匹配规则 产品季  大类 品类 (中类有就匹配)  波段 第一维度  这5匹配
            BaseQueryWrapper<StyleColor> styleColorQueryWrapper = new BaseQueryWrapper<>();
            styleColorQueryWrapper.eq("ts.planning_season_id", planningProject.getSeasonId());
            styleColorQueryWrapper.eq("ts.prod_category1st", planningProjectPlankVo.getProdCategory1stCode());
            styleColorQueryWrapper.eq("1".equals(planningProjectPlankVo.getIsProdCategory2nd()), "ts.prod_category2nd", planningProjectPlankVo.getProdCategory2ndCode());
            styleColorQueryWrapper.eq("ts.prod_category", planningProjectPlankVo.getProdCategoryCode());
            // styleColorQueryWrapper.eq("tsc.band_code", planningProjectPlankVo.getBandCode());
            styleColorQueryWrapper.eq("tsc.order_flag", "1");
            if (!bulkNos.isEmpty()) {
                styleColorQueryWrapper.notIn("tsc.style_no", bulkNos);
            }

            List<StyleColorVo> styleColorList = stylePricingMapper.getByStyleList(styleColorQueryWrapper);
            if (styleColorList == null || styleColorList.isEmpty()) {
                continue;
            }
            List<String> styleColorIds = styleColorList.stream().map(StyleColorVo::getId).collect(Collectors.toList());
            //  初步匹配上,再去筛选维度
            List<FieldVal> fieldVals = fieldValService.list(new QueryWrapper<FieldVal>().eq("data_group", FieldValDataGroupConstant.STYLE_COLOR).in("foreign_id", styleColorIds));
            Map<String, List<FieldVal>> map= fieldVals.stream().collect(Collectors.groupingBy(FieldVal::getForeignId));


            for (StyleColorVo styleColorVo : styleColorList) {
                List<FieldVal> fieldVals1 = map.get(styleColorVo.getId());
                if (fieldVals1 == null || fieldVals1.isEmpty()) {
                    continue;
                }
                for (FieldVal FieldVal : fieldVals1) {
                    if (FieldVal.getFieldId().equals(planningProjectPlankVo.getDimensionId())) {
                        if (FieldVal.getVal() ==null){
                            continue;
                        }
                        if (FieldVal.getVal().equals(planningProjectPlankVo.getDimensionValue())) {
                            // 说明匹配上了
                            PlanningProjectPlankVo planningProjectPlankVo1 = new PlanningProjectPlankVo();
                            BeanUtil.copyProperties(planningProjectPlankVo, planningProjectPlankVo1);
                            planningProjectPlankVo1.setBulkStyleNo(styleColorVo.getStyleNo());
                            planningProjectPlankVo1.setMatchingStyleStatus("2");
                            planningProjectPlankVo1.setPic(styleColorVo.getStyleColorPic());
                            planningProjectPlankVo1.setBandCode(styleColorVo.getBandCode());
                            planningProjectPlankVo1.setBandName(styleColorVo.getBandName());
                            planningProjectPlankVo1.setStyleColorId(styleColorVo.getId());
                            BasicsdatumColourLibrary colourLibrary = basicsdatumColourLibraryService.getOne(new QueryWrapper<BasicsdatumColourLibrary>().eq("colour_code", styleColorVo.getColorCode()));
                            if (colourLibrary != null) {
                                planningProjectPlankVo1.setColorSystem(colourLibrary.getColorType());
                            }
                            planningProjectPlankVo1.setId(null);
                            planningProjectPlankVo1.setIsVirtual("1");
                            bulkNos.add(planningProjectPlankVo1.getBulkStyleNo());
                            list1.add(planningProjectPlankVo1);
                            if (jsonObject.get(planningProjectPlankVo1.getBandName()+","+planningProjectPlankVo1.getDimensionValue()) == null){
                                jsonObject.put(planningProjectPlankVo1.getBandName()+","+planningProjectPlankVo1.getDimensionValue(),1);
                            }else {
                                jsonObject.put(planningProjectPlankVo1.getBandName()+","+planningProjectPlankVo1.getDimensionValue(),jsonObject.getInteger(planningProjectPlankVo1.getBandName()+","+planningProjectPlankVo1.getDimensionValue())+1);
                            }

                            planningProject.setIsMatch("1");

                        }


                    }
                }
            }
        }

        //hashMap转为字符串
        Set<String> ids = list1.stream().map(PlanningProjectPlankVo::getPlanningProjectDimensionId).collect(Collectors.toSet());
        if (!ids.isEmpty()){
            UpdateWrapper<PlanningProjectDimension> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",ids);
            updateWrapper.set("virtual_number",jsonObject.toJSONString());
            planningProjectDimensionService.update(updateWrapper);
        }

        list.addAll(list1);
        planningProjectService.updateById(planningProject);
    }

    @Override
    public void unMatchByBulkStyleNo(String bulkStyleNo) {
        QueryWrapper<PlanningProjectPlank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bulk_style_no", bulkStyleNo);
        PlanningProjectPlank planningProjectPlank = this.getOne(queryWrapper);
        if (planningProjectPlank!=null){
            PlanningProjectDimension planningProjectDimension = planningProjectDimensionService.getById(planningProjectPlank.getPlanningProjectDimensionId());

            planningProjectPlank.setBulkStyleNo("");
            planningProjectPlank.setStyleColorId("");
            planningProjectPlank.setPic("");
            planningProjectPlank.setBandName(planningProjectDimension.getBandName());
            planningProjectPlank.setBandCode(planningProjectDimension.getBandCode());
            planningProjectPlank.setMatchingStyleStatus("0");
            planningProjectPlank.setColorSystem("");
            this.updateById(planningProjectPlank);
        }
    }
}
