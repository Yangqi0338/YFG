/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.process.dto.InitiateProcessDto;
import com.base.sbc.module.process.entity.ProcessBusinessInstance;
import com.base.sbc.module.process.vo.ProcessNodeRecordVo;
import com.base.sbc.module.process.vo.ProcessNodeStatusConditionVo;

import java.util.List;

/** 
 * 类描述：流程配置-业务实例 service类
 * @address com.base.sbc.module.process.service.ProcessBusinessInstanceService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-6 15:03:26
 * @version 1.0  
 */
public interface ProcessBusinessInstanceService extends BaseService<ProcessBusinessInstance> {

    /**
     * 描述- 发起流程
     * initiateProcessDto 发起流程dto
     * @return
     */
    Boolean initiateProcess(InitiateProcessDto initiateProcessDto);


    /**
     * 描述- 完成操作
     * businessDataId 业务数据id
     * actionCode 动作
     * @return
     */
    Boolean complete(String businessDataId, String actionCode, Object objectData);


    /**
     * 描述-根据业务id查询当先下的动作
     * @param businessDataId 业务数据id
     * @return
     */
    List<ProcessNodeStatusConditionVo> getActionBybusinessDataId(String businessDataId);

    /**
     * 描述 根据业务id查询流程节点
     * @param businessDataId 业务数据id
     * @return
     */
    List<ProcessNodeRecordVo> getNodeBybusinessDataId(String businessDataId);
}
