/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pushrecords.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.base.sbc.config.common.BaseLambdaQueryWrapper;
import com.base.sbc.config.constant.SmpProperties;
import com.base.sbc.config.enums.business.PushRespStatus;
import com.base.sbc.config.resttemplate.RestTemplateService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pushrecords.dto.PushRecordsDto;
import com.base.sbc.module.pushrecords.entity.PushRecords;
import com.base.sbc.module.pushrecords.mapper.PushRecordsMapper;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.smp.dto.HttpReq;
import com.base.sbc.module.smp.dto.HttpResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    @Autowired
    private RestTemplateService restTemplateService;

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
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Boolean pushRecordSave(HttpResp httpResp, String data) {
        JSONArray jsonArray;
        if (JSON.isValidArray(data)) {
            jsonArray = JSON.parseArray(data);
        }else {
            jsonArray = new JSONArray();
            jsonArray.add(JSON.parseObject(data));
        }
        for (int i = 0, jsonArraySize = jsonArray.size(); i < jsonArraySize; i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String url = httpResp.getUrl();

            String code = httpResp.getCode();
            if (StrUtil.isBlank(code)) {
                code = StrUtil.firstNonBlank(
                        json.getString("code"),
                        json.getString("styleNo"),
                        json.getString("bulkStyleNo"),
                        json.getString("designNo")
                );
            }
            PushRecordsDto pushRecordsDto = new PushRecordsDto();
            pushRecordsDto.setRelatedId(code);
            pushRecordsDto.setPushAddress(url);
            pushRecordsDto.reset2QueryFirst();
            PushRecords pushRecords = pushRecordsList(pushRecordsDto)
                    .stream().findFirst().orElse(BeanUtil.copyProperties(pushRecordsDto, PushRecords.class));

//        pushRecords.setModuleName(moduleName);
//        pushRecords.setFunctionName(functionName);
            pushRecords.setRelatedName(json.getString("name"));
            pushRecords.setPushContent(data);
            pushRecords.setPushCount(Opt.ofBlankAble(pushRecords.getPushCount()).orElse(1));
            pushRecords.setPushStatus(httpResp.isSuccess() ? PushRespStatus.SUCCESS : PushRespStatus.FAILURE);
            pushRecords.setResponseMessage(httpResp.isSuccess() ? httpResp.getData() : httpResp.getMessage());
            pushRecords.setResponseStatusCode(httpResp.getStatusCode());
            this.saveOrUpdate(pushRecords);
        }
        return httpResp.isSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public PushRecords prePushRecordSave(HttpReq httpReq) {
        String url = httpReq.getUrl();
//        PushRecordsDto pushRecordsDto = new PushRecordsDto();
//        pushRecordsDto.setRelatedId(httpReq.getCode());
//        pushRecordsDto.setPushAddress(url);
//        pushRecordsDto.reset2QueryFirst();
//        PushRecords pushRecords = pushRecordsList(pushRecordsDto)
//                .stream().findFirst().orElse(BeanUtil.copyProperties(pushRecordsDto, PushRecords.class));
        PushRecords pushRecords = new PushRecords();

        pushRecords.setRelatedId(httpReq.getCode());
        pushRecords.setRelatedName(httpReq.getName());
        pushRecords.setBusinessId(httpReq.getBusinessId());
        pushRecords.setBusinessCode(httpReq.getBusinessCode());
        pushRecords.setModuleName(httpReq.getModuleName());
        pushRecords.setFunctionName(httpReq.getFunctionName());
        pushRecords.setPushAddress(url);
        pushRecords.setPushContent(httpReq.getData());
        pushRecords.setPushCount(1);
        for (SmpProperties.SystemModuleEnum systemEnum : SmpProperties.SystemModuleEnum.values()) {
            if (url.startsWith(systemEnum.getUrl())) {
                pushRecords.setPushCount(systemEnum.getRetryNum());
            }
        }

        pushRecords.setPushStatus(PushRespStatus.PROCESS);
        this.save(pushRecords);
        return pushRecords;
    }

    @Override
    public List<PushRecords> pushRecordsList(PushRecordsDto pushRecords) {
        pushRecords.startPage();
        return this.list(new BaseLambdaQueryWrapper<PushRecords>()
                .notNullEq(PushRecords::getPushStatus, pushRecords.getPushStatus())
                .notNullNe(PushRecords::getPushStatus, pushRecords.getNePushStatus())
                .notEmptyEq(PushRecords::getPushCount, pushRecords.getPushCount())
                .between(PushRecords::getUpdateDate, pushRecords.getUpdateDate())
                .notEmptyIn(PushRecords::getRelatedId, pushRecords.getRelatedId())
                .eq(PushRecords::getPushAddress, pushRecords.getPushAddress())
                .isNull(StrUtil.isBlank(pushRecords.getRelatedId()), PushRecords::getRelatedId)
                .orderByDesc(PushRecords::getCreateDate)
        );
    }

    @Override
    public List<PushRecords> existHandlePushRecord() {
        String[] dateArray = Stream.of(LocalDateTime.now().minusDays(1), LocalDateTime.now())
                .map(it -> it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).toArray(String[]::new);

        LambdaQueryWrapper<PushRecords> qw = new BaseLambdaQueryWrapper<PushRecords>()
                .between(PushRecords::getUpdateDate, dateArray)
                .eq(PushRecords::getPushStatus, PushRespStatus.PROCESS);

        boolean exists = this.exists(qw);
        if (!exists) return new ArrayList<>();
        return this.list(qw);
    }

    @Override
    public boolean rePush(String id) {
        PushRecords pushRecords = this.getById(id);
        HttpResp httpResp = restTemplateService.spmPost(pushRecords.getPushAddress(), pushRecords.getPushContent());
        pushRecords.setPushStatus(httpResp.isSuccess() ? PushRespStatus.SUCCESS : PushRespStatus.FAILURE);
        pushRecords.setResponseMessage(httpResp.getMessage());
        pushRecords.setResponseStatusCode(httpResp.getCode());
        pushRecords.setPushCount(pushRecords.getPushCount() + 1);
        this.updateById(pushRecords);
        return httpResp.isSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchRePushNewLog(List<String> ids) {
        List<PushRecords> pushRecordsList = this.listByIds(ids);
        int count = 0;
        for (PushRecords pushRecords : pushRecordsList) {
            HttpResp httpResp = restTemplateService.spmPost(pushRecords.getPushAddress(), pushRecords.getPushContent());
            pushRecords.setPushStatus(httpResp.isSuccess() ? PushRespStatus.SUCCESS : PushRespStatus.FAILURE);
            pushRecords.setResponseMessage(httpResp.getMessage());
            pushRecords.setResponseStatusCode(httpResp.getCode());
            pushRecords.setPushCount(0);
            pushRecords.setId(null);
            if(httpResp.isSuccess()){
                count++;
            }
        }
        this.saveBatch(pushRecordsList);
        return count;
    }

    @Override
    public void closeStatus(List<String> ids) throws Exception {
        List<PushRecords> pushRecords = listByIds(ids);
        long count = pushRecords.stream().filter(o -> !PushRespStatus.FAILURE.equals(o.getPushStatus())).count();
        if(count > 0){
            throw new Exception("只能关闭失败的数据");
        }
        LambdaUpdateWrapper<PushRecords> updateWrapper = new LambdaUpdateWrapper<PushRecords>()
                .set(PushRecords::getPushStatus, PushRespStatus.CLOSE)
                .in(PushRecords::getId, ids);
        this.update(updateWrapper);
    }


// 自定义方法区 不替换的区域【other_end】

}

