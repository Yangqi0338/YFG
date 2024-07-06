/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formtype.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formtype.dto.QueryFieldManagementDto;
import com.base.sbc.module.formtype.dto.SaveUpdateFieldManagementDto;
import com.base.sbc.module.formtype.entity.*;
import com.base.sbc.module.formtype.mapper.FieldManagementMapper;
import com.base.sbc.module.formtype.mapper.FieldOptionConfigMapper;
import com.base.sbc.module.formtype.mapper.FormTypeMapper;
import com.base.sbc.module.formtype.mapper.OptionMapper;
import com.base.sbc.module.formtype.service.FieldManagementService;
import com.base.sbc.module.formtype.service.FormTypeService;
import com.base.sbc.module.formtype.vo.FieldManagementVo;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.mapper.PlanningDemandMapper;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：字段管理表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.formType.service.FieldManagementService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 18:33:51
 */
@Service
public class FieldManagementServiceImpl extends BaseServiceImpl<FieldManagementMapper, FieldManagement> implements FieldManagementService {

    // 自定义方法区 不替换的区域【other_start】

    @Autowired
    private BaseController baseController;
    @Autowired
    private OptionMapper optionMapper;
    @Autowired
    private FormTypeService formTypeService;

    @Autowired
    private PlanningDemandMapper planningDemandMapper;

    @Autowired
    private FieldOptionConfigMapper fieldOptionConfigMapper;

    @Autowired
    private FormTypeMapper formTypeMapper;

    private IdGen idGen = new IdGen();

    @Override
    public ApiResult saveUpdateField(SaveUpdateFieldManagementDto saveUpdateFieldManagementDto) {
        if (StringUtils.isEmpty(saveUpdateFieldManagementDto.getId())) {
            /*查询是否重复*/
            QueryWrapper<FieldManagement> queryWrapper = new QueryWrapper<>();

            if (!StringUtils.isEmpty(saveUpdateFieldManagementDto.getFieldName())) {
                queryWrapper.or().eq("field_name", saveUpdateFieldManagementDto.getFieldName()).eq("form_type_id", saveUpdateFieldManagementDto.getFormTypeId());
            }
            List<FieldManagement> list=  baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(list)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            FieldManagement fieldManagement = new FieldManagement();
            BeanUtils.copyProperties(saveUpdateFieldManagementDto, fieldManagement);
            fieldManagement.setCompanyCode(baseController.getUserCompany());
            fieldManagement.setIsCompile(BaseGlobal.STATUS_NORMAL);
            fieldManagement.insertInit();
            baseMapper.insert(fieldManagement);
            if (!CollectionUtils.isEmpty(saveUpdateFieldManagementDto.getOptionList())) {
                saveUpdateFieldManagementDto.getOptionList().forEach(m -> {
                    m.insertInit();
                    m.setCompanyCode(baseController.getUserCompany());
                    m.setFieldId(fieldManagement.getId());
                    optionMapper.insert(m);
                });
            }
        }else {
            /*修改*/
            FieldManagement fieldManagement1 = baseMapper.selectById(saveUpdateFieldManagementDto.getId());
            BeanUtils.copyProperties(saveUpdateFieldManagementDto, fieldManagement1);
            fieldManagement1.updateInit();
            baseMapper.updateById(fieldManagement1);
            if (!CollectionUtils.isEmpty(saveUpdateFieldManagementDto.getOptionList())) {
                QueryWrapper<Option> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("field_id",fieldManagement1.getId());
                List<Option> optionList = optionMapper.selectList(queryWrapper);
                saveUpdateFieldManagementDto.getOptionList().forEach(m -> {
                    m.setId(null);
                    m.insertInit();
                    m.setCompanyCode(baseController.getUserCompany());
                    m.setFieldId(fieldManagement1.getId());
                    optionMapper.insert(m);
                });
                if(!CollectionUtils.isEmpty(optionList)){
                    optionMapper.deleteBatchIds(optionList.stream().map(Option::getId).collect(Collectors.toList()));
                }
            }
        }
        return ApiResult.success("操作成功");
    }

