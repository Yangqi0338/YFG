/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.tasklist.dto.QueryPageTaskListDTO;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.vo.TaskListVO;
import com.github.pagehelper.PageInfo;

/**
 * 任务列表 Service
 *
 * @author XHTE
 * @create 2024/7/10
 */
public interface TaskListService extends BaseService<TaskList>{

// 自定义方法区 不替换的区域【other_start】

    PageInfo<TaskListVO> findPage(QueryPageTaskListDTO queryPageTaskList);

// 自定义方法区 不替换的区域【other_end】

	
}