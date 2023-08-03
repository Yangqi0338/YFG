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
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.entity.CompanyPost;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.BaseService;
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
            List<NodeStatus> list = list(qw);
            if (CollUtil.isEmpty(list)) {
                return;
            }

            Map<String, NodeStatus> nodeStatus = list.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> {
                if (DateUtil.compare(b.getStartDate(), a.getStartDate()) > 0) {
                    return b;
                }
                return a;
            }));
            BeanUtil.setProperty(obj, listKey, list);
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
    public JSONObject getNodeStatusConfig(String nodeStatusConfigKey, String userId, String node, String status, String dataId, BaseService baseService) {
        // 如果所有不为空 则判断是否有此节点状态的权限
        if (StrUtil.isNotBlank(dataId)) {
            Object t = hasNodeStatusAuth(nodeStatusConfigKey, userId, dataId, baseService);
            node = BeanUtil.getProperty(t, "node");
            status = BeanUtil.getProperty(t, "status");
        }
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
    public Object hasNodeStatusAuth(String nodeStatusConfigKey, String userId, String dataId, BaseService baseService) {
        // 获取当前节点
        // 0 查询打版数据
        Object pm = baseService.getById(dataId);
        if (pm == null) {
            throw new OtherException("打版指令数据不存在");
        }
        String planningSeasonId = BeanUtil.getProperty(pm, "planningSeasonId");
        String node = BeanUtil.getProperty(pm, "node");
        String status = BeanUtil.getProperty(pm, "status");

        // 1判断是否有产品季权限
        List<String> planningSeasonIds = amcFeignService.getPlanningSeasonIdByUserId(getUserId());
        if (CollUtil.isEmpty(planningSeasonIds) || !CollUtil.contains(planningSeasonIds, planningSeasonId)) {
            throw new OtherException("你不在该产品季人员当中");
        }
        // 2 判断是否有下一步岗位权限
        // 2.0 获取当前节点需要的角色权限
        JSONObject nodeStatusConfig = getNodeStatusConfig(nodeStatusConfigKey, userId, node, status, null, baseService);
        if (nodeStatusConfig == null) {
            return pm;
        }
        JSONObject auth = nodeStatusConfig.getJSONObject("auth");
        if (auth == null) {
            return pm;
        }
        //角色or岗位匹配会+1
        int flg = 0;
        List<String> msg = new ArrayList<>(4);
        JSONArray authUserIdArr = auth.getJSONArray("userId");
        JSONArray authPostArr = auth.getJSONArray("post");
        if (ObjectUtil.isEmpty(authUserIdArr) && ObjectUtil.isEmpty(authPostArr)) {
            return pm;
        }
        if (ObjectUtil.isNotEmpty(authUserIdArr)) {
            //判断当前userId是否和授权的userId相等
            List<String> authUserIds = authUserIdArr.toJavaList(String.class).stream().map(item -> {
                return (String) BeanUtil.getProperty(pm, item);
            }).collect(Collectors.toList());
            if (authUserIds.contains(userId)) {
                flg++;
            } else {
                msg.add("用户不匹配");
            }

        }
        if (ObjectUtil.isNotEmpty(authPostArr) && flg == 0) {
            // 2.1 获取当前登录人员岗位
            UserCompany userInfo = amcFeignService.getUserInfo(getUserId(), BaseGlobal.YES);
            List<String> userPostName = Opt.ofNullable(userInfo.getPostList()).map(pl -> pl.stream().map(CompanyPost::getName).collect(Collectors.toList())).orElse(new ArrayList<>());
            List<String> authPost = authPostArr.toJavaList(String.class);
            // 是否有交集
            Collection<String> intersection = CollUtil.intersection(userPostName, authPost);
            if (CollUtil.isNotEmpty(intersection)) {
                flg++;
            } else {
                msg.add("岗位不匹配,需要[" + StrUtil.join(StrUtil.COMMA, authPost) + "]");
            }
        }
        //无匹配项抛出异常
        if (flg == 0) {
            throw new OtherException(CollUtil.join(msg, StrUtil.COMMA));
        }
        return pm;

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
                nodeStatusVos.sort((a, b) -> {
                    return a.getStartDate().compareTo(b.getStartDate());
                });
                BeanUtil.setProperty(o, "nodeStatusList", nodeStatusVos);
            }
            if (StrUtil.equals(suspend, BaseGlobal.YES)) {
                BeanUtil.setProperty(o, "startDate", updateDate);
            }
        }
    }

// 自定义方法区 不替换的区域【other_end】

}