    @Override
    public PageInfo<FieldManagementVo> getFieldManagementList(QueryFieldManagementDto queryFieldManagementDto) {
        if (queryFieldManagementDto.getPageNum() != 0 && queryFieldManagementDto.getPageSize() != 0) {
            PageHelper.startPage(queryFieldManagementDto);
        }
        queryFieldManagementDto.setCompanyCode(baseController.getUserCompany());
        /*当formTypeId为空时使用编码查询出formTypeId*/
        if(StrUtil.isEmpty(queryFieldManagementDto.getFormTypeId())){
            FormType formType = formTypeService.getByOne("code", queryFieldManagementDto.getFormTypeCode());
            if(ObjectUtil.isEmpty(formType)){
                throw new OtherException("表单id和编码不能同时为空");
            }
            queryFieldManagementDto.setFormTypeId(formType.getId());
        }
        List<FieldManagementVo> list = this.getFieldManagementListMapper(queryFieldManagementDto);
        /*
        * 判断字段是否是对象 是对象获取到对象里面的所有字段
        * */
      list.forEach(fieldManagementVo -> {
          if(!StringUtils.isEmpty(fieldManagementVo.getFormObjectId())){
              QueryWrapper queryWrapper=new QueryWrapper();
              queryWrapper.eq("form_type_Id",fieldManagementVo.getFormObjectId());
              queryWrapper.eq("company_code",baseController.getUserCompany());
              List<FieldManagementVo> fieldManagementVoList = BeanUtil.copyToList( baseMapper.selectList(queryWrapper), FieldManagementVo.class);
              fieldManagementVo.setList(fieldManagementVoList);
          }
      });
        return new PageInfo<>(list);
    }

