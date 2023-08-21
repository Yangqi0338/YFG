/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pushRecords.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.JSONStringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pushRecords.mapper.PushRecordsMapper;
import com.base.sbc.module.pushRecords.entity.PushRecords;
import com.base.sbc.module.pushRecords.service.PushRecordsService;
import com.base.sbc.module.smp.dto.HttpResp;
import org.springframework.stereotype.Service;

/**
 * 类描述：推送记录表 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pushRecords.service.PushRecordsService
 * @email your email
 * @date 创建时间：2023-7-9 17:33:48
 */
@Service
public class PushRecordsServiceImpl extends BaseServiceImpl<PushRecordsMapper, PushRecords> implements PushRecordsService {


// 自定义方法区 不替换的区域【other_start】

    /**
     * 构造对象并且保存
     *
     * @param httpResp     请求响应结果
     * @param data         推送的数据
     * @param moduleName   模块名称
     * @param functionName 功能方法名称
     * @return 推送的结果
     */
    @Override
    public Boolean pushRecordSave(HttpResp httpResp, Object data, String moduleName, String functionName) {
        PushRecords pushRecords = new PushRecords();

        String jsonString = JSONStringUtils.toJSONString(data);
        JSONObject json = JSON.parseObject(jsonString);

        pushRecords.setModuleName(moduleName);
        pushRecords.setFunctionName(functionName);
        pushRecords.setRelatedId(json.getString("id"));
        pushRecords.setRelatedName(json.getString("name"));
        pushRecords.setPushAddress(httpResp.getUrl());
        pushRecords.setPushContent(jsonString);
        pushRecords.setPushCount("1");
        pushRecords.setPushStatus(httpResp.isSuccess() ? "成功" : "失败");
        pushRecords.setResponseMessage(httpResp.getMessage());
        pushRecords.setResponseStatusCode(httpResp.getCode());
        pushRecords.setId(json.getString("syncId"));
        this.save(pushRecords);
        return httpResp.isSuccess();
    }


// 自定义方法区 不替换的区域【other_end】

}

