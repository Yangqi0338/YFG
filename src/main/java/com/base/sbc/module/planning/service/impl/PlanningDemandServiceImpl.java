/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.fieldManagement.dto.QueryFieldManagementDto;
import com.base.sbc.module.fieldManagement.entity.FieldManagement;
import com.base.sbc.module.fieldManagement.mapper.FieldManagementMapper;
import com.base.sbc.module.fieldManagement.mapper.OptionMapper;
import com.base.sbc.module.fieldManagement.vo.FieldManagementVo;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.dto.SaveDelDemandDto;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.mapper.PlanningDemandProportionDataMapper;
import com.base.sbc.module.planning.mapper.PlanningDemandMapper;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.service.PlanningDemandService;
import com.base.sbc.module.planning.vo.PlanningDemandVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：企划-需求维度表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDemandDimensionalityService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:18
 */
@Service
public class PlanningDemandServiceImpl extends ServicePlusImpl<PlanningDemandMapper, PlanningDemand> implements PlanningDemandService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private FieldManagementMapper fieldManagementMapper;
    @Autowired
    private FormTypeMapper formTypeMapper;
    @Autowired
    private PlanningDemandProportionDataMapper planningDemandProportionDataMapper;


    @Override
    public ApiResult getDemandListById(QueryDemandDto queryDemandDimensionalityDto) {
        QueryWrapper<PlanningDemand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", queryDemandDimensionalityDto.getCategoryId());
        queryWrapper.eq("planning_season_id", queryDemandDimensionalityDto.getPlanningSeasonId());
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        /*选中的需求*/
        List<PlanningDemandVo> list = baseMapper.getDemandDimensionalityById(queryWrapper);
        list.forEach(p -> {
            /*查询表单数据*/
            QueryFieldManagementDto queryFieldManagementDto = new QueryFieldManagementDto();
            queryFieldManagementDto.setCompanyCode(baseController.getUserCompany());
            queryFieldManagementDto.setGroupName(p.getDemandName());
            queryFieldManagementDto.setFormTypeId(p.getFormTypeId());
            List<FieldManagementVo> list1 = fieldManagementMapper.getFieldManagementList(queryFieldManagementDto);
            p.setFieldManagementVoList(list1);
            /*查询需求数据*/
            QueryWrapper<PlanningDemandProportionData> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("demand_id", p.getId());
            List<PlanningDemandProportionData> demandDimensionalityDataList = planningDemandProportionDataMapper.selectList(queryWrapper1);
            p.setList(demandDimensionalityDataList);
        });
        return ApiResult.success("查询成功", list);
    }

    @Override
    public ApiResult getFormDemand(QueryDemandDto queryDemandDimensionalityDto) {
        Map<String, List> map = new HashMap<>();
        QueryWrapper<FormType> formTypeQueryWrapper = new QueryWrapper<>();
        formTypeQueryWrapper.eq("name", queryDemandDimensionalityDto.getFormName());
        List<FormType> formTypeList = formTypeMapper.selectList(formTypeQueryWrapper);
        if (CollectionUtils.isEmpty(formTypeList)) {
            throw new OtherException("获取表单失败");
        }
        QueryWrapper<FieldManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("form_type_id", formTypeList.get(0).getId());
        queryWrapper.groupBy("group_name");
        /*配置的字段*/
        List<FieldManagement> fieldManagementList = fieldManagementMapper.selectList(queryWrapper);
        map.put("fieldManagement", fieldManagementList);
        QueryWrapper<PlanningDemand> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("category_id", queryDemandDimensionalityDto.getCategoryId());
        queryWrapper1.eq("planning_season_id", queryDemandDimensionalityDto.getPlanningSeasonId());
        List<PlanningDemand> planningDemandList = baseMapper.selectList(queryWrapper1);
        List<String> stringList = planningDemandList.stream().map(PlanningDemand::getDemandName).collect(Collectors.toList());
        //交集
        List<FieldManagement> fieldManagementList2 = fieldManagementList.stream().filter(f -> stringList.contains(f.getGroupName())).collect(Collectors.toList());

        map.put("demandDimensionality", fieldManagementList2);
        return ApiResult.success("查询成功", map);
    }

    @Override
    @Transactional(readOnly = false)
    public ApiResult saveDel(List<SaveDelDemandDto> saveDelDemandDto) {
        /*查询已存在的*/
        QueryWrapper<PlanningDemand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", saveDelDemandDto.get(0).getCategoryId());
        queryWrapper.eq("planning_season_id", saveDelDemandDto.get(0).getPlanningSeasonId());
        List<PlanningDemand> list = baseMapper.selectList(queryWrapper);
        /*添加的数据*/
        List<SaveDelDemandDto> addList = new ArrayList<>();
        /*删除的数据*/
        List<PlanningDemand> delList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            addList = saveDelDemandDto;
        } else {
            /*前端传的数据*/
            List<String> demandNameList2 = saveDelDemandDto.stream().map(SaveDelDemandDto::getDemandName).collect(Collectors.toList());
            delList = list.stream().filter(item -> !demandNameList2.contains(item.getDemandName())).collect(Collectors.toList());
            /*数据库查询的数据*/
            List<String> demandNameList = list.stream().map(PlanningDemand::getDemandName).collect(Collectors.toList());
            addList = saveDelDemandDto.stream().filter(item -> !demandNameList.contains(item.getDemandName())).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(delList)) {
            QueryWrapper<PlanningDemand> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("id", delList.stream().map(PlanningDemand::getId).collect(Collectors.toList()));
          /*  queryWrapper1.eq("category_id", saveDelDemandDto.get(0).getCategoryId());
            queryWrapper1.eq("planning_season_id", saveDelDemandDto.get(0).getPlanningSeasonId());*/
            /*删除需求*/
            baseMapper.delete(queryWrapper1);
            /*删除占比*/
            QueryWrapper<PlanningDemandProportionData> planningDemandProportionDataQueryWrapper = new QueryWrapper<>();
            planningDemandProportionDataQueryWrapper.in("demand_id", delList.stream().map(PlanningDemand::getId).collect(Collectors.toList()));
            planningDemandProportionDataQueryWrapper.eq("company_code", baseController.getUserCompany());
            List<PlanningDemandProportionData> planningDemandProportionDataList = planningDemandProportionDataMapper.selectList(planningDemandProportionDataQueryWrapper);
            if (!CollectionUtils.isEmpty(planningDemandProportionDataList)) {
                planningDemandProportionDataQueryWrapper.clear();
                planningDemandProportionDataQueryWrapper.in("id", planningDemandProportionDataList.stream().map(PlanningDemandProportionData::getId).collect(Collectors.toList()));
                planningDemandProportionDataMapper.delete(planningDemandProportionDataQueryWrapper);
            }
        }
        /*新增新增加的*/
        if (!CollectionUtils.isEmpty(addList)) {
            addList.forEach(s -> {
                PlanningDemand planningDemand = new PlanningDemand();
                BeanUtils.copyProperties(s, planningDemand);
                planningDemand.setCompanyCode(baseController.getUserCompany());
                planningDemand.insertInit();
                baseMapper.insert(planningDemand);
            });
        }
        return ApiResult.success("操作成功");
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
