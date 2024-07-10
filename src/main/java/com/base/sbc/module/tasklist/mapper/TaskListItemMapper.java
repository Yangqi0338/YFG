/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.tasklist.entity.TaskListItem;

/**
 * 任务列表详情 Mapper
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Mapper
public interface TaskListItemMapper extends BaseMapper<TaskListItem> {

}