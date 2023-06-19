/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.service;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.nodestatus.entity.NodeStatusConfig;

/**
 * 类描述：节点状态配置信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.nodestatus.service.NodeStatusConfigService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-19 13:33:10
 */
public interface NodeStatusConfigService extends IServicePlus<NodeStatusConfig> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 打版节点配置 key
     */
    String PATTERN_MAKING_NODE_STATUS = "PATTERN_MAKING_NODE_STATUS";

    NodeStatusConfig getByType(String type, String companyCode);

    /**
     * 获取配置 转成json
     *
     * @param type
     * @param companyCode 企业编码
     * @return
     */
    JSONObject getConfig2Json(String type, String companyCode);

// 自定义方法区 不替换的区域【other_end】


}
