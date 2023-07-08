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
 * 类描述：导入导出基础资料-档差 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumRangeDifference
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-18 19:42:16
 * @version 1.0
 */
@Data
@ApiModel("基础资料-档差 BasicsdatumRangeDifference")
public class BasicsdatumRangeDifferenceExcelDto  {

    // @Excel(name = "id")
    private String id;
    private String measurementIds;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    @Excel(name = "编码")
    private String code;
    /** 档差 */
    @ApiModelProperty(value = "档差"  )
    @Excel(name = "档差")
    private String rangeDifference;
    /** 测量点 */
    @ApiModelProperty(value = "测量点"  )
    @Excel(name = "测量点")
    private String measurement;
    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    @Excel(name = "号型类型")
    private String modelType;
    /** 尺码 */
    @ApiModelProperty(value = "尺码"  )
    @Excel(name = "尺码")
    private String size;
    /** 基础尺码 */
    @ApiModelProperty(value = "基础尺码"  )
    @Excel(name = "基础的尺码")
    private String basicsSize;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    @Excel(name = "图片",type = 2)
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "可用",replace = {"true_0", "false_1"})
    private String status;
}
