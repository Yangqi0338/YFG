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
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.SpringContextHolder;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.mapper.FieldManagementMapper;
import com.base.sbc.module.process.dto.InitiateProcessDto;
import com.base.sbc.module.process.entity.*;
import com.base.sbc.module.process.mapper.*;
import com.base.sbc.module.process.service.ProcessBusinessInstanceService;
import com.base.sbc.module.process.service.ProcessNodeRecordService;
import com.base.sbc.module.process.vo.ProcessNodeActionVo;
import com.base.sbc.module.process.vo.ProcessNodeRecordVo;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;
import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类描述：流程配置-业务实例 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.process.service.ProcessBusinessInstanceService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-6 15:03:26
 */
@Service
public class ProcessBusinessInstanceServiceImpl extends BaseServiceImpl<ProcessBusinessInstanceMapper, ProcessBusinessInstance> implements ProcessBusinessInstanceService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private ProcessProcessSchemeMapper processProcessSchemeMapper;

    @Autowired
    private ProcessNodeMapper processNodeMapper;

    @Autowired
    private ProcessNodeStatusMapper processNodeStatusMapper;

    @Autowired
    private ProcessNodeRecordMapper processNodeRecordMapper;

    @Autowired
    private ProcessNodeRecordService processNodeRecordService;

    @Autowired
    private ProcessNodeStatusConditionMapper processNodeStatusConditionMapperl;

    @Autowired
    private ProcessStateRecordMapper processStateRecordMapper;

    @Autowired
    private ProcessNodeActionMapper processNodeActionMapper;

    @Autowired
    private ProcessNodeStatusUpdateManagementMapper processNodeStatusUpdateManagementMapper;

    @Autowired
    private FieldManagementMapper fieldManagementMapper;

    /**
     * 描述- 发起流程
     * initiateProcessDto 发起流程dto
     *
     * @param initiateProcessDto
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean initiateProcess(InitiateProcessDto initiateProcessDto) {
        if (StringUtils.isBlank(initiateProcessDto.getBusinessDataId())) {
            throw new OtherException("业务数据id不能为空");
        }
        if (StringUtils.isBlank(initiateProcessDto.getSchemeCode())) {
            throw new OtherException("方案编码不能为空");
        }
        /*查询方案编码是否存在*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("scheme_code", initiateProcessDto.getSchemeCode());
        ProcessProcessScheme processProcessScheme = processProcessSchemeMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(processProcessScheme)) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_ATTRIBUTE_NOT_REQUIREMENTS);
        }
        /*获取流程接所有节点*/
        queryWrapper.clear();
        queryWrapper.eq("process_scheme_id", processProcessScheme.getId());
        queryWrapper.orderByAsc("sort");
        List<ProcessNode> processNodeList = processNodeMapper.selectList(queryWrapper);
        /*获取第一个节点的初始状态*/
        queryWrapper.clear();
        queryWrapper.eq("node_id", processNodeList.get(0).getId());
        queryWrapper.eq("start_status", BaseGlobal.STATUS_CLOSE);
        List<ProcessNodeStatus> processNodeStatusList = processNodeStatusMapper.selectList(queryWrapper);
        /*创建一个流程*/
        ProcessBusinessInstance processBusinessInstance = new ProcessBusinessInstance();
        processBusinessInstance.setProcessSchemeId(processProcessScheme.getId());
        processBusinessInstance.setAtPresentStatusName(processNodeStatusList.get(0).getStatusName());
        processBusinessInstance.setAtPresentNodeName(processNodeList.get(0).getNodeName());
        processBusinessInstance.setAtPresentNodeId(processNodeList.get(0).getId());
        processBusinessInstance.setBusinessDataId(initiateProcessDto.getBusinessDataId());
        processBusinessInstance.setCompanyCode(baseController.getUserCompany());
        processBusinessInstance.insertInit();
        baseMapper.insert(processBusinessInstance);
        /*节点记录*/
        List<ProcessNodeRecord> processNodeRecordList = new ArrayList<>();
        for (int i = 0; i < processNodeList.size(); i++) {
            ProcessNodeRecord processNodeRecord = new ProcessNodeRecord();
            processNodeRecord.setBusinessInstanceId(processBusinessInstance.getId());
            processNodeRecord.setNodeId(processNodeList.get(i).getId());
            processNodeRecord.setSort(processNodeList.get(i).getSort());
            processNodeRecord.setNodeName(processNodeList.get(i).getNodeName());
            if (i == 0) {
                processNodeRecord.setStartDate(new Date());
                processNodeRecord.setAtPresentStatus(processNodeStatusList.get(0).getStatusName());
            }
            processNodeRecordList.add(processNodeRecord);
        }
        processNodeRecordService.batchAddition(processNodeRecordList);
        /*状态记录一条*/
        ProcessStateRecord processStateRecord = new ProcessStateRecord();
        processStateRecord.insertInit();
        processStateRecord.setCompanyCode(baseController.getUserCompany());
        processStateRecord.setBusinessInstanceId(processBusinessInstance.getId());
        processStateRecord.setNodeRecordId(processNodeRecordList.get(0).getId());
        processStateRecord.setStatus(processNodeStatusList.get(0).getStatusName());
        processStateRecordMapper.insert(processStateRecord);
        return true;
    }

    /**
     * 描述- 完成操作 返回需要修改的字段
     * businessDataId 业务数据id
     * action 动作
     * objectData 数据
     *
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean complete(String businessDataId, String action, Object objectData) {
        if (StringUtils.isBlank(businessDataId)) {
            throw new OtherException("业务数据id不能为空");
        }
        if (StringUtils.isBlank(action)) {
            throw new OtherException("动作不能为空");
        }
        /*获取示例流程*/
        ProcessBusinessInstance processBusinessInstance = getProcessBusinessInstance(businessDataId);
        /**
         * 获取当前节点 状态下的条件及动作
         * //节点下的每个状态下的动作不对重复
         */
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.clear();
        queryWrapper.eq("nsc.node_id", processBusinessInstance.getAtPresentNodeId());
        queryWrapper.eq("nsc.original_status", processBusinessInstance.getAtPresentStatusName());
        queryWrapper.eq("na.action_name", action);
        queryWrapper.eq("na.status", BaseGlobal.STATUS_NORMAL);
        /*获取节点状态条件及动作*/
        ProcessNodeStatusConditionVo processNodeStatusConditionVo = processNodeStatusConditionMapperl.getCondition(queryWrapper);
        if (ObjectUtils.isEmpty(processNodeStatusConditionVo)) {
            throw new OtherException("该状态无此动作");
        }

        // 判断规则 -1表示全员
        if (!processNodeStatusConditionVo.getRuleUserId().equals("-1") && !processNodeStatusConditionVo.getRuleUserId().equals(baseController.getUserId())) {
            throw new OtherException("该用户无此操作");
        }
        /*获取当前节点*/
        ProcessNode processNode = processNodeMapper.selectById(processBusinessInstance.getAtPresentNodeId());

        /*获取节点记录的当前节点*/
        queryWrapper.clear();
        queryWrapper.eq("business_instance_id", processBusinessInstance.getId());
        queryWrapper.eq("node_id", processNode.getId());
        ProcessNodeRecord processNodeRecord = processNodeRecordMapper.selectOne(queryWrapper);

        /*条件是否满足*/
        isConditionSatisfy(processNodeStatusConditionVo, objectData);

        /**
         * 流转状态
         * 判断下个状态是否是最后一个状态，
         * 是流转节点： 修改当前节点，当前状态为修改节点的初始节点
         * 否则流转状态 节点不变 改变状态
         */
        queryWrapper.clear();
        queryWrapper.eq("node_id", processNodeStatusConditionVo.getNodeId());
        queryWrapper.eq("status_name", processNodeStatusConditionVo.getTargetStatus());
        ProcessNodeStatus processNodeStatus = processNodeStatusMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(processNodeStatus)) {
            throw new OtherException("无状态");
        }
        ProcessStateRecord processStateRecord = new ProcessStateRecord();
        /*判断是否为最后一个状态*/
        if (processNodeStatus.getEndStatus().equals(BaseGlobal.STATUS_NORMAL)) {
            /*流转状态*/
            processBusinessInstance.setAtPresentStatusName(processNodeStatus.getStatusName());
            processBusinessInstance.updateInit();
            processNodeRecord.updateInit();
            processNodeRecord.setAtPresentStatus(processNodeStatus.getStatusName());
            /*状态记录一条*/
            processStateRecord.insertInit();
            processStateRecord.setCompanyCode(baseController.getUserCompany());
            processStateRecord.setBusinessInstanceId(processBusinessInstance.getId());
            processStateRecord.setNodeRecordId(processNodeRecord.getId());
            processStateRecord.setStatus(processNodeStatus.getStatusName());
        } else {
            /**
             * 流转节点
             * 当前节点调整结束事件 当前状态 是否完成
             * 示例下一个节点信息
             */
            processNodeRecord.updateInit();
            processNodeRecord.setEndDate(new Date());
            processNodeRecord.setAtPresentStatus(processNodeStatus.getStatusName());
            processNodeRecord.setIsComplete(BaseGlobal.STATUS_CLOSE);

            processStateRecord.insertInit();
            processStateRecord.setCompanyCode(baseController.getUserCompany());
            processStateRecord.setBusinessInstanceId(processBusinessInstance.getId());
            processStateRecord.setNodeRecordId(processNodeRecord.getId());
            processStateRecord.setStatus(processNodeStatus.getStatusName());
            /*查询下一个节点记录*/
            queryWrapper.clear();
            queryWrapper.eq("business_instance_id", processBusinessInstance.getId());
            queryWrapper.eq("sort", processNodeRecord.getSort() + BaseGlobal.ONE);
            ProcessNodeRecord nextNodeRecord = processNodeRecordMapper.selectOne(queryWrapper);
            if (ObjectUtils.isEmpty(nextNodeRecord)) {
                /*最后一个节点*/
                processBusinessInstance.setIsComplete(BaseGlobal.STATUS_CLOSE);
                processBusinessInstance.updateInit();

            } else {
                /*获取下个节点的初始状态*/
                queryWrapper.clear();
                queryWrapper.eq("node_id", nextNodeRecord.getNodeId());
                queryWrapper.eq("start_status", BaseGlobal.STATUS_CLOSE);
                ProcessNodeStatus nextNodeStatus = processNodeStatusMapper.selectOne(queryWrapper);
                nextNodeRecord.setAtPresentStatus(nextNodeStatus.getStatusName());
                nextNodeRecord.setStartDate(new Date());
                /*修改下节点*/
                processNodeRecordMapper.updateById(nextNodeRecord);
                /*状态记录一条*/
                ProcessStateRecord nextStateRecord = new ProcessStateRecord();
                nextStateRecord.insertInit();
                nextStateRecord.setCompanyCode(baseController.getUserCompany());
                nextStateRecord.setBusinessInstanceId(processBusinessInstance.getId());
                nextStateRecord.setNodeRecordId(nextNodeRecord.getId());
                nextStateRecord.setStatus(nextNodeStatus.getStatusName());
                processStateRecordMapper.insert(nextStateRecord);
                /*调整流程示例数据*/
                processBusinessInstance.setAtPresentStatusName(nextNodeStatus.getStatusName());
                processBusinessInstance.setAtPresentNodeName(nextNodeRecord.getNodeName());
                processBusinessInstance.setAtPresentNodeId(nextNodeRecord.getNodeId());
                processBusinessInstance.updateInit();
            }

        }
        /*添加一条状态记录*/
        processStateRecordMapper.insert(processStateRecord);
        /*修改当前节点*/
        processNodeRecordMapper.updateById(processNodeRecord);
        /*修改实例*/
        baseMapper.updateById(processBusinessInstance);

        /*修改数据*/
        updateData(processNodeStatusConditionVo.getId(), processBusinessInstance.getProcessSchemeId(), businessDataId, objectData);

        throw new OtherException("成功成功");
