/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.nodestatus.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.nodestatus.entity.NodeStatusConfig;
import com.base.sbc.module.nodestatus.mapper.NodeStatusConfigMapper;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import org.springframework.stereotype.Service;

/**
 * 类描述：节点状态配置信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.nodestatus.service.NodeStatusConfigService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-19 13:33:10
 */
@Service
public class NodeStatusConfigBaseServiceImpl extends BaseServiceImpl<NodeStatusConfigMapper, NodeStatusConfig> implements NodeStatusConfigService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public NodeStatusConfig getByType(String type, String companyCode) {
        QueryWrapper<NodeStatusConfig> qw = new QueryWrapper<>();
        qw.eq("type", type);
        qw.eq("COMPANY_CODE", companyCode);
        qw.last("limit 1");
        return this.getOne(qw);
    }

    @Override
    public JSONObject getConfig2Json(String type, String companyCode) {
        NodeStatusConfig byType = getByType(type, companyCode);
        if (byType != null) {
            return JSONObject.parseObject(byType.getConfig(), Feature.OrderedField);
        }
        return null;
    }


// 自定义方法区 不替换的区域【other_end】

}
