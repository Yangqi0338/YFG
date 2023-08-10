/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.entity.SampleItemLog;
import com.base.sbc.module.sample.enums.SampleLogOperationType;
import com.base.sbc.module.sample.mapper.SampleItemLogMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：样衣明细日志 service类
 *
 * @address com.base.sbc.module.sample.service.SampleItemLogService
 */
@Service
public class SampleItemLogServiceImpl extends BaseServiceImpl<SampleItemLogMapper, SampleItemLog> implements SampleItemLogService {
    @Autowired
    SampleItemLogMapper mapper;

    private IdGen idGen = new IdGen();

    @Override
    public List<SampleItemLog> getListBySampleItemId(String sampleItemId, String type) {
        QueryWrapper<SampleItemLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sample_item_id", sampleItemId);
        if (SampleLogOperationType.BORROW.getK().equals(type)) {
            queryWrapper.eq("type", type);
        } else {
            queryWrapper.ne("type", SampleLogOperationType.BORROW.getK());
        }
        return mapper.selectList(queryWrapper);
    }

    @Override
    public boolean save(String sampleItemId, String type, String remarks) {
        SampleItemLog log = new SampleItemLog();

        log.setId(idGen.nextIdStr());
        log.setSampleItemId(sampleItemId);
        log.setType(type);
        log.setRemarks(remarks);

        Integer count = mapper.insert(log);
        return count > 0 ? true : false;
    }

    @Override
    public void saveBatch(List<String> sampleItemIds, String type, String remarks) {
        List<SampleItemLog> sampleItemLogs = sampleItemIds.stream()
                .map(sampleItemId -> {
                    SampleItemLog log = new SampleItemLog();
                    log.setId(idGen.nextIdStr());
                    log.setSampleItemId(sampleItemId);
                    log.setType(type);
                    log.setRemarks(remarks);
                    return log;
                }).collect(Collectors.toList());
        super.saveBatch(sampleItemLogs);
    }
}

