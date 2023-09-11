/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.mapper.FieldManagementMapper;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.SaveDelDimensionalityDto;
import com.base.sbc.module.planning.dto.UpdateDimensionalityDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.mapper.PlanningDemandMapper;
import com.base.sbc.module.planning.mapper.PlanningDimensionalityMapper;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：企划-维度表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDimensionalityService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-27 11:15:30
 */
@Service
public class PlanningDimensionalityServiceImpl extends BaseServiceImpl<PlanningDimensionalityMapper, PlanningDimensionality> implements PlanningDimensionalityService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private PlanningDemandMapper planningDemandMapper;
    @Autowired
    private FieldManagementMapper fieldManagementMapper;

    @Autowired
    private  FormTypeMapper formTypeMapper;


    @Override
    public ApiResult getDimensionalityList(DimensionLabelsSearchDto dto) {
        BaseQueryWrapper<PlanningDimensionality> qw = new BaseQueryWrapper<>();
        PlanningUtils.dimensionCommonQw(qw, dto);
        qw.eq("planning_season_id", dto.getPlanningSeasonId());
        List<PlanningDimensionality> planningDimensionalityList = baseMapper.selectList(qw);
        return ApiResult.success("查询成功", planningDimensionalityList);
    }

    @Override
    public ApiResult getFormDimensionality(DimensionLabelsSearchDto DimensionLabelsSearchDto) {
        QueryWrapper<FormType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "维度标签");
        /*查询该品类已存在的需求*/
        List<FormType> planningDemandList = formTypeMapper.selectList(queryWrapper);
        BaseQueryWrapper<FieldManagement> fieldManagementQueryWrapper = new BaseQueryWrapper<>();
        PlanningUtils.dimensionCommonQw(fieldManagementQueryWrapper, DimensionLabelsSearchDto);

        fieldManagementQueryWrapper.in("form_type_id", planningDemandList.get(0).getId());
        /*查询以选择需求占比的所有字段*/
        List<FieldManagement> fieldManagementList = fieldManagementMapper.selectList(fieldManagementQueryWrapper);
        Map<String, List> map = new HashMap<>();
        map.put("fieldManagementList", fieldManagementList);
        BaseQueryWrapper<PlanningDimensionality> planningDimensionalityQueryWrapper = new BaseQueryWrapper<>();
        PlanningUtils.dimensionCommonQw(planningDimensionalityQueryWrapper, DimensionLabelsSearchDto);
        planningDimensionalityQueryWrapper.eq("planning_season_id", DimensionLabelsSearchDto.getPlanningSeasonId());
        List<PlanningDimensionality> planningDimensionalityList = baseMapper.selectList(planningDimensionalityQueryWrapper);
        List<String> stringList1 = planningDimensionalityList.stream().map(PlanningDimensionality::getDimensionalityName).collect(Collectors.toList());
        List<FieldManagement> fieldManagementList2 = fieldManagementList.stream().filter(f -> stringList1.contains(f.getFieldName())).collect(Collectors.toList());
        map.put("planningDimensionalityList", fieldManagementList2);
        return ApiResult.success("查询成功", map);
    }

    @Override
    public ApiResult saveBatchDimensionality(List<SaveDelDimensionalityDto> list) {
        /*查询数据库已存在维度*/
        QueryWrapper<PlanningDimensionality> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", list.get(0).getCategoryId());
        queryWrapper.eq("planning_season_id", list.get(0).getPlanningSeasonId());
        List<PlanningDimensionality> planningDimensionalityList = baseMapper.selectList(queryWrapper);
        /*需要删除的数据*/
        List<PlanningDimensionality> delList = new ArrayList<>();
        /*添加的数据*/
        List<SaveDelDimensionalityDto> addList = new ArrayList<>();
        if (CollectionUtils.isEmpty(planningDimensionalityList)) {
            addList = list;
        } else {
            List<String> stringList = list.stream().map(SaveDelDimensionalityDto::getDimensionalityName).collect(Collectors.toList());
            delList = planningDimensionalityList.stream().filter(item -> !stringList.contains(item.getDimensionalityName())).collect(Collectors.toList());
            List<String> stringList1 = planningDimensionalityList.stream().map(PlanningDimensionality::getDimensionalityName).collect(Collectors.toList());
            addList = list.stream().filter(item -> !StringUtils.isEmpty(item.getDimensionalityName()) && !stringList1.contains(item.getDimensionalityName())).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(delList)) {
            QueryWrapper<PlanningDimensionality> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("id", delList.stream().map(PlanningDimensionality::getId).collect(Collectors.toList()));
            baseMapper.delete(queryWrapper1);
        }
        if (!CollectionUtils.isEmpty(addList)) {
            addList.forEach(s -> {
                PlanningDimensionality planningDimensionality = new PlanningDimensionality();
                BeanUtils.copyProperties(s, planningDimensionality);
                planningDimensionality.setCompanyCode(baseController.getUserCompany());
                planningDimensionality.insertInit();
                baseMapper.insert(planningDimensionality);
            });
        }
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult delDimensionality(String id) {
        List<String> ids = StringUtils.convertList(id);
        baseMapper.deleteBatchIds(ids);
        return ApiResult.success("操作成功");
    }


    @Override
    public ApiResult saveDimensionality(UpdateDimensionalityDto dto) {
        PlanningDimensionality planningDimensionality = null;
        // 新增
        if (CommonUtils.isInitId(dto.getId())) {
            planningDimensionality = new PlanningDimensionality();
            BeanUtil.copyProperties(dto, planningDimensionality);
            planningDimensionality.setDelFlag(BaseGlobal.NO);
            baseMapper.insert(planningDimensionality);
        } else {
            /*调整维度*/
            planningDimensionality = baseMapper.selectById(dto.getId());
            BeanUtil.copyProperties(dto, planningDimensionality);
            planningDimensionality.updateInit();
            baseMapper.updateById(planningDimensionality);
        }
        /*调整字段*/
        FieldManagement fieldManagement = fieldManagementMapper.selectById(dto.getFieldId());
        fieldManagement.setIsExamine(dto.getIsExamine());
        fieldManagement.setFieldName(dto.getDimensionalityName());
        fieldManagement.updateInit();
        fieldManagementMapper.updateById(fieldManagement);
        return ApiResult.success("操作成功", planningDimensionality);
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
