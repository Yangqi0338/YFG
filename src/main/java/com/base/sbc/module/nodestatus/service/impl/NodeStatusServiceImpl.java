/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.nodestatus.mapper.NodeStatusMapper;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 类描述：节点状态记录 service类
 * @address com.base.sbc.module.nodestatus.service.NodeStatusService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 17:34:59
 * @version 1.0  
 */
@Service
public class NodeStatusServiceImpl extends ServicePlusImpl<NodeStatusMapper, NodeStatus> implements NodeStatusService {


// 自定义方法区 不替换的区域【other_start】

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public NodeStatus nodeStatusChange(String dataId, String node, String status) {

        // 1、修改上一个节点状态的完成时间
        NodeStatus currentNodeStatus = getCurrentNodeStatusByDataId(dataId);
        Date nowDate = new Date();
        if(currentNodeStatus!=null){
            UpdateWrapper<NodeStatus> uw=new UpdateWrapper<>();
            uw.set("end_date", nowDate);
            uw.eq("id",currentNodeStatus.getId());
            update(uw);
        }
        // 2、 保存当前流程节点状态
        NodeStatus nextNodeStatus=new NodeStatus();
        nextNodeStatus.setNode(node);
        nextNodeStatus.setStatus(status);
        nextNodeStatus.setStartDate(nowDate);
        nextNodeStatus.setDataId(dataId);
        save(nextNodeStatus);
        return nextNodeStatus;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,OtherException.class})
    public NodeStatus finishCurrentNodeStatus(String dataId) {
        NodeStatus nodeStatus = getCurrentNodeStatusByDataId(dataId);
        if(nodeStatus==null){
            throw  new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        nodeStatus.setEndDate(new Date());
        updateById(nodeStatus);
        return  nodeStatus;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,OtherException.class})
    public NodeStatus finishNodeStatus(String dataId, String node, String status) {
        QueryWrapper<NodeStatus> qw=new QueryWrapper<>();
        qw.eq("data_id",dataId);
        qw.eq("node",node);
        qw.eq("status",status);
        qw.orderByDesc("start_date");
        PageHelper.startPage(1, 1);
        NodeStatus nodeStatus = getOne(qw);
        if(nodeStatus==null){
            throw  new OtherException(BaseErrorEnum.ERR_SELECT_NOT_FOUND);
        }
        nodeStatus.setEndDate(new Date());
        updateById(nodeStatus);
        return nodeStatus;
    }

    @Override
    public NodeStatus getCurrentNodeStatusByDataId(String dataId) {
        QueryWrapper<NodeStatus> qw=new QueryWrapper<>();
        qw.eq("data_id",dataId);
        qw.orderByDesc("start_date");
        PageHelper.startPage(1, 1);
        NodeStatus currentNode = getOne(qw);
        return currentNode;
    }

// 自定义方法区 不替换的区域【other_end】
	
}
