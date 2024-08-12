/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.message.utils.MessageUtils;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.dto.NodeStatusChangeDto;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.mapper.NodeStatusMapper;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：节点状态记录 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.nodestatus.service.NodeStatusService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 17:34:59
 */
@Service
public class NodeStatusServiceImpl extends BaseServiceImpl<NodeStatusMapper, NodeStatus> implements NodeStatusService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    private AmcFeignService amcFeignService;
    @Autowired
    private NodeStatusConfigService nodeStatusConfigService;
    @Autowired
    private MessageUtils messageUtils;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public NodeStatus nodeStatusChange(String dataId, String node, String status, String startFlg, String endFlg) {

        // 1、修改上一个节点状态的完成时间
        NodeStatus currentNodeStatus = getCurrentNodeStatusByDataId(dataId);
        Date nowDate = new Date();
        if (currentNodeStatus != null) {
            UpdateWrapper<NodeStatus> uw = new UpdateWrapper<>();
            uw.set("end_date", nowDate);
            uw.eq("id", currentNodeStatus.getId());
            update(uw);
        }
        // 2 查询是否存在当前节点状态
        QueryWrapper<NodeStatus> nsQw = new QueryWrapper<>();
        nsQw.eq("data_id", dataId);
        nsQw.eq("node", node);
        nsQw.eq("status", status);
        nsQw.orderByAsc("id");
        List<NodeStatus> nsList = this.list(nsQw);
        if (CollUtil.isNotEmpty(nsList)) {
            //获取第一个id
            String firstId = nsList.get(0).getId();
            //删除已经存在的状态
            QueryWrapper<NodeStatus> deQcIds = new QueryWrapper<>();
            deQcIds.eq("data_id", dataId);
            deQcIds.ge("id", firstId);
            this.remove(deQcIds);
        }
        // 3、 保存当前流程节点状态
        NodeStatus nextNodeStatus = new NodeStatus();
        nextNodeStatus.setNode(node);
        nextNodeStatus.setStatus(status);
        nextNodeStatus.setStartDate(nowDate);
        nextNodeStatus.setDataId(dataId);
        nextNodeStatus.setStartFlg(startFlg);
        nextNodeStatus.setEndFlg(endFlg);
        if (StrUtil.equals(endFlg, BaseGlobal.YES)) {
            nextNodeStatus.setEndDate(nowDate);
        }
        save(nextNodeStatus);
        return nextNodeStatus;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public NodeStatus finishCurrentNodeStatus(String dataId) {
        NodeStatus nodeStatus = getCurrentNodeStatusByDataId(dataId);
        if (nodeStatus == null) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        nodeStatus.setEndDate(new Date());
        updateById(nodeStatus);
        return nodeStatus;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public NodeStatus finishNodeStatus(String dataId, String node, String status) {
        QueryWrapper<NodeStatus> qw = new QueryWrapper<>();
        qw.eq("data_id", dataId);
        qw.eq("node", node);
        qw.eq("status", status);
        qw.orderByDesc("start_date");
        PageHelper.startPage(1, 1);
        NodeStatus nodeStatus = getOne(qw);
        if (nodeStatus == null) {
            throw new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        nodeStatus.setEndDate(new Date());
        updateById(nodeStatus);
        return nodeStatus;
    }

    @Override
    public NodeStatus getCurrentNodeStatusByDataId(String dataId) {
        QueryWrapper<NodeStatus> qw = new QueryWrapper<>();
        qw.eq("data_id", dataId);
        qw.isNull("end_date");
        qw.orderByDesc("id");
        PageHelper.startPage(1, 1);
        NodeStatus currentNode = getOne(qw);
        return currentNode;
    }

    @Override
    public void setNodeStatusToBean(Object obj, String listKey, String mapKey) {
        try {
            String id = BeanUtil.getProperty(obj, "id");
            if (StrUtil.isBlank(id)) {
                return;
            }
            QueryWrapper<NodeStatus> qw = new QueryWrapper<>();
            qw.eq("data_id", id);
            qw.eq("del_flag", BaseGlobal.NO);
            qw.orderByAsc("start_date");
            List<NodeStatus> beanList = list(qw);
            if (CollUtil.isEmpty(beanList)) {
                return;
            }
            List<NodeStatusVo> voList = BeanUtil.copyToList(beanList, NodeStatusVo.class);
            Map<String, NodeStatusVo> nodeStatus = voList.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> {
                if (DateUtil.compare(b.getStartDate(), a.getStartDate()) > 0) {
                    return b;
                }
                return a;
            }));
            BeanUtil.setProperty(obj, listKey, voList);
            BeanUtil.setProperty(obj, mapKey, nodeStatus);
        } catch (Exception e) {
            log.error("设置节点状态信息失败", e);
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void nodeStatusChangeBatch(List<NodeStatusChangeDto> list) {
        for (NodeStatusChangeDto dto : list) {
            nodeStatusChange(dto.getDataId(), dto.getNode(), dto.getStatus(), dto.getStartFlg(), dto.getEndFlg());
        }
    }

    @Override
    public void setNodeStatusToListBean(List list, String dataIdKey, String listKey, String mapKey) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        if (StrUtil.isAllBlank(listKey, mapKey)) {
            return;
        }
        // 获取dataId
        List<String> dataIds = new ArrayList<>(16);
        for (Object vo : list) {
            String dataId = BeanUtil.getProperty(vo, dataIdKey);
            if (StrUtil.isBlank(dataId)) {
                continue;
            }
            dataIds.add(dataId);
        }
        //查询节点状态数据
        QueryWrapper<NodeStatus> qw = new QueryWrapper<>();
        qw.in("data_id", dataIds);
        List<NodeStatus> nodeStatusList = list(qw);
        if (CollUtil.isEmpty(nodeStatusList)) {
            return;
        }
        List<NodeStatusVo> nodeStatusVos = BeanUtil.copyToList(nodeStatusList, NodeStatusVo.class);
        Map<String, List<NodeStatusVo>> nsMap = nodeStatusVos.stream().collect(Collectors.groupingBy(NodeStatusVo::getDataId));
        for (Object vo : list) {
            List<NodeStatusVo> pmNsList = nsMap.get(BeanUtil.getProperty(vo, dataIdKey));
            if (CollUtil.isEmpty(pmNsList)) {
                continue;
            }
            Map<String, NodeStatusVo> pmNsMap = pmNsList.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> b));
            if (StrUtil.isNotBlank(listKey)) {
                BeanUtil.setProperty(vo, listKey, pmNsList);
            }
            if (StrUtil.isNotBlank(mapKey)) {
                BeanUtil.setProperty(vo, mapKey, pmNsMap);
            }
        }

    }

    @Override
    public JSONObject getNodeStatusConfig(String nodeStatusConfigKey, String node, String status) {
        JSONObject config = nodeStatusConfigService
                .getConfig2Json(nodeStatusConfigKey, getCompanyCode());
        if (StrUtil.isNotBlank(node) && config != null) {
            config = config.getObject(node, JSONObject.class, Feature.OrderedField);
            if (StrUtil.isNotBlank(status) && config != null) {
                config = config.getObject(status, JSONObject.class, Feature.OrderedField);
            }
        }
        return config;
    }


    @Override
    public void hasNodeStatusAuth(String userId, BaseDataEntity bean, JSONObject nodeStatusConfig) {
        // 2 判断是否有下一步岗位权限
        // 2.0 获取当前节点需要的角色权限
        if (ObjectUtil.isEmpty(nodeStatusConfig)) {
            return;
        }
        JSONObject check = nodeStatusConfig.getJSONObject("check");
        if (ObjectUtil.isEmpty(check)) {
            return;
        }
        //

        List<String> msg = new ArrayList<>(4);
        JSONObject auth = check.getJSONObject("auth");
        boolean authMatch = false;
        if (ObjectUtil.isNotEmpty(auth)) {
            // 匹配用户
            JSONArray authUserIdArr = auth.getJSONArray("userId");
            // 匹配部门用户类型 2为样衣组长
            JSONArray deptUserType = auth.getJSONArray("deptUserType");
            if (ObjectUtil.isNotEmpty(authUserIdArr)) {
                boolean userMatch = false;
                for (int i = 0; i < authUserIdArr.size(); i++) {
                    JSONObject userConfig = authUserIdArr.getJSONObject(i);
                    String userIdVal = BeanUtil.getProperty(bean, userConfig.getString("id"));
                    String userNameVal = BeanUtil.getProperty(bean, userConfig.getString("name"));
                    String msgStr = userConfig.getString("msg");
                    if (StrUtil.equals(userIdVal, userId)) {
                        userMatch = true;
                        break;
                    } else {
                        msg.add("【" + msgStr + "】不匹配,需要【" + userNameVal + "】");
                    }
                }
                authMatch = userMatch;
            }

            //部门用户类型
            if (ObjectUtil.isNotEmpty(deptUserType) && !authMatch) {
                boolean deptMatch = false;
                for (int i = 0; i < deptUserType.size(); i++) {
                    JSONObject deptConfig = deptUserType.getJSONObject(i);
                    String field = deptConfig.getString("id");
                    String name = deptConfig.getString("name");
                    String val = deptConfig.getString("val");

                    String deptId = BeanUtil.getProperty(bean, field);
                    List<UserCompany> deptManager = amcFeignService.getDeptManager(deptId, val);
                    UserCompany one = CollUtil.findOne(deptManager, (uc) -> StrUtil.equals(uc.getUserId(), userId));
                    if (one != null) {
                        deptMatch = true;
                        break;
                    } else {
                        String msgStr = "【" + BeanUtil.getProperty(bean, name) + "】" + deptConfig.getString("msg");
                        msg.add(msgStr);
                    }
                }
                authMatch = deptMatch;
            }
        } else {
            authMatch = true;
        }

        //非空校验
        boolean requiredCheck = true;
        JSONArray required = check.getJSONArray("required");
        if (CollUtil.isNotEmpty(required)) {
            for (int i = 0; i < required.size(); i++) {
                JSONObject jsonObject = required.getJSONObject(i);
                String field = jsonObject.getString("field");
                String msgStr = jsonObject.getString("msg");
                if (ObjectUtil.isEmpty(BeanUtil.getProperty(bean, field))) {
                    msg.add(msgStr);
                    requiredCheck = false;
                }
            }
        }
        //权限校验没过或者飞空校验没过 跑出异常
        if (!requiredCheck || !authMatch) {
            throw new OtherException(CollUtil.join(msg, StrUtil.COMMA));
        }
    }

    @Override
    public void setNodeStatus(List list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<String> ids = new ArrayList<>(12);
        for (Object o : list) {
            ids.add(BeanUtil.getProperty(o, "id"));
        }
        // 查询所有状态
        QueryWrapper<NodeStatus> qw = new QueryWrapper<>();
        qw.in("data_id", ids);
        qw.orderByAsc("start_date");
        List<NodeStatus> nodeStatusList = list(qw);
        if (CollUtil.isEmpty(nodeStatusList)) {
            return;
        }
        // 设置状态
        Map<String, List<NodeStatus>> nodeStatusMap = nodeStatusList.stream().collect(Collectors.groupingBy(NodeStatus::getDataId));
        for (Object o : list) {
            List<NodeStatus> nodeStatusList1 = nodeStatusMap.get(BeanUtil.getProperty(o, "id"));
            String suspend = BeanUtil.getProperty(o, "suspend");
            Date updateDate = BeanUtil.getProperty(o, "updateDate");
            if (CollUtil.isNotEmpty(nodeStatusList1)) {
                String node = BeanUtil.getProperty(o, "node");
                String status = BeanUtil.getProperty(o, "status");
                List<NodeStatusVo> nodeStatusVos = BeanUtil.copyToList(nodeStatusList1, NodeStatusVo.class);
                Map<String, NodeStatusVo> startDataMap = nodeStatusVos.stream().collect(Collectors.toMap(k -> k.getNode() + k.getStatus(), v -> v, (a, b) -> b, LinkedHashMap::new));
                BeanUtil.setProperty(o, "startDate", Optional.ofNullable(startDataMap.get(node + status)).map(NodeStatusVo::getStartDate).orElse(null));
                //nodeStatusVos.sort(Comparator.comparing(NodeStatusVo::getStartDate));
                BeanUtil.setProperty(o, "nodeStatusList", nodeStatusVos);
            }
            if (StrUtil.equals(suspend, BaseGlobal.YES)) {
                BeanUtil.setProperty(o, "startDate", updateDate);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nextOrPrev(GroupUser user, BaseDataEntity bean, String nodeStatusConfigKey, String np) {
        JSONObject config = getNodeNextAndAuth(user, bean, nodeStatusConfigKey, np);
        List<NodeStatusChangeDto> nsList = config.getJSONArray("node").toJavaList(NodeStatusChangeDto.class);
        if (CollUtil.isEmpty(nsList)) {
            throw new OtherException(np + "配置错误");
        }
        nsList.forEach(ns -> {
            ns.setDataId(bean.getId());
        });
        nodeStatusChangeBatch(nsList);
        NodeStatusChangeDto lastNs = CollUtil.getLast(nsList);
        //设置值
        setUpdateVal(bean, config, user);
        BeanUtil.setProperty(bean, "node", lastNs.getNode());
        BeanUtil.setProperty(bean, "status", lastNs.getStatus());
        /*发送消息*/
        messageUtils.sampleTaskSendMessage(bean,config,lastNs.getStatus(),user);
        return true;
    }

    private void setUpdateVal(BaseDataEntity bean, JSONObject config, GroupUser user) {
        JSONArray jsonArray = config.getJSONArray(NodeStatusConfigService.UPDATE);
        if (CollUtil.isEmpty(jsonArray)) {
            return;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String valType = jsonObject.getString("valType");
            Object val = jsonObject.get("val");
            String field = jsonObject.getString("field");
            if (StrUtil.equals(valType, "loginName")) {
                try {
                    BeanUtil.setProperty(bean, field, user.getName());
                } catch (Exception e) {
                    log.error("设置值失败(" + field + ")", e);
                }
            } else if (StrUtil.equals(valType, "loginId")) {
                try {
                    BeanUtil.setProperty(bean, field, user.getId());
                } catch (Exception e) {
                    log.error("设置值失败(" + field + ")", e);
                }
            } else if (StrUtil.equals(valType, "nowDateTime")) {
                try {
                    BeanUtil.setProperty(bean, field, new Date());
                } catch (Exception e) {
                    log.error("设置值失败(" + field + ")", e);
                }
            } else {
                try {
                    BeanUtil.setProperty(bean, field, val);
                } catch (Exception e) {
                    log.error("设置值失败(" + field + ")", e);
                }
            }
        }
    }


    @Override
    public JSONObject getNodeNextOrPrev(String nodeStatusConfigKey, String node, String status, String np) {
        JSONObject config = null;
        try {
            config = nodeStatusConfigService
                    .getConfig2Json(nodeStatusConfigKey, getCompanyCode());
            config = config.getObject(node, JSONObject.class, Feature.OrderedField);
            config = config.getObject(status, JSONObject.class, Feature.OrderedField);
            config = config.getObject(np, JSONObject.class, Feature.OrderedField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public JSONObject getNodeNextAndAuth(GroupUser user, BaseDataEntity bean, String nodeStatusConfigKey, String np) {
        String node = BeanUtil.getProperty(bean, "node");
        String status = BeanUtil.getProperty(bean, "status");
        JSONObject config = getNodeNextOrPrev(nodeStatusConfigKey, node, status, np);
        if (config == null) {
            throw new OtherException(np + "配置错误");
        }
        //校验是否有权限
        hasNodeStatusAuth(user.getId(), bean, config);
        return config;
    }

    @Override
    public List<NodeStatus> nsWorkList(QueryWrapper qw) {
        return getBaseMapper().nsWorkList(qw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public NodeStatus replaceNode(String dataId, String node, String status, String startFlg, String endFlg) {
        Date nowDate = new Date();

        //  查询是否存在当前节点状态
        QueryWrapper<NodeStatus> nsQw = new QueryWrapper<>();
        nsQw.eq("data_id", dataId);
        nsQw.eq("node", node);
        nsQw.eq("status", status);
        nsQw.orderByAsc("id");
        List<NodeStatus> nsList = this.list(nsQw);
        if (CollUtil.isNotEmpty(nsList)) {
            //获取第一个id
            String firstId = nsList.get(0).getId();
            //删除已经存在的状态
            QueryWrapper<NodeStatus> deQcIds = new QueryWrapper<>();
            deQcIds.eq("data_id", dataId);
            deQcIds.ge("id", firstId);
            this.remove(deQcIds);
        }
        // 保存当前流程节点状态
        NodeStatus nextNodeStatus = new NodeStatus();
        nextNodeStatus.setNode(node);
        nextNodeStatus.setStatus(status);
        nextNodeStatus.setStartDate(nowDate);
        nextNodeStatus.setDataId(dataId);
        nextNodeStatus.setStartFlg(startFlg);
        nextNodeStatus.setEndFlg(endFlg);
        if (StrUtil.equals(endFlg, BaseGlobal.YES)) {
            nextNodeStatus.setEndDate(nowDate);
        }
        save(nextNodeStatus);
        return nextNodeStatus;
    }

// 自定义方法区 不替换的区域【other_end】

}
