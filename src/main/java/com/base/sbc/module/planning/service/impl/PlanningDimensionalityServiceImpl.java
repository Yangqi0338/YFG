/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.mapper.FieldManagementMapper;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.UpdateDimensionalityDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.mapper.PlanningDimensionalityMapper;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private FieldManagementMapper fieldManagementMapper;

    @Autowired
    private  FormTypeMapper formTypeMapper;


    @Override
    public ApiResult getDimensionalityList(DimensionLabelsSearchDto dto) {
        //先按中类查，中类没有再查品类
        BaseQueryWrapper<PlanningDimensionality> qw = new BaseQueryWrapper<>();
        dto.setCategoryFlag("1");
        PlanningUtils.dimensionCommonQw(qw, dto);
        List<PlanningDimensionality> planningDimensionalityList = baseMapper.selectList(qw);
        if (CollUtil.isEmpty(planningDimensionalityList)) {
            qw = new BaseQueryWrapper<>();
            dto.setCategoryFlag("0");
            PlanningUtils.dimensionCommonQw(qw, dto);
            planningDimensionalityList = baseMapper.selectList(qw);
        }
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
    public Boolean saveBatchDimensionality(List<UpdateDimensionalityDto> list){
        List<PlanningDimensionality> dimensionalityList = BeanUtil.copyToList(list, PlanningDimensionality.class);
        saveOrUpdateBatch(dimensionalityList);
        return true;
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
            planningDimensionality.setId(null);
            baseMapper.insert(planningDimensionality);
        } else {
            /*调整维度*/
            planningDimensionality = baseMapper.selectById(dto.getId());
            BeanUtil.copyProperties(dto, planningDimensionality);
            planningDimensionality.updateInit();
            baseMapper.updateById(planningDimensionality);
        }
        return ApiResult.success("操作成功", planningDimensionality);
    }

    /**
     * 批量保存修改
     *
     * @param dimensionalityDtoList
     * @return
     */
    @Override
    public Boolean batchSaveDimensionality(List<UpdateDimensionalityDto> dimensionalityDtoList) {
        List<PlanningDimensionality> list = BeanUtil.copyToList(dimensionalityDtoList, PlanningDimensionality.class);
        list.forEach(p -> {
            if (CommonUtils.isInitId(p.getId())) {
                p.setId(null);
            }
        });
        saveOrUpdateBatch(list);
        return true;
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
