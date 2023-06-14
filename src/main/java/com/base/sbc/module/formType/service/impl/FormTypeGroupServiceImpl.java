/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.formType.dto.SaveUpdateFormTypeGroupDto;
import com.base.sbc.module.formType.mapper.FormTypeGroupMapper;
import com.base.sbc.module.formType.entity.FormTypeGroup;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.formType.service.FormTypeGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：表单类型分组 service类
 * @address com.base.sbc.module.formType.service.FormTypeGroupService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:05
 * @version 1.0  
 */
@Service
public class FormTypeGroupServiceImpl extends ServicePlusImpl<FormTypeGroupMapper, FormTypeGroup> implements FormTypeGroupService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private FormTypeMapper formTypeMapper;


    @Override
    public ApiResult saveUpdateGroup(SaveUpdateFormTypeGroupDto formTypeGroupDto) {
        FormTypeGroup formTypeGroup=new FormTypeGroup();
        /*修改*/
        if(StringUtils.isNotBlank(formTypeGroupDto.getId())){
             formTypeGroup=   baseMapper.selectById(formTypeGroupDto.getId());
            BeanUtils.copyProperties(formTypeGroupDto,formTypeGroup);
            baseMapper.updateById(formTypeGroup);
        }else {
            /*新增*/
            QueryWrapper<FormTypeGroup> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("group_name",formTypeGroupDto.getGroupName());
            queryWrapper.eq("company_code",baseController.getUserCompany());
            /*查询group_name是否重复*/
             List<FormTypeGroup> list= baseMapper.selectList(queryWrapper);
             if(!CollectionUtils.isEmpty(list)){
                 throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
             }
            BeanUtils.copyProperties(formTypeGroupDto,formTypeGroup);
            formTypeGroup.setCompanyCode(baseController.getUserCompany());
            formTypeGroup.insertInit();
            baseMapper.insert(formTypeGroup);
        }

        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult getGroupIsCoding() {
        QueryWrapper<FormTypeGroup> queryWrapper=new QueryWrapper<>();
        Map<String,Object> map=new HashMap<>();
        map.put("tableName",formTypeMapper.getTableName());
        map.put("formTypeGroup",baseMapper.selectList(queryWrapper) );
        return ApiResult.success("操作成功",map) ;
    }

	
}
