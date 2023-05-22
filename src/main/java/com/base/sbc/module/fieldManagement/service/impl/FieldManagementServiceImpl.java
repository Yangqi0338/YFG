/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fieldManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.fieldManagement.dto.QueryFieldManagementDto;
import com.base.sbc.module.fieldManagement.dto.SaveUpdateFieldManagementDto;
import com.base.sbc.module.fieldManagement.entity.Option;
import com.base.sbc.module.fieldManagement.mapper.FieldManagementMapper;
import com.base.sbc.module.fieldManagement.entity.FieldManagement;
import com.base.sbc.module.fieldManagement.mapper.OptionMapper;
import com.base.sbc.module.fieldManagement.service.FieldManagementService;
import com.base.sbc.module.fieldManagement.vo.FieldManagementVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：字段管理表 service类
 * @address com.base.sbc.module.fieldManagement.service.FieldManagementService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 18:33:51
 * @version 1.0  
 */
@Service
public class FieldManagementServiceImpl extends ServicePlusImpl<FieldManagementMapper, FieldManagement> implements FieldManagementService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private OptionMapper optionMapper;

    @Override
    public ApiResult saveUpdateField(SaveUpdateFieldManagementDto saveUpdateFieldManagementDto) {
        if(StringUtils.isEmpty(saveUpdateFieldManagementDto.getId())) {
            /*查询是否重复*/
            QueryWrapper<FieldManagement> queryWrapper = new QueryWrapper<>();

            if (!StringUtils.isEmpty(saveUpdateFieldManagementDto.getFieldName())) {
                queryWrapper.or().eq("field_name", saveUpdateFieldManagementDto.getFieldName()).eq("form_type_id",saveUpdateFieldManagementDto.getFormTypeId());
            }
        /*    if (!StringUtils.isEmpty(saveUpdateFieldManagementDto.getDefaultHint())) {
                queryWrapper.or().eq("default_hint", saveUpdateFieldManagementDto.getDefaultHint()).eq("form_type_id",saveUpdateFieldManagementDto.getFormTypeId());
            }*/

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
                    m.setRemark("");
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
                    m.setRemark("");
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
        List<FieldManagementVo> list = baseMapper.getFieldManagementList(queryFieldManagementDto);
        return new PageInfo<>(list);
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
    public List<FieldManagementVo> getFieldManagementListByIds(List<String> ids) {
        QueryFieldManagementDto dto=new QueryFieldManagementDto();
        dto.setIds(ids);
        dto.setCompanyCode(getCompanyCode());
        List<FieldManagementVo> list = baseMapper.getFieldManagementList(dto);
        return list;
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