    /**
     * 查询维度-字段有配置的选项
     *
     * @param queryDemandDto
     * @return
     */
    @Override
    public List<FieldManagementVo> getFieldConfigList(QueryDemandDto queryDemandDto) {
        /*查询表单的数据*/
        QueryWrapper<FormType> formTypeQueryWrapper = new QueryWrapper<>();
        formTypeQueryWrapper.eq("code", queryDemandDto.getFormCode());
        List<FormType> formTypeList = formTypeMapper.selectList(formTypeQueryWrapper);
        if (CollectionUtils.isEmpty(formTypeList)) {
            throw new OtherException("获取表单失败");
        }
        /*品类查询字段配置列表查询品类下的字段id*/
        BaseQueryWrapper qw = new BaseQueryWrapper();
        queryDemandDto.setFieldId(formTypeList.get(0).getId());
        PlanningUtils.fieldConfigQw(qw,queryDemandDto);
        List<FieldOptionConfig> optionConfigList = fieldOptionConfigMapper.selectList(qw);
        /*获取到这个品类下存在的字段*/
        List<String> fieldManagementIdList = optionConfigList.stream().map(FieldOptionConfig::getFieldManagementId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fieldManagementIdList)) {
            throw new OtherException("字段未配置选项");
        }
        /**
         * 查询需求占比中依赖于字段id
         */
        List<FieldManagementVo> list =   BeanUtil.copyToList(baseMapper.selectBatchIds(fieldManagementIdList), FieldManagementVo.class);
        return list;
    }

    @Override
    public ApiResult adjustmentOrder(QueryFieldManagementDto queryFieldManagementDto) {
        FieldManagement fieldManagement=new FieldManagement();
        Integer  currentId =    baseMapper.selectById( queryFieldManagementDto.getCurrentId()).getSequence();
        Integer targetId =  baseMapper.selectById( queryFieldManagementDto.getTargetId()) .getSequence();
        fieldManagement.setId(queryFieldManagementDto.getCurrentId());
        fieldManagement.setSequence(targetId);
        fieldManagement.updateInit();
        baseMapper.updateById(fieldManagement);
        FieldManagement fieldManagement1=new FieldManagement();
        fieldManagement1.setId(queryFieldManagementDto.getTargetId());
        fieldManagement1.setSequence(currentId);
        fieldManagement1.updateInit();
        baseMapper.updateById(fieldManagement1);
        return ApiResult.success("操作成功");
    }

    @Override
    public List<FieldManagementVo> getFieldManagementListByIds(List<String> ids,String planningSeasonId,String prodCategory,String channel) {
        if (CollUtil.isEmpty(ids)) {
            return null;
        }
        QueryFieldManagementDto dto = new QueryFieldManagementDto();
        dto.setIds(ids);
        dto.setCompanyCode(getCompanyCode());
        List<FieldManagementVo> list = new ArrayList<>();
        if(StrUtil.isEmpty(planningSeasonId)){
            list = this.getFieldManagementListMapper(dto);
        }else {
            if(StrUtil.isEmpty(channel)){
                throw new OtherException("渠道不能为空");
            }
            dto.setPlanningSeasonId(planningSeasonId);
            dto.setProdCategory(prodCategory);
            dto.setChannel(channel);
            list = this.getFieldManagementList1Mapper(dto);
        }

        return list;
    }

    @Override
    public List<FieldManagementVo> getFieldManagementListMapper(QueryFieldManagementDto dto){
        List<FieldManagementVo> fieldManagementList = baseMapper.getFieldManagementList(dto);
        return getFieldManagementVos(fieldManagementList);
    }

    @Override
    public List<FieldManagementVo> getFieldManagementList1Mapper(QueryFieldManagementDto dto){
        List<FieldManagementVo> fieldManagementList = baseMapper.getFieldManagementList1(dto);
        return getFieldManagementVos(fieldManagementList);
    }


    private List<FieldManagementVo> getFieldManagementVos(List<FieldManagementVo> fieldManagementList) {
        if(CollUtil.isNotEmpty(fieldManagementList)){
            List<Option> optionList = baseMapper.getOptionList(fieldManagementList.stream().map(FieldManagementVo::getId).distinct().collect(Collectors.toList()));
            Map<String, List<Option>> collect = optionList.stream().collect(Collectors.groupingBy(Option::getFieldId));
            for (FieldManagementVo fieldManagementVo : fieldManagementList) {
                fieldManagementVo.setOptionList(collect.getOrDefault(fieldManagementVo.getId(),new ArrayList<>()));
            }
        }
        return fieldManagementList;
    }

    @Override
    public List<FieldManagementVo> list(String code, String categoryId, String season) {

        QueryWrapper<FormType> qw = new QueryWrapper<>();
        qw.eq("code", code);
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.last("limit 1");
        FormType formType = formTypeService.getOne(qw);
        if (formType == null) {
            return null;
        }
        QueryWrapper<FieldManagement> fmQw = new QueryWrapper<>();
        fmQw.eq("form_type_id", formType.getId());
        fmQw.apply("FIND_IN_SET({0},season)", season);
        fmQw.apply("FIND_IN_SET({0},category_id)", categoryId);
        fmQw.select("id");
        List<Object> objectList = this.listObjs(fmQw);
        List<String> ids = objectList.stream().map(Object::toString).collect(Collectors.toList());
        return getFieldManagementListByIds(ids,null,null,null);
    }

    @Override
    public void conversion(List<FieldManagementVo> fieldList, List<FieldVal> valueList) {
        if (CollUtil.isEmpty(fieldList)) {
            return;
        }
        Map<String, FieldVal> valMap = Optional.ofNullable(valueList).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(FieldVal::getFieldName, v -> v, (a, b) -> b));
        for (FieldManagementVo vo : fieldList) {
            vo.setFieldId(vo.getId());
            vo.setId(Optional.ofNullable(valMap.get(vo.getFieldName())).map(FieldVal::getId).orElse(idGen.nextIdStr()));
            vo.setVal(Optional.ofNullable(valMap.get(vo.getFieldName())).map(FieldVal::getVal).orElse(null));
            vo.setValName(Optional.ofNullable(valMap.get(vo.getFieldName())).map(FieldVal::getValName).orElse(null));
            vo.setSelected(valMap.containsKey(vo.getFieldName()));
        }

    }

    /**
     * 删除字段
     *
     * @param id
     * @return
     */
    @Override
    public Boolean removeById(String id) {
        /*先验证企划企划需求是否保存的该字段*/
        QueryWrapper queryWrapper1 =new QueryWrapper();
        queryWrapper1.eq("field_id",id);
        List<PlanningDemand> planningDemandList = planningDemandMapper.selectList(queryWrapper1);
        if(!CollectionUtils.isEmpty(planningDemandList)){
            throw new OtherException("删除失败 需求占比引用");
        }
        List<String> ids = com.base.sbc.config.utils.StringUtils.convertList(id);
        QueryWrapper<Option> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("field_id",ids);
        List<Option> optionList= optionMapper.selectList(queryWrapper);
        List<String> optionIds =	optionList.stream().map(Option::getId).collect(Collectors.toList());
        optionMapper.deleteBatchIds(optionIds);
        return baseMapper.deleteBatchIds(com.base.sbc.config.utils.StringUtils.convertList(id))>0;
    }

    /**
     * 获取表单中的字段
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo<FieldManagementVo> getFieldListByFormCode(QueryFieldManagementDto dto) {

        if(StrUtil.isEmpty(dto.getFormTypeCode())){
            throw new OtherException("表单编码不能为空");
        }
        FormType formType = formTypeService.getByOne("code", dto.getFormTypeCode());
        if (ObjectUtil.isEmpty(formType)) {
            throw new OtherException("无表单");
        }
        BaseQueryWrapper<FieldManagement> qw = new BaseQueryWrapper<>();
        Page<FieldManagement> objects = PageHelper.startPage(dto);
        qw.eq("form_type_id",formType.getId());
        qw.like(StrUtil.isNotBlank(dto.getGroupName()),"group_name",dto.getGroupName());
        qw.like(StrUtil.isNotBlank(dto.getFieldName()),"field_name",dto.getFieldName());
        qw.like(StrUtil.isNotBlank(dto.getFieldExplain()),"field_explain",dto.getFieldExplain());
        list(qw);
        return CopyUtil.copy(objects.toPageInfo(), FieldManagementVo.class);
    }


// 自定义方法区 不替换的区域【other_end】

}
