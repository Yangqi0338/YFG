/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.dto;

import com.base.sbc.config.common.base.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

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

    /**
     * 任务编号（单据编号）
     */
    @ApiModelProperty(value = "任务编号（单据编号）")
    private String taskCode;
    /**
     * 任务类型（1-款式打标下发）
     */
    @ApiModelProperty(value = "任务类型（1-款式打标下发）")
    private Integer taskType;
    /**
     * 任务状态（1-待办 2-已办）
     */
    @ApiModelProperty(value = "任务状态（1-待办 2-已办）")
    private Integer taskStatus;
    /**
     * 任务名称（「任务类型：单据编号」）
     */
    @ApiModelProperty(value = "任务名称（任务类型：单据编号）")
    private String taskName;
    /**
     * 任务内容（「单据编号：同步结果」同步结果举例：总共下发xx条，成功xx条，失败xx条）
     */
    @ApiModelProperty(value = "任务内容（「单据编号：同步结果」同步结果举例：总共下发xx条，成功xx条，失败xx条）")
    private String taskContent;
    /**
     * 接收时间
     */
    @ApiModelProperty(value = "接收时间")
    private List<String> receiveDate;
}
