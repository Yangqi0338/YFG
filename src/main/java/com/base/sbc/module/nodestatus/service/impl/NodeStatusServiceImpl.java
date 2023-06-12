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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.mapper.NodeStatusMapper;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class NodeStatusServiceImpl extends ServicePlusImpl<NodeStatusMapper, NodeStatus> implements NodeStatusService {


// 自定义方法区 不替换的区域【other_start】

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
        qw.orderByDesc("start_date");
        PageHelper.startPage(1, 1);
        NodeStatus currentNode = getOne(qw);
        return currentNode;
    }

    @Override
    public void set(Object obj, String listKey, String mapKey) {
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

// 自定义方法区 不替换的区域【other_end】

}
