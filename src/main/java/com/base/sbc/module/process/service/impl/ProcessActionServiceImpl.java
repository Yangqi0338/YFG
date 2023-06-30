/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.process.dto.AddRevampProcessActionDto;
import com.base.sbc.module.process.dto.QueryActionDto;
import com.base.sbc.module.process.entity.ProcessAction;
import com.base.sbc.module.process.entity.ProcessNodeAction;
import com.base.sbc.module.process.mapper.ProcessActionMapper;
import com.base.sbc.module.process.mapper.ProcessNodeActionMapper;
import com.base.sbc.module.process.service.ProcessActionService;
import com.base.sbc.module.process.vo.ProcessActionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 类描述：流程配置-动作定义 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.process.service.ProcessActionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 11:03:08
 */
@Service
public class ProcessActionServiceImpl extends BaseServiceImpl<ProcessActionMapper, ProcessAction> implements ProcessActionService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private ProcessNodeActionMapper processNodeActionMapper;

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 流程配置-动作定义分页查询
     *
     * @param queryActionDto
     * @return
     */
    @Override
    public PageInfo<ProcessActionVo> getProcessActionList(QueryActionDto queryActionDto) {
        /*分页*/
        if (queryActionDto.getPageNum() != 0 && queryActionDto.getPageSize() != 0) {
            PageHelper.startPage(queryActionDto);
        }
        QueryWrapper<ProcessAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.like(StringUtils.isNotBlank(queryActionDto.getActionName()), "action_name", queryActionDto.getActionName());
        queryWrapper.like(StringUtils.isNotBlank(queryActionDto.getActionCode()), "action_code", queryActionDto.getActionCode());
        /*查询流程配置-动作定义数据*/
        List<ProcessAction> processActionList = baseMapper.selectList(queryWrapper);
        PageInfo<ProcessAction> pageInfo = new PageInfo<>(processActionList);
        /*转换vo*/
        List<ProcessActionVo> list = BeanUtil.copyToList(processActionList, ProcessActionVo.class);
        PageInfo<ProcessActionVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 方法描述：新增修改流程配置-动作定义
     *
     * @param addRevampProcessActionDto 流程配置-动作定义Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampProcessAction(AddRevampProcessActionDto addRevampProcessActionDto) {
        ProcessAction processAction = new ProcessAction();
        if (StringUtils.isEmpty(addRevampProcessActionDto.getId())) {
            QueryWrapper<ProcessAction> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("action_name", addRevampProcessActionDto.getActionName()).or().eq("action_code", addRevampProcessActionDto.getActionCode());
            List<ProcessAction> processActionList = baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(processActionList)) {
                throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
            }
            /*新增*/
            BeanUtils.copyProperties(addRevampProcessActionDto, processAction);
            processAction.setCompanyCode(baseController.getUserCompany());
            processAction.insertInit();
            baseMapper.insert(processAction);
        } else {
            /*查询在节点中是否用到该动作*/
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("action_id", addRevampProcessActionDto.getId());
            queryWrapper.eq("company_code", baseController.getUserCompany());
            List<ProcessNodeAction> processNodeActionList = processNodeActionMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(processNodeActionList)) {
                throw new OtherException("该数据有被引用");
            }
            /*修改*/
            processAction = baseMapper.selectById(addRevampProcessActionDto.getId());
            if (ObjectUtils.isEmpty(processAction)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampProcessActionDto, processAction);
            processAction.updateInit();
            baseMapper.updateById(processAction);
        }
        return true;
    }


    /**
     * 方法描述：删除流程配置-动作定义
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delProcessAction(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopProcessAction(StartStopDto startStopDto) {
        UpdateWrapper<ProcessAction> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
