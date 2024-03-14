/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumCoefficientTemplate;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumDimensionality;
import com.base.sbc.module.basicsdatum.service.BasicsdatumCoefficientTemplateService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumDimensionalityService;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumDimensionalityVo;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.entity.FieldManagement;
import com.base.sbc.module.formtype.entity.FormType;
import com.base.sbc.module.formtype.mapper.FieldManagementMapper;
import com.base.sbc.module.formtype.mapper.FormTypeMapper;
import com.base.sbc.module.planning.dto.CheckMutexDto;
import com.base.sbc.module.planning.dto.DimensionLabelsSearchDto;
import com.base.sbc.module.planning.dto.UpdateDimensionalityDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningDimensionality;
import com.base.sbc.module.planning.mapper.PlanningDimensionalityMapper;
import com.base.sbc.module.planning.service.PlanningDemandService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningDemandService;
import com.base.sbc.module.planning.service.PlanningDimensionalityService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.DimensionalityListVo;
import com.base.sbc.module.planning.vo.PlanningDimensionalityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
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
    @Resource
    private PlanningDemandService planningDemandService;

    @Autowired
    private BasicsdatumDimensionalityService basicsdatumDimensionalityService;

    @Autowired
    private BasicsdatumCoefficientTemplateService basicsdatumCoefficientTemplateService;


    @Autowired
    private PlanningChannelService planningChannelService;

    @Override
    public DimensionalityListVo getDimensionalityList(DimensionLabelsSearchDto dto) {

        BaseQueryWrapper<PlanningDimensionality> qw = new BaseQueryWrapper<>();
        DimensionalityListVo dimensionalityListVo = new DimensionalityListVo();
        List<PlanningDimensionality> planningDimensionalityList = null;
        //（配置页面查询）
        if (StrUtil.isNotBlank(dto.getConfigPageFlag())) {
            dto.setCategoryFlag(StrUtil.isNotBlank(dto.getProdCategory2nd()) ? BaseGlobal.YES : BaseGlobal.NO);
            PlanningUtils.dimensionCommonQw(qw, dto);
            qw.orderByAsc("sort");
            planningDimensionalityList = baseMapper.selectList(qw);
        } else {
            //坑位、款式页面先按中类查，中类没有再查品类
            if(StrUtil.isNotBlank(dto.getProdCategory2nd())){
                dto.setCategoryFlag("1");
                PlanningUtils.dimensionCommonQw(qw, dto);
                qw.orderByAsc("sort");
                planningDimensionalityList = baseMapper.selectList(qw);
            }
            if (CollUtil.isEmpty(planningDimensionalityList) && StrUtil.isBlank(dto.getConfigPageFlag())) {
                qw = new BaseQueryWrapper<>();
                dto.setCategoryFlag("0");
                PlanningUtils.dimensionCommonQw(qw, dto);
                qw.orderByAsc("sort");
                planningDimensionalityList = baseMapper.selectList(qw);
            }
        }
        dimensionalityListVo.setPlanningDimensionalities(planningDimensionalityList);
        dimensionalityListVo.setCategoryFlag(dto.getCategoryFlag());
        return dimensionalityListVo;
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
    @Transactional(rollbackFor = {Exception.class})
    public ApiResult delDimensionality(String id,String sortId) {
        List<String> ids = StringUtils.convertList(id);
        baseMapper.deleteBatchIds(ids);
        if(StringUtils.isNotBlank(sortId)){
           List<PlanningDimensionality> list = baseMapper.selectBatchIds(StringUtils.convertList(sortId));
           Integer index =1;
            for (PlanningDimensionality planningDimensionality : list) {
                planningDimensionality.setSort(index++);
            }
            saveOrUpdateBatch(list);
        }
        return ApiResult.success("操作成功");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
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
        /*校验维度等级*/
        checkDimensionality(dto);
        return ApiResult.success("操作成功", planningDimensionality);
    }

    /**
     * 批量保存修改
     *
     * @param dimensionalityDtoList
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<PlanningDimensionality> batchSaveDimensionality(List<UpdateDimensionalityDto> dimensionalityDtoList) {

        if (dimensionalityDtoList.isEmpty()) {
            return new ArrayList<>();
        }
        CheckMutexDto checkMutexDto = new CheckMutexDto();
        checkMutexDto.setChannel(dimensionalityDtoList.get(0).getChannel());
        checkMutexDto.setPlanningSeasonId(dimensionalityDtoList.get(0).getPlanningSeasonId());
        checkMutexDto.setPlanningChannelId(dimensionalityDtoList.get(0).getPlanningChannelId());
        checkMutexDto.setProdCategory(dimensionalityDtoList.get(0).getProdCategory());
        checkMutexDto.setProdCategory2nd(dimensionalityDtoList.get(0).getProdCategory2nd());
        planningDemandService.checkMutex(checkMutexDto);
        List<PlanningDimensionality> list = BeanUtil.copyToList(dimensionalityDtoList, PlanningDimensionality.class);
        list.forEach(p -> {
            if (CommonUtils.isInitId(p.getId())) {
                p.setId(null);
            }
        });
        /*校验维度等级*/
        saveOrUpdateBatch(list);
        checkDimensionality(dimensionalityDtoList.get(0));

        return list;
    }

    /**
     * 校验维度等级
     * @param dto
     */
    public void checkDimensionality(UpdateDimensionalityDto dto){
        BaseQueryWrapper<PlanningDimensionality> queryWrapper = new BaseQueryWrapper<>();
        DimensionLabelsSearchDto dimensionLabelsSearchDto = new DimensionLabelsSearchDto();
        BeanUtil.copyProperties(dto, dimensionLabelsSearchDto);
        setBaseQueryWrapper(queryWrapper, dimensionLabelsSearchDto);
        queryWrapper.isNotNullStr("tpd.dimensionality_grade");
        List<PlanningDimensionalityVo> dimensionalityList = baseMapper.getCoefficientList(queryWrapper);
        if (CollUtil.isNotEmpty(dimensionalityList)) {
            List<String> stringList = dimensionalityList.stream().map(PlanningDimensionalityVo::getDimensionalityGradeName).collect(Collectors.toList());
            /*获取重复的维度*/
            Set<String> uniqueElements = new HashSet<>(stringList);
            Set<String> duplicateElements = stringList.stream().filter(e -> !uniqueElements.remove(e)).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(duplicateElements)) {
                throw new OtherException(duplicateElements + "数据存在重复维度等级");
            }
        }
    }


    /**
     * 修改排序
     *
     * @param queryFieldManagementDto
     * @return
     */
    @Override
    public Boolean regulateSort(QueryFieldManagementDto queryFieldManagementDto) {
        PlanningDimensionality fieldManagement = new PlanningDimensionality();
        Integer currentId = baseMapper.selectById(queryFieldManagementDto.getCurrentId()).getSort();
        Integer targetId = baseMapper.selectById(queryFieldManagementDto.getTargetId()).getSort();
        fieldManagement.setId(queryFieldManagementDto.getCurrentId());
        fieldManagement.setSort(targetId);
        fieldManagement.updateInit();
        baseMapper.updateById(fieldManagement);
        PlanningDimensionality fieldManagement1 = new PlanningDimensionality();
        fieldManagement1.setId(queryFieldManagementDto.getTargetId());
        fieldManagement1.setSort(currentId);
        fieldManagement1.updateInit();
        baseMapper.updateById(fieldManagement1);
        return true;
    }

    /**
     * 设置构造器
     * @param queryWrapper
     * @param dto
     */
   public void setBaseQueryWrapper(BaseQueryWrapper queryWrapper,DimensionLabelsSearchDto dto){
       queryWrapper.eq("tpd.channel",dto.getChannel());
       if(StrUtil.isNotBlank(dto.getProdCategory2nd())){
           queryWrapper.eq("tpd.prod_category2nd",dto.getProdCategory2nd());
       }else {
           queryWrapper.isNullStr("tpd.prod_category2nd");
       }
       queryWrapper.eq("tpd.prod_category",dto.getProdCategory());

       queryWrapper.eq("tpd.planning_season_id",dto.getPlanningSeasonId());
       queryWrapper.eq("tpd.coefficient_flag",BaseGlobal.YES);
       queryWrapper.eq("tpd.del_flag",BaseGlobal.NO);
       queryWrapper.isNotNullStr("tfm.group_name");
       queryWrapper.orderByAsc("tpd.group_sort","tpd.sort");
    }

    /**
     * 获取围度系数数据
     *
     * @param dto
     * @return
     */
    @Override
    public List<PlanningDimensionalityVo>  getCoefficient(DimensionLabelsSearchDto dto) {
        BaseQueryWrapper<PlanningDimensionality> queryWrapper = new BaseQueryWrapper<>();
        setBaseQueryWrapper(queryWrapper,dto);
        List<PlanningDimensionalityVo> dimensionalityList = baseMapper.getCoefficientList(queryWrapper);
        List<PlanningDimensionalityVo> list = new ArrayList<>();

        if(CollUtil.isEmpty(dimensionalityList)){
            return list;
        }
        LinkedHashMap<String, List<PlanningDimensionalityVo>> map = dimensionalityList.stream().collect(Collectors.groupingBy(p -> p.getGroupName(), LinkedHashMap::new, Collectors.toList()));
        for (String s :map.keySet()){
            PlanningDimensionalityVo planningDimensionalityVo = new PlanningDimensionalityVo();
            planningDimensionalityVo.setList(map.get(s));
            planningDimensionalityVo.setGroupName(s);
            list.add(planningDimensionalityVo);
        }
        return list;
    }

    /**
     * 系数模板引用
     * 覆盖之前的数据
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean templateReference(DimensionLabelsSearchDto dto) {
        if (StrUtil.isEmpty(dto.getPlanningSeasonId())) {
            throw new OtherException("产品季id不能为空");
        }
        if (StrUtil.isEmpty(dto.getPlanningChannelId())) {
            throw new OtherException("渠道id不能为空");
        }
        /*获取模板*/
        BasicsdatumCoefficientTemplate basicsdatumCoefficientTemplate = basicsdatumCoefficientTemplateService.getById(dto.getCoefficientTemplateId());
        if (ObjectUtil.isEmpty(basicsdatumCoefficientTemplate)) {
            throw new OtherException("模板id错误");
        }
        /*查询渠道*/
        PlanningChannel planningChannel = planningChannelService.getById(dto.getPlanningChannelId());
        if (ObjectUtil.isEmpty(planningChannel)) {
            throw new OtherException("此产品季渠道查询失败");
        }

        QueryWrapper<BasicsdatumDimensionality> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coefficient_template_id", basicsdatumCoefficientTemplate.getId());
        queryWrapper.in(StrUtil.isNotBlank(dto.getProdCategory()), "prod_category", StringUtils.convertList(dto.getProdCategory()) );
