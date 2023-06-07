/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service.impl;

import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.process.entity.ProcessNodeRecord;
import com.base.sbc.module.process.mapper.ProcessNodeRecordMapper;
import com.base.sbc.module.process.service.ProcessNodeRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：流程配置-节点记录 service类
 * @address com.base.sbc.module.process.service.ProcessNodeRecordService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-6 15:03:28
 * @version 1.0  
 */
@Service
public class ProcessNodeRecordServiceImpl extends ServicePlusImpl<ProcessNodeRecordMapper, ProcessNodeRecord> implements ProcessNodeRecordService {

    /**
     * 描述- 批量新增
     * initiateProcessDto 发起流程dto
     *
     * @param processNodeRecordList
     * @return
     */
    @Override
    public Boolean batchAddition(List<ProcessNodeRecord> processNodeRecordList) {
        saveOrUpdateBatch(processNodeRecordList);
        return true;
    }
}
