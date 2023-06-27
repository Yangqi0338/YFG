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
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.process.dto.AddRevampProcessNodeActionDto;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusConditionDto;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusUpdateManagementDto;
import com.base.sbc.module.process.entity.ProcessNodeAction;
import com.base.sbc.module.process.entity.ProcessNodeStatusCondition;
import com.base.sbc.module.process.entity.ProcessNodeStatusUpdateManagement;
import com.base.sbc.module.process.mapper.ProcessNodeStatusConditionMapper;
import com.base.sbc.module.process.service.ProcessNodeActionService;
import com.base.sbc.module.process.service.ProcessNodeStatusConditionService;
import com.base.sbc.module.process.service.ProcessNodeStatusUpdateManagementService;
import com.base.sbc.module.process.vo.ProcessNodeActionVo;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;
import com.base.sbc.module.process.vo.ProcessNodeStatusUpdateManagementVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：流程配置-节点状态条件 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.process.service.ProcessNodeStatusConditionService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-5 17:10:23
 */
@Service
public class ProcessNodeStatusConditionBaseServiceImpl extends BaseServiceImpl<ProcessNodeStatusConditionMapper, ProcessNodeStatusCondition> implements ProcessNodeStatusConditionService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private ProcessNodeActionService processNodeActionService;

    @Autowired
    private ProcessNodeStatusUpdateManagementService processNodeStatusUpdateManagementService;


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 流程配置-节点状态条件分页查询
     *
     * @param queryDto
     * @return
     */
    @Override
    public PageInfo<ProcessNodeStatusConditionVo> getProcessNodeStatusConditionList(QueryDto queryDto) {
        /*分页*/
        PageHelper.startPage(queryDto);
        QueryWrapper<ProcessNodeStatusCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        /*查询流程配置-节点状态条件数据*/
        List<ProcessNodeStatusCondition> processNodeStatusConditionList = baseMapper.selectList(queryWrapper);
        PageInfo<ProcessNodeStatusCondition> pageInfo = new PageInfo<>(processNodeStatusConditionList);
        /*转换vo*/
        List<ProcessNodeStatusConditionVo> list = BeanUtil.copyToList(processNodeStatusConditionList, ProcessNodeStatusConditionVo.class);
        PageInfo<ProcessNodeStatusConditionVo> pageInfo1 = new PageInfo<>();
        pageInfo1.setList(list);
        pageInfo1.setTotal(pageInfo.getTotal());
        pageInfo1.setPageNum(pageInfo.getPageNum());
        pageInfo1.setPageSize(pageInfo.getPageSize());
        return pageInfo1;
    }


    /**
     * 方法描述：新增修改流程配置-节点状态条件
     *
     * @param addRevampProcessNodeStatusConditionDto 流程配置-节点状态条件Dto类
     * @return boolean
     */
    @Override
    public Boolean addRevampProcessNodeStatusCondition(AddRevampProcessNodeStatusConditionDto addRevampProcessNodeStatusConditionDto) {
        ProcessNodeStatusCondition processNodeStatusCondition = new ProcessNodeStatusCondition();
        if (StringUtils.isEmpty(addRevampProcessNodeStatusConditionDto.getId())) {
            QueryWrapper<ProcessNodeStatusCondition> queryWrapper = new QueryWrapper<>();
            /*新增*/
            BeanUtils.copyProperties(addRevampProcessNodeStatusConditionDto, processNodeStatusCondition);
            processNodeStatusCondition.setCompanyCode(baseController.getUserCompany());
            processNodeStatusCondition.insertInit();
            baseMapper.insert(processNodeStatusCondition);
        } else {
            /*修改*/
            processNodeStatusCondition = baseMapper.selectById(addRevampProcessNodeStatusConditionDto.getId());
            if (ObjectUtils.isEmpty(processNodeStatusCondition)) {
                throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
            }
            BeanUtils.copyProperties(addRevampProcessNodeStatusConditionDto, processNodeStatusCondition);
            processNodeStatusCondition.updateInit();
            baseMapper.updateById(processNodeStatusCondition);
        }
        /*保存动作*/
        if(!CollectionUtils.isEmpty(addRevampProcessNodeStatusConditionDto.getList())){
            //前端传入数据
            List<AddRevampProcessNodeActionDto> list =addRevampProcessNodeStatusConditionDto.getList();
            /*数据库查询数据*/
            QueryWrapper queryWrapper= new QueryWrapper();
            queryWrapper.eq("node_status_condition_id",processNodeStatusCondition.getId());
            queryWrapper.eq("company_code",baseController.getUserCompany());
            List<ProcessNodeAction> processNodeActionList =  processNodeActionService.list(queryWrapper);
            List<String> stringList = list.stream().map(AddRevampProcessNodeActionDto::getActionId).collect(Collectors.toList());
            /*需要删除的数据*/
            List<ProcessNodeAction> delNodeActionList = processNodeActionList.stream().filter(s -> !stringList.contains(s.getActionId())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(delNodeActionList)){
                List<String> delIdList = delNodeActionList.stream().filter(s -> StringUtils.isNotBlank(s.getId())).map(ProcessNodeAction::getId).collect(Collectors.toList());
                /*批量删除*/
                processNodeActionService.delProcessNodeAction(String.join(",", delIdList));
            }
            for (AddRevampProcessNodeActionDto addRevampProcessNodeActionDto : list) {
                addRevampProcessNodeActionDto.setNodeStatusConditionId(processNodeStatusCondition.getId());
            }
            processNodeActionService.batchAddRevampProcessNodeAction(list);
        }
        /*保存修改的字段*/
        if(!CollectionUtils.isEmpty(addRevampProcessNodeStatusConditionDto.getUpdateManagementDtoList())){
            for (AddRevampProcessNodeStatusUpdateManagementDto addRevampProcessNodeStatusUpdateManagementDto : addRevampProcessNodeStatusConditionDto.getUpdateManagementDtoList()) {
                addRevampProcessNodeStatusUpdateManagementDto.setNodeStatusConditionId(processNodeStatusCondition.getId());
            }
            processNodeStatusUpdateManagementService.batchAddRevampProcessNodeAction(addRevampProcessNodeStatusConditionDto.getUpdateManagementDtoList());
        }
        return true;
    }


    /**
     * 方法描述：删除流程配置-节点状态条件
     *
     * @param id （多个用，）
     * @return boolean
     */
    @Override
    public Boolean delProcessNodeStatusCondition(String id) {
        List<String> ids = StringUtils.convertList(id);
        /*批量删除*/
        baseMapper.deleteBatchIds(ids);
        return true;
    }

    /**
     * 方法描述：查询明细
     *
     * @param addRevampProcessNodeStatusConditionDto
     * @return boolean
     */
    @Override
    public ProcessNodeStatusConditionVo getNodeStatusConditionDetail(AddRevampProcessNodeStatusConditionDto addRevampProcessNodeStatusConditionDto) {

        QueryWrapper<ProcessNodeStatusCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("node_id", addRevampProcessNodeStatusConditionDto.getNodeId());
        queryWrapper.eq("original_status", addRevampProcessNodeStatusConditionDto.getOriginalStatus());
        queryWrapper.eq("target_status", addRevampProcessNodeStatusConditionDto.getTargetStatus());
        ProcessNodeStatusCondition processNodeStatusCondition = baseMapper.selectOne(queryWrapper);
        ProcessNodeStatusConditionVo processNodeStatusConditionVo = new ProcessNodeStatusConditionVo();
        if (!ObjectUtils.isEmpty(processNodeStatusCondition)) {
            /*获取动作*/
            BeanUtils.copyProperties(processNodeStatusCondition, processNodeStatusConditionVo);
            QueryWrapper queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("node_status_condition_id", processNodeStatusCondition.getId());
            List<ProcessNodeAction> processNodeActionList = processNodeActionService.list(queryWrapper1);
            if (!CollectionUtils.isEmpty(processNodeActionList)) {
                List<ProcessNodeActionVo> list = BeanUtil.copyToList(processNodeActionList, ProcessNodeActionVo.class);
                processNodeStatusConditionVo.setProcessNodeActionVoList(list);
            }
            /*获取修改的字段*/
            queryWrapper1.clear();
            queryWrapper1.eq("node_status_condition_id", processNodeStatusCondition.getId());
            List<ProcessNodeStatusUpdateManagement> updateManagementList = processNodeStatusUpdateManagementService.list(queryWrapper1);
            if (!CollectionUtils.isEmpty(updateManagementList)) {
                List<ProcessNodeStatusUpdateManagementVo> processNodeStatusUpdateManagementVoList = BeanUtil.copyToList(updateManagementList, ProcessNodeStatusUpdateManagementVo.class);
                processNodeStatusConditionVo.setUpdateManagementVoList(processNodeStatusUpdateManagementVoList);
            }
        }
        return processNodeStatusConditionVo;
    }


    /**
     * 方法描述：启用停止
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    @Override
    public Boolean startStopProcessNodeStatusCondition(StartStopDto startStopDto) {
        UpdateWrapper<ProcessNodeStatusCondition> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", StringUtils.convertList(startStopDto.getIds()));
        updateWrapper.set("status", startStopDto.getStatus());
        /*修改状态*/
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /** 自定义方法区 不替换的区域【other_end】 **/

}
