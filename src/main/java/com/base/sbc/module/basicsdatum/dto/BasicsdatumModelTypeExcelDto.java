/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：导入导出基础资料-号型类型 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumModelType
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 9:31:14
 * @version 1.0
 */
@Data
@ApiModel("基础资料-号型类型 BasicsdatumModelType")
public class BasicsdatumModelTypeExcelDto  {

    @Excel(name = "id")
    private String id;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    @Excel(name = "号型类型")
    private String modelType;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    @Excel(name = "号型编码")
    private String coding;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    @Excel(name = "描述")
    private String description;
    /** Dimension 1 Type */
    @ApiModelProperty(value = "Dimension 1 Type"  )
    @Excel(name = "Dimension 1 Type")
    private String dimensionType;
    /** 尺码 */
    @ApiModelProperty(value = "尺码"  )
    @Excel(name = "尺码")
    private String size;
    /** 基础 尺码 */
    @ApiModelProperty(value = "基础 尺码"  )
    @Excel(name = "基础 尺码")
    private String basicsSize;
    /** 款式确认(0确定，1未确认) */
    @ApiModelProperty(value = "款式确认(0确定，1未确认)"  )
    @Excel(name = "款式确认", replace = {"true_0", "false_1"} )
    private String styleConfirmation;
    /** 材料确认(0确定，1未确认) */
    @ApiModelProperty(value = "材料确认(0确定，1未确认)"  )
    @Excel(name = "材料确认", replace = {"true_0", "false_1"} )
    private String materialsConfirmation;
    /** 尺寸确认(0确定，1未确认) */
    @ApiModelProperty(value = "尺寸确认(0确定，1未确认)"  )
    @Excel(name = "尺寸确认", replace = {"true_0", "false_1"} )
    private String sizeConfirmation;
    /** 发送状态(0发送，1未发送) */
    @ApiModelProperty(value = "发送状态(0发送，1未发送)"  )
    @Excel(name = "发送状态", replace = {"已发送_0", "未发送_1"} )
    private String sendStatus;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    @Excel(name = "品类")
    private String category;
    /** 品类id */
    @ApiModelProperty(value = "品类id"  )
    @Excel(name = "品类id")
    private String categoryId;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "状态(0正常,1停用)")
    private String status;
    private String sizeLabelId;
}
