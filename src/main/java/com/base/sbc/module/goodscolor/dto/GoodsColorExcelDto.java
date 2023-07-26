/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.goodscolor.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
public class GoodsColorExcelDto {



    /** 颜色分类id */
    private String colorTypeId;
    /** 颜色十六进制 */
    private String colorHex;
    /** 颜色编码 */
    @Excel(name = "颜色编码")
    private String colorCode;
    /** 颜色RGB */
    @Excel(name = "RGB三角")
    private String colorRgb;

    /** 颜色 */
    @Excel(name = "颜色")
    private String color;
    /** 颜色分类 */
    private String colorClassify;
    /** pantone色号 */
    private String pantoneCode;
    /** pantone名称 */
    @Excel(name = "潘通色")
    private String pantoneName;
    /** 色系 */

    private String  colorType;
    /** 色系编码 */
    @Excel(name = "颜色系")
    private String  colorTypeName;;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /*备注*/
    @Excel(name = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name = "启用" , replace = {"true_0", "false_1"} )
    private String status;
}
