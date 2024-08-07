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
import com.base.sbc.module.tasklist.entity.TaskListDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 导出款式打标下发任务列表详情 Excel 对象
 *
 * @author XHTE
 * @create 2024/7/10
 */
@Data
@ApiModel("导出款式打标下发任务列表详情 Excel 对象")
@ColumnWidth(25)
@ContentRowHeight(60)
@ContentStyle(
        wrapped = BooleanEnum.TRUE,
        horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER,
        shrinkToFit = BooleanEnum.TRUE
)
public class ExportStyleMarkingIssuedExcelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误信息（用来记录这个数据为什么下发失败）
     */
    @ApiModelProperty(value = "错误信息（用来记录这个数据为什么下发失败）")
    @ExcelProperty(value = "错误信息")
    private String errorInfo;
    /**
     * 同步结果（1-成功 2-失败）
     */
    @ApiModelProperty(value = "同步结果（1-成功 2-失败）")
    @ExcelProperty(value = "同步结果")
    private String syncResult;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    @ExcelProperty(value = "大货款号")
    private String styleNo;

}
