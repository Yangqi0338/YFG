/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：颜色企划明细 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.entity.ColorPlanningItem
 * @email your email
 * @date 创建时间：2023-8-15 13:58:55
 */
@Data
@ApiModel("颜色企划明细 ColorPlanningItem")
public class ColorPlanningItemVO {

    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 颜色企划id
     */
    @ApiModelProperty(value = "颜色企划id")
    private String colorPlanningId;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /**
     * 潘通色号
     */
    @ApiModelProperty(value = "潘通色号")
    private String pantoneColor;
    /**
     * 颜色图
     */
    @ApiModelProperty(value = "颜色图")
    private String colorPic;
    /**
     * 标签（多个逗号分割）
     */
    @ApiModelProperty(value = "标签（多个逗号分割）")
    private String tag;
    /**
     * 标签编码（多个逗号分割）
     */
    @ApiModelProperty(value = "标签编码（多个逗号分割）")
    private String tagCode;
    /**
     * 颜色规格
     */
    @ApiModelProperty(value = "颜色规格")
    private String colorSpec;
    /**
     * 色系
     */
    @ApiModelProperty(value = "色系")
    private String colorSystem;
    /**
     * 16进制
     */
    @ApiModelProperty(value = "16进制")
    private String colorHex;
}