//        return true;
    }

    /**
     * 描述-根据业务id查询当先下的动作
     *
     * @param businessDataId 业务数据id
     * @return
     */
    @Override
    public List<ProcessNodeStatusConditionVo> getActionBybusinessDataId(String businessDataId) {
        /**
         * 1获取当前节点当前状态下有的动作返回
         * 2返回 状态下的修改字段是否需要弹框输入值
         */
        /*获取示例流程*/
        ProcessBusinessInstance processBusinessInstance = getProcessBusinessInstance(businessDataId);
        /*获取到当前可流转的状态*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("node_id", processBusinessInstance.getAtPresentNodeId());
        queryWrapper.eq("original_status", processBusinessInstance.getAtPresentStatusName());
        List<ProcessNodeStatusCondition> statusConditionList = processNodeStatusConditionMapperl.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(statusConditionList)) {
            throw new OtherException("当前状态下无数据");
        }
        List<ProcessNodeStatusConditionVo> processNodeStatusConditionVoList = BeanUtil.copyToList(statusConditionList, ProcessNodeStatusConditionVo.class);
        for (ProcessNodeStatusConditionVo processNodeStatusCondition : processNodeStatusConditionVoList) {
            /*获取里面的动作*/
            queryWrapper.clear();
            queryWrapper.eq("node_status_condition_id", processNodeStatusCondition.getId());
            List<ProcessNodeAction> processNodeActionList = processNodeActionMapper.selectList(queryWrapper);
            List<ProcessNodeActionVo> processNodeActionVoList = BeanUtil.copyToList(processNodeActionList, ProcessNodeActionVo.class);
            processNodeActionVoList.forEach(processNodeActionVo -> {
                /*获取里面的字段*/
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("node_status_condition_id", processNodeStatusCondition.getId());
                queryWrapper1.eq("type", "3");
                List<ProcessNodeStatusUpdateManagement> updateManagementList = processNodeStatusUpdateManagementMapper.selectList(queryWrapper1);
                if (!CollectionUtils.isEmpty(updateManagementList)) {
                    List<String> stringList = updateManagementList.stream().map(ProcessNodeStatusUpdateManagement::getFieldManagementId).collect(Collectors.toList());
                    queryWrapper1.clear();
                    queryWrapper1.in("id", stringList);
                    /*设置字段*/
                    List<FieldManagement> fieldManagementList = fieldManagementMapper.selectList(queryWrapper1);
                    processNodeActionVo.setFieldManagementList(fieldManagementList);
                }
            });
            processNodeStatusCondition.setProcessNodeActionVoList(processNodeActionVoList);
        }
        return processNodeStatusConditionVoList;
    }

    /**
     * 描述 根据业务id查询流程节点
     *
     * @param businessDataId 业务数据id
     * @return
     */
    @Override
    public List<ProcessNodeRecordVo> getNodeBybusinessDataId(String businessDataId) {
        /*查询流程示例数据*/
        ProcessBusinessInstance processBusinessInstance = getProcessBusinessInstance(businessDataId);
        /**
         * 获取到流程的所有节点包括里面的状态
         */
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("business_instance_id", processBusinessInstance.getId());
        queryWrapper.orderByAsc("sort");
        List<ProcessNodeRecord> processNodeRecordList = processNodeRecordService.list(queryWrapper);
        List<ProcessNodeRecordVo> processNodeRecordVoList = BeanUtil.copyToList(processNodeRecordList, ProcessNodeRecordVo.class);
        processNodeRecordVoList.forEach(processNodeRecordVo -> {
            queryWrapper.clear();
            queryWrapper.eq("company_code", baseController.getUserCompany());
            queryWrapper.eq("node_id", processNodeRecordVo.getNodeId());
            List<ProcessNodeStatus> processNodeStatusList = processNodeStatusMapper.selectList(queryWrapper);
            processNodeRecordVo.setProcessNodeStatusList(processNodeStatusList);
        });
        return processNodeRecordVoList;
    }


    /**
     * 描述 修改业务数据
     */
    void updateData(String nodeStatusConditionId, String processSchemeId, String businessDataId, Object objectData) {
        /*方案*/
        ProcessProcessScheme processProcessScheme = processProcessSchemeMapper.selectById(processSchemeId);
        /*获取到修改的字段*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("node_status_condition_id", nodeStatusConditionId);
        List<ProcessNodeStatusUpdateManagement> updateManagementList = processNodeStatusUpdateManagementMapper.selectList(queryWrapper);
        try {
            /*获取到修改的Service服务*/
            BaseServiceImpl servicePlus = SpringContextHolder.getBean(processProcessScheme.getServiceName());
            /*修改*/
            UpdateWrapper updateWrapper = new UpdateWrapper();
            /*组装数据*/
            updateManagementList.forEach(u -> {
                /*查询到表单字段*/
                FieldManagement fieldManagement = fieldManagementMapper.selectById(u.getFieldManagementId());
                /*转换成数据库字段*/
                String FieldName = camelCaseToUnderscore(fieldManagement.getFieldName());
                if (u.getType().equals("0")) {
                    updateWrapper.set(FieldName, u.getUpdateText());
                } else if (u.getType().equals("1")) {
                    updateWrapper.set(FieldName, baseController.getUserId());
                } else if (u.getType().equals("2")) {
                    updateWrapper.set(FieldName, new Date());
                } else if (u.getType().equals("3")) {
                    Object s1 = BeanUtil.getProperty(objectData, fieldManagement.getFieldName());
                    if (!ObjectUtils.isEmpty(s1)) {
                        updateWrapper.set(FieldName, s1.toString());
                    }
                }
            });
            updateWrapper.eq("id", businessDataId);
            servicePlus.update(updateWrapper);
        } catch (Exception e) {
            throw new OtherException("修改失败");
        }
    }


    /**
     * 获取校验流程示例
     */
    public ProcessBusinessInstance getProcessBusinessInstance(String businessDataId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("business_data_id", businessDataId);
        ProcessBusinessInstance processBusinessInstance = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(processBusinessInstance)) {
            throw new OtherException("业务数据id错误");
        }
        if (processBusinessInstance.getIsComplete().equals(BaseGlobal.STATUS_CLOSE)) {
            throw new OtherException("该流程已完成");
        }
        return processBusinessInstance;
    }


    /**
     * 判断条件是否满足
     * formId 表单id
     * nodeCondition 公式
     * objectData 数据
     */
    public void isConditionSatisfy(ProcessNodeStatusConditionVo processNodeStatusConditionVo, Object objectData) {
        /**
         * 解析表达式 替换里面的占位符
         */
        String nodeConditionFormula = processNodeStatusConditionVo.getNodeConditionFormula();
        /*表达式里面的占位符*/
        String placeholder = getPlaceholder(nodeConditionFormula);
        Map<String, Object> env = new HashMap<>(16);
        if (!StringUtils.isBlank(placeholder)) {
            String[] placeholders = placeholder.split(",");
            for (String s : placeholders) {
                Object s1 = BeanUtil.getProperty(objectData, s);
                nodeConditionFormula = nodeConditionFormula.replace("${" + s + "}", "$" + s + "$");
                env.put("$" + s + "$", s1);
            }
        }
        boolean b = false;
        try {
            // 函数计算
            b = (boolean) AviatorEvaluator.execute(nodeConditionFormula, env);
        } catch (Exception e) {
            throw new OtherException("表达式计算异常");
        }
        if (!b) {
            /*不满足条件返回的数据*/
            throw new OtherException(processNodeStatusConditionVo.getReminder());
        }
    }


    /*“thisIsAString”，则输出字符串将是 “this_is_a_string”。*/
    public String camelCaseToUnderscore(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                // 如果当前字符是大写字母，将其转换为小写字母并在前面添加下划线
                output.append("_").append(Character.toLowerCase(currentChar));
            } else {
                // 否则，直接将字符添加到输出中
                output.append(currentChar);
            }
        }
        return output.toString();
    }

    /**
     * 获取占位符字段
     * str
     */
    public String getPlaceholder(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(str);
        String placeholders = "";
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            placeholders += placeholder + ",";
        }
        return placeholders.substring(0, placeholders.length() - 1);
    }

}