//        queryWrapper.in(StrUtil.isNotBlank(dto.getProdCategory2nd()),"prod_category2nd",dto.getProdCategory2nd());
        /*获取模板中的系数*/
        List<BasicsdatumDimensionality> dimensionalityList = basicsdatumDimensionalityService.list(queryWrapper);

        if (CollUtil.isNotEmpty(dimensionalityList)) {
            /*此产品季的系数*/
            QueryWrapper<PlanningDimensionality> dimensionalityQueryWrapper = new QueryWrapper<>();
            dimensionalityQueryWrapper.in(StrUtil.isNotBlank(dto.getProdCategory()), "prod_category",StringUtils.convertList(dto.getProdCategory()) );
            dimensionalityQueryWrapper.eq("planning_season_id", dto.getPlanningSeasonId());
            dimensionalityQueryWrapper.eq("channel", planningChannel.getChannel());
            dimensionalityQueryWrapper.eq("planning_channel_id", dto.getPlanningChannelId());
            dimensionalityQueryWrapper.eq("coefficient_flag", BaseGlobal.YES);
            List<PlanningDimensionality> planningDimensionalityList = baseMapper.selectList(dimensionalityQueryWrapper);
            if (CollUtil.isNotEmpty(planningDimensionalityList)) {
                /*删除之前的数据覆盖*/
                List<String> stringList = planningDimensionalityList.stream().map(PlanningDimensionality::getId).collect(Collectors.toList());
                baseMapper.deleteBatchIds(stringList);
            }
            List<PlanningDimensionality> list = BeanUtil.copyToList(dimensionalityList, PlanningDimensionality.class);
            list.forEach(l -> {
                l.setId(null);
                l.setPlanningChannelId(dto.getPlanningChannelId());
                l.setPlanningSeasonId(dto.getPlanningSeasonId());
                l.setChannel(planningChannel.getChannel());
                l.setChannelName(planningChannel.getChannelName());
                l.setCoefficientFlag(BaseGlobal.YES);
            });
            saveOrUpdateBatch(list);
        }
        return true;
    }

    @Override
    public List<PlanningDimensionality> copyDimensionality(DimensionLabelsSearchDto dimensionLabelsSearchDto) {
        String planningSeasonId = dimensionLabelsSearchDto.getPlanningSeasonId();
        DimensionalityListVo dimensionalityList = this.getDimensionalityList(dimensionLabelsSearchDto);
        List<PlanningDimensionality> planningDimensionalities = dimensionalityList.getPlanningDimensionalities();
        if (!planningDimensionalities.isEmpty()) {
            List<String> ids = planningDimensionalities.stream().map(PlanningDimensionality::getId).collect(Collectors.toList());
            this.removeByIds(ids);
        }

        dimensionLabelsSearchDto.setPlanningSeasonId(dimensionLabelsSearchDto.getRefPlanningSeasonId());

        DimensionalityListVo dimensionalityList1 = this.getDimensionalityList(dimensionLabelsSearchDto);
        for (PlanningDimensionality planningDimensionality : dimensionalityList1.getPlanningDimensionalities()) {
            planningDimensionality.setId(null);
            planningDimensionality.setPlanningSeasonId(planningSeasonId);
        }
        List<UpdateDimensionalityDto> updateDimensionalityDtos = BeanUtil.copyToList(dimensionalityList1.getPlanningDimensionalities(), UpdateDimensionalityDto.class);
        return  this.batchSaveDimensionality(updateDimensionalityDtos);
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
