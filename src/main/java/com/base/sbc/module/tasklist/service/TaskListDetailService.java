/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDetailDTO;
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import com.base.sbc.module.tasklist.vo.TaskListDetailVO;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageInfo;


/**
 * 任务列表详情 Service
 *
 * @author XHTE
 * @create 2024/7/10
 */
public interface TaskListDetailService extends BaseService<TaskListDetail> {

    /**
     * 查询任务列表详情列表分页
     *
     * @param queryPageTaskListDetail 查询条件
     * @return 查询任务列表详情列表分页
     */
    PageInfo<TaskListDetailVO> queryTaskListDetailPage(QueryPageTaskListDetailDTO queryPageTaskListDetail);

}