/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.formType.dto.FormDeleteDto;
import com.base.sbc.module.formType.dto.FormStartStopDto;
import com.base.sbc.module.formType.dto.QueryFormTypeDto;
import com.base.sbc.module.formType.dto.SaveUpdateFormTypeDto;
import com.base.sbc.module.formType.entity.FormTypeGroup;
import com.base.sbc.module.formType.mapper.FormTypeGroupMapper;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.service.FormTypeService;
import com.base.sbc.module.formType.vo.PagingFormTypeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 类描述：表单类型 service类
 * @address com.base.sbc.module.formType.service.FormTypeService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:01
 * @version 1.0  
 */
@Service
public class FormTypeServiceImpl extends ServicePlusImpl<FormTypeMapper, FormType> implements FormTypeService {
    @Autowired
    private FormTypeGroupMapper formTypeGroupMapper;
    @Autowired
    private BaseController baseController;

    @Override
    public PageInfo<PagingFormTypeVo> getFormTypeIsGroup(QueryFormTypeDto queryFormTypeDto) {
        PageHelper.startPage(queryFormTypeDto);
        queryFormTypeDto.setCompanyCode(baseController.getUserCompany());
        queryFormTypeDto.setOrderBy("ftg.create_date desc");
        List<PagingFormTypeVo> list = baseMapper.getFormTypeIsGroup(queryFormTypeDto);
        return new PageInfo<>(list);
    }

    @Override
    public ApiResult formDelete(FormDeleteDto formDeleteDto) {
        if (formDeleteDto.getType()) {
            /*表单分组*/
            QueryWrapper<FormType> queryWrapper=new QueryWrapper<>();
            queryWrapper.in("group_id",Arrays.asList(formDeleteDto.getIds()));
            List<FormType> formTypeList= baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(formTypeList)){
                throw new OtherException("分组存在表单类型");
            }
            formTypeGroupMapper.deleteBatchIds(Arrays.asList(formDeleteDto.getIds()));
        } else {
            /*类型*/
            baseMapper.deleteBatchIds(Arrays.asList(formDeleteDto.getIds()));
        }
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult getFormType(QueryFormTypeDto queryFormTypeDto) {
        UpdateWrapper<FormType> queryWrapper = new UpdateWrapper<>();
        queryWrapper.eq("company_code",baseController.getUserCompany());
        if(StringUtils.isNotBlank(queryFormTypeDto.getName())){
            queryWrapper.like("name" ,queryFormTypeDto.getName());
        }
        queryWrapper.eq("status" ,"0");
        queryWrapper.eq("del_flag" ,"0");

        return ApiResult.success("查询成功", baseMapper.selectList(queryWrapper));
    }


    @Override
    public ApiResult formStartStop(FormStartStopDto formStartStopDto) {
        if (formStartStopDto.getType()) {
            /*表单分组*/
            UpdateWrapper<FormTypeGroup> queryWrapper = new UpdateWrapper<>();
            queryWrapper.in("id", formStartStopDto.getIds());
            queryWrapper.set("status", formStartStopDto.getStatus());
            queryWrapper.set("update_date", new Date());
            queryWrapper.set("update_id", baseController.getUserId());
            queryWrapper.set("update_name", baseController.getUser().getName());
            formTypeGroupMapper.update(null, queryWrapper);
        } else {
            /*表单类型*/
            UpdateWrapper<FormType> queryWrapper = new UpdateWrapper<>();
            queryWrapper.in("id", formStartStopDto.getIds());
            queryWrapper.set("status", formStartStopDto.getStatus());
            queryWrapper.set("update_date", new Date());
            queryWrapper.set("update_id", baseController.getUserId());
            queryWrapper.set("update_name", baseController.getUser().getName());
            baseMapper.update(null, queryWrapper);
        }
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult saveUpdateType(SaveUpdateFormTypeDto saveUpdateFormTypeDto) {
        FormType formType=new FormType();
        /*修改*/
        if(StringUtils.isNotBlank(saveUpdateFormTypeDto.getId())){
             formType= baseMapper.selectById(saveUpdateFormTypeDto.getId());
            BeanUtils.copyProperties(saveUpdateFormTypeDto,formType);
            formType.insertInit();
            baseMapper.updateById(formType);

        }else {
            QueryWrapper<FormType> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("name",saveUpdateFormTypeDto.getName());
            queryWrapper.eq("company_code",baseController.getUserCompany());
            List<FormType> list= baseMapper.selectList(queryWrapper);
            if(!CollectionUtils.isEmpty(list)){
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            BeanUtils.copyProperties(saveUpdateFormTypeDto,formType);
            formType.setCompanyCode(baseController.getUserCompany());
            formType.setRemark("");
            formType.insertInit();
            baseMapper.insert(formType);
        }
        return ApiResult.success("操作成功");
    }



/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
