/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pushrecords.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pushrecords.dto.PushRecordsDto;
import com.base.sbc.module.pushrecords.entity.PushRecords;
import com.base.sbc.module.smp.dto.HttpReq;
import com.base.sbc.module.smp.dto.HttpResp;

import java.util.List;

/**
 * 类描述：推送记录表 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pushRecords.service.PushRecordsService
 * @email your email
 * @date 创建时间：2023-7-9 17:33:48
 */
public interface PushRecordsService extends BaseService<PushRecords> {

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
    Boolean pushRecordSave(HttpResp httpResp, String data, String moduleName, String functionName);
    PushRecords prePushRecordSave(HttpReq httpReq);
    List<PushRecords> pushRecordsList(PushRecordsDto pushRecords);
    List<PushRecords> existHandlePushRecord();
    boolean rePush(String id);

// 自定义方法区 不替换的区域【other_end】


}

