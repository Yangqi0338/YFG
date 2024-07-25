/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.tasklist.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;

/**
 * 导出任务列表 Excel 对象
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Data
@ApiModel("导出任务列表 Excel 对象")
@ColumnWidth(25)
@ContentRowHeight(60)
@ContentStyle(
        wrapped = BooleanEnum.TRUE,
        horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER,
        shrinkToFit = BooleanEnum.TRUE
)
public class ExportTaskListExcelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号（单据编号）
     */
    @ApiModelProperty(value = "任务编号（单据编号）")
    @ExcelProperty(value = "任务编号")
    private String taskCode;
    /**
     * 任务类型（1-款式打标下发）
     */
    @ApiModelProperty(value = "任务类型（1-款式打标下发）")
    @ExcelProperty(value = "任务类型")
    private String taskType;
    /**
     * 任务状态（1-待办 2-已办）
     */
    @ApiModelProperty(value = "任务状态（1-待办 2-已办）")
    @ExcelProperty(value = "任务状态")
    private String taskStatus;
    /**
     * 任务名称（「任务类型：单据编号」）
     */
    @ApiModelProperty(value = "任务名称（任务类型：单据编号）")
    @ExcelProperty(value = "任务名称")
    private String taskName;
    /**
     * 任务内容（「单据编号：同步结果」同步结果举例：总共下发xx条，成功xx条，失败xx条）
     */
    @ApiModelProperty(value = "任务内容（「单据编号：同步结果」同步结果举例：总共下发xx条，成功xx条，失败xx条）")
    @ExcelProperty(value = "任务内容")
    private String taskContent;
    /**
     * 发起人名称
     */
    @ApiModelProperty(value = "发起人名称")
    @ExcelProperty(value = "发起人名称")
    private String initiateUserName;
    /**
     * 接收人名称
     */
    @ApiModelProperty(value = "接收人名称")
    @ExcelProperty(value = "接收人名称")
    private String receiveUserName;
    /**
     * 接收时间
     */
    @ApiModelProperty(value = "接收时间")
    @ExcelProperty(value = "接收时间")
    private String receiveDate;


}
