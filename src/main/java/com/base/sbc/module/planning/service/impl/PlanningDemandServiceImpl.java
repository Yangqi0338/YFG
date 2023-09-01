/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumModelTypeExcelDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formType.dto.QueryFieldManagementDto;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.mapper.FieldManagementMapper;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.dto.SaveDelDemandDto;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.mapper.PlanningDemandMapper;
import com.base.sbc.module.planning.mapper.PlanningDemandProportionDataMapper;
import com.base.sbc.module.planning.service.PlanningDemandService;
import com.base.sbc.module.planning.vo.PlanningDemandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class PlanningDemandServiceImpl extends BaseServiceImpl<PlanningDemandMapper, PlanningDemand> implements PlanningDemandService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private FieldManagementMapper fieldManagementMapper;
    @Autowired
    private FormTypeMapper formTypeMapper;
    @Autowired
    private PlanningDemandProportionDataMapper planningDemandProportionDataMapper;


    @Override
    public List<PlanningDemandVo> getDemandListById(QueryDemandDto queryDemandDimensionalityDto) {
        QueryWrapper<PlanningDemand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_category", queryDemandDimensionalityDto.getCategoryId());
        queryWrapper.eq("planning_season_id", queryDemandDimensionalityDto.getPlanningSeasonId());
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        queryWrapper.eq(StrUtil.isNotBlank(queryDemandDimensionalityDto.getFieldId()), "field_id", queryDemandDimensionalityDto.getFieldId());
        /*选中的需求*/
        List<PlanningDemandVo> list =   BeanUtil.copyToList(baseMapper.selectList(queryWrapper), PlanningDemandVo.class);
        List<String> stringList1 = list.stream().map(PlanningDemandVo::getFieldId).collect(Collectors.toList());
        List<FieldManagementVo> fieldManagementVoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stringList1)){
            /*查询表单数据*/
            QueryFieldManagementDto queryFieldManagementDto = new QueryFieldManagementDto();
            queryFieldManagementDto.setCompanyCode(baseController.getUserCompany());
            queryFieldManagementDto.setIds(stringList1);
            /*查询所有关联的字段*/
            fieldManagementVoList = fieldManagementMapper.getFieldManagementList(queryFieldManagementDto);
        }

        Map<String, List<FieldManagementVo>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldManagementVoList)) {
            map = fieldManagementVoList.stream().collect(Collectors.groupingBy(FieldManagementVo::getId));
        }
        for (PlanningDemandVo p : list) {
            List<FieldManagementVo> list1 =  map.get(p.getFieldId());
            if(!CollectionUtils.isEmpty(list1)){
                p.setFieldManagementVo(list1.get(0));
            }
            /*查询需求数据*/
            QueryWrapper<PlanningDemandProportionData> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("demand_id", p.getId());
            List<PlanningDemandProportionData> demandDimensionalityDataList = planningDemandProportionDataMapper.selectList(queryWrapper1);
            p.setList(demandDimensionalityDataList);
            /*禁用下拉框已选中的值*/
            list1.forEach(fieldManagementVo -> {
                if(fieldManagementVo.getFieldTypeCoding().equals("select")){
                    List<String> stringList =   p.getList().stream().map(PlanningDemandProportionData::getClassify).collect(Collectors.toList());
                    fieldManagementVo.getOptionList().forEach(option -> {
                        if (stringList.contains(option.getOptionName())) {
                            option.setDisabled(true);
                        }
                    });
                }
            });
        };
        return list;
    }

    @Override
    public ApiResult getFormDemand(QueryDemandDto queryDemandDimensionalityDto) {
        Map<String, List> map = new HashMap<>();
        /*查询表单的数据*/
        QueryWrapper<FormType> formTypeQueryWrapper = new QueryWrapper<>();
        formTypeQueryWrapper.eq("code", queryDemandDimensionalityDto.getFormCode());
        List<FormType> formTypeList = formTypeMapper.selectList(formTypeQueryWrapper);
        if (CollectionUtils.isEmpty(formTypeList)) {
            throw new OtherException("获取表单失败");
        }
        QueryWrapper<FieldManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("form_type_id", formTypeList.get(0).getId());
        queryWrapper.like("category_id",queryDemandDimensionalityDto.getCategoryId());
        /*配置的字段*/
        /**
         * 查询需求占比中依赖于字段id
         */
        List<FieldManagement> fieldManagementList = fieldManagementMapper.selectList(queryWrapper);
        map.put("fieldManagement", fieldManagementList);
        QueryWrapper<PlanningDemand> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.like("prod_category", queryDemandDimensionalityDto.getCategoryId());
        queryWrapper1.eq("planning_season_id", queryDemandDimensionalityDto.getPlanningSeasonId());
        List<PlanningDemand> planningDemandList = baseMapper.selectList(queryWrapper1);
        /*字段id*/
        List<String> stringList = planningDemandList.stream().map(PlanningDemand::getFieldId).collect(Collectors.toList());
        List<FieldManagement> fieldManagementList2 =new ArrayList<>();
        queryWrapper.clear();
        if(!CollectionUtils.isEmpty(stringList)){
            queryWrapper.in("id",stringList);
            fieldManagementList2 = fieldManagementMapper.selectList(queryWrapper);
        }


        map.put("demandDimensionality", fieldManagementList2);
        return ApiResult.success("查询成功", map);
    }

    @Override
    @Transactional(readOnly = false)
    public ApiResult saveDel(List<SaveDelDemandDto> saveDelDemandDto) {
        /*查询已存在的*/
        QueryWrapper<PlanningDemand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_category", saveDelDemandDto.get(0).getProdCategory());
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
            addList = saveDelDemandDto.stream().filter(item -> !StringUtils.isEmpty(item.getDemandName())&&!demandNameList.contains(item.getDemandName())   ).collect(Collectors.toList());
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

        List<PlanningDemand> planningDemandList = BeanUtil.copyToList(addList, PlanningDemand.class);
        saveBatch(planningDemandList);

        return ApiResult.success("操作成功");
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
