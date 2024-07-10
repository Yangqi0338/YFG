/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务列表分页查询接收对象
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务列表分页查询接收对象")
public class QueryPageTaskListDTO extends Page {

    private static final long serialVersionUID = 1L;

    {
        // 默认 Excel 导出标记为 0
        excelFlag = 0;
    }

    /**
     * Excel 导出标记（0-否，1-是)
     */
    @ApiModelProperty("Excel 导出标记（0-否，1-是)")
    private Integer excelFlag;

}
