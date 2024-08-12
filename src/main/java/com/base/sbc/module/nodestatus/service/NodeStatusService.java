/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.nodestatus.dto.NodeStatusChangeDto;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：节点状态记录 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.nodestatus.service.NodeStatusService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 17:34:59
 */
public interface NodeStatusService extends BaseService<NodeStatus> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 节点状态改变（到下一个节点）
     *
     * @param dataId
     * @param node
     * @param status
     */
    NodeStatus nodeStatusChange(String dataId, String node, String status, String startFlg, String endFlg);

    /**
     * 完成当前节点
     *
     * @param dataId
     */
    NodeStatus finishCurrentNodeStatus(String dataId);

    /**
     * 完成指定节点
     *
     * @param dataId
     */
    NodeStatus finishNodeStatus(String dataId, String node, String status);

    /**
     * 获取当前节点
     *
     * @param dataId
     */
    NodeStatus getCurrentNodeStatusByDataId(String dataId);

    /**
     * 设置节点状态数据
     *
     * @param obj
     * @param listKey 集合key
     * @param mapKey  mapkey
     */
    void setNodeStatusToBean(Object obj, String listKey, String mapKey);

    /**
     * 节点状态改版 批量
     *
     * @param list
     */
    void nodeStatusChangeBatch(List<NodeStatusChangeDto> list);

    /**
     * 设置节点状态到list
     *
     * @param list
     * @param dataIdKey
     * @param listKey
     * @param mapKey
     */
    void setNodeStatusToListBean(List list, String dataIdKey, String listKey, String mapKey);

    void hasNodeStatusAuth(String userId, BaseDataEntity bean, JSONObject nodeConfig);

    JSONObject getNodeStatusConfig(String nodeStatusConfigKey, String node, String status);

    void setNodeStatus(List list);

    boolean nextOrPrev(GroupUser user, BaseDataEntity bean, String nodeStatusConfigKey, String np);


    /**
     * 获取上一步或者下一步的配置
     *
     * @param nodeStatusConfigKey
     * @param node
     * @param status
     * @param np                  prev 上 ,next下
     * @return
     */
    JSONObject getNodeNextOrPrev(String nodeStatusConfigKey, String node, String status, String np);

    /**
     * 获取节点 并判断是否有权限
     *
     * @param user
     * @param bean
     * @param patternMakingNodeStatus
     * @return
     */
    JSONObject getNodeNextAndAuth(GroupUser user, BaseDataEntity bean, String patternMakingNodeStatus, String np);

// 自定义方法区 不替换的区域【other_end】
    List<NodeStatus> nsWorkList(@Param(Constants.WRAPPER) QueryWrapper qw);

    @Transactional(rollbackFor = {Exception.class})
    NodeStatus replaceNode(String dataId, String node, String status, String startFlg, String endFlg);
}
