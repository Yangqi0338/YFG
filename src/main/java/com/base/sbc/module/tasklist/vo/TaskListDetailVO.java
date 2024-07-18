/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.vo;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.base.sbc.module.tasklist.entity.TaskList;
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：任务列表详情返回对象
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务列表详情返回对象")
@ColumnWidth(25)
@ContentRowHeight(60)
@ContentStyle(
        wrapped = BooleanEnum.TRUE,
        horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER,
        shrinkToFit = BooleanEnum.TRUE
)
public class TaskListDetailVO extends TaskListDetail {

    private static final long serialVersionUID = 1L;

}
