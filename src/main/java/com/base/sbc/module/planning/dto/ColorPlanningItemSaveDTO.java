package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("颜色保存")
public class ColorPlanningItemSaveDTO {
    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
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
