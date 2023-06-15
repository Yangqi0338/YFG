/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.service;

import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.nodestatus.dto.NodeStatusChangeDto;
import com.base.sbc.module.nodestatus.entity.NodeStatus;

import java.util.List;

/** 
 * 类描述：节点状态记录 service类
 * @address com.base.sbc.module.nodestatus.service.NodeStatusService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 17:34:59
 * @version 1.0  
 */
public interface NodeStatusService extends IServicePlus<NodeStatus>{

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
     * @param dataId
     */
    NodeStatus finishCurrentNodeStatus(String dataId);

    /**
     * 完成指定节点
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


// 自定义方法区 不替换的区域【other_end】


}
