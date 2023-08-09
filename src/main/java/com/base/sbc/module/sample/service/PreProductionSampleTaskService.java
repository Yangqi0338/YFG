/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service;

import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.patternmaking.dto.NodeStatusChangeDto;
import com.base.sbc.module.sample.dto.PreProductionSampleTaskSearchDto;
import com.base.sbc.module.sample.dto.PreTaskAssignmentDto;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.vo.PreProductionSampleTaskListVo;

import java.security.Principal;
import java.util.List;

/**
 * 类描述：产前样-任务 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.sample.service.PreProductionSampleTaskService
 * @email your email
 * @date 创建时间：2023-7-18 11:04:08
 */
public interface PreProductionSampleTaskService extends BaseService<PreProductionSampleTask> {

// 自定义方法区 不替换的区域【other_start】

    boolean enableSetting(String id, String enableFlag);

    boolean taskAssignment(PreTaskAssignmentDto dto);

    public boolean nodeStatusChange(String userId, List<NodeStatusChangeDto> list, GroupUser groupUser);

    /**
     * 节点状态改变
     *
     * @param dto
     * @return
     */
    boolean nodeStatusChange(String userId, NodeStatusChangeDto dto, GroupUser groupUser);

    List<PreProductionSampleTaskListVo> taskList(PreProductionSampleTaskSearchDto dto);

    boolean nextOrPrev(Principal user, String id, String next);
// 自定义方法区 不替换的区域【other_end】


}
