/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.process.dto.InitiateProcessDto;
import com.base.sbc.module.process.entity.*;
import com.base.sbc.module.process.mapper.*;
import com.base.sbc.module.process.service.ProcessBusinessInstanceService;
import com.base.sbc.module.process.service.ProcessNodeRecordService;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;
import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class ProcessBusinessInstanceServiceImpl extends ServicePlusImpl<ProcessBusinessInstanceMapper, ProcessBusinessInstance> implements ProcessBusinessInstanceService {

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
    public Map complete(String businessDataId, String action, Object objectData) {
        if (StringUtils.isBlank(businessDataId)) {
            throw new OtherException("业务数据id不能为空");
        }
        if (StringUtils.isBlank(action)) {
            throw new OtherException("动作不能为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("business_data_id", businessDataId);
        queryWrapper.eq("is_complete",BaseGlobal.STATUS_NORMAL);
        ProcessBusinessInstance processBusinessInstance = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(processBusinessInstance)) {
            throw new OtherException("业务数据id错误");
        }
        if(processBusinessInstance.getIsComplete().equals(BaseGlobal.STATUS_CLOSE)){
            throw new OtherException("该流程已完成");
        }
        /*获取当前节点 当前动作下的条件*/
        queryWrapper.clear();
        queryWrapper.eq("nsc.node_id", processBusinessInstance.getAtPresentNodeId());
        queryWrapper.eq("nsc.original_status", processBusinessInstance.getAtPresentStatusName());
        queryWrapper.eq("na.action_name", action);
        queryWrapper.eq("na.status",BaseGlobal.STATUS_NORMAL);
        /*获取节点状态条件及动作*/
        ProcessNodeStatusConditionVo processNodeStatusConditionVo = processNodeStatusConditionMapperl.getCondition(queryWrapper);
        if (ObjectUtils.isEmpty(processNodeStatusConditionVo)) {
            throw new OtherException("该状态无此动作");
        }
        boolean b1 = isConditionSatisfy(processNodeStatusConditionVo.getUpdateField(), processNodeStatusConditionVo, objectData);
        if(b1){
            return new HashMap();
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
        boolean b = isConditionSatisfy(processNodeStatusConditionVo.getUpdateField(), processNodeStatusConditionVo, objectData);

        if (!b) {
            throw new OtherException("条件不满足");
        }

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
            queryWrapper.eq("sort", processNodeRecord.getSort() + 1);
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
        Map map=new HashMap();
        map.put("updateField",processNodeStatusConditionVo.getUpdateField());
        return map;
    }

    /**
     * 判断条件是否满足
     * formId 表单id
     * nodeCondition 公式
     * objectData 数据
     */
    boolean isConditionSatisfy(String updateField, ProcessNodeStatusConditionVo processNodeStatusConditionVo, Object objectData) {

      String nodeConditionFormula =  processNodeStatusConditionVo.getNodeConditionFormula();
       /*表达式里面的占位符*/
       String placeholder =  getPlaceholder(nodeConditionFormula);
        if (!StringUtils.isBlank(placeholder)) {
            String[] placeholders = placeholder.split(",");
            for (String s : placeholders) {
                JSONObject jsonObject = JSONObject.parseObject(objectData.toString());
                nodeConditionFormula =  nodeConditionFormula.replace("${"+s+"}",jsonObject.get(s).toString());
            }
        }
        boolean b = true;
        try {
            b = (boolean) AviatorEvaluator.execute(nodeConditionFormula);

        } catch (Exception e) {
            throw new OtherException(e.toString());
        }
        if(!b){
            throw new OtherException(processNodeStatusConditionVo.getReminder());
        }
        return true;
    }



    /**
     * 获取占位符字段
     * str
     */
    String getPlaceholder(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(str);
        String placeholders = "";
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            placeholders += placeholder +",";
        }
        return placeholders.substring(0, placeholders.length() - 1);
    }

}
