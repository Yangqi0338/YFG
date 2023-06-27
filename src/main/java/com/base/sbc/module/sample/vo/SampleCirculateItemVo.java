package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SampleCirculateItemVo {

    @ApiModelProperty(value = "编号")
    private String id;

    @ApiModelProperty(value = "样衣主表ID")
    private String sampleId;

    @ApiModelProperty(value = "状态：0-未入库，1-在库，2-借出，3-删除")
    private Integer status;

    @ApiModelProperty(value = "图片。多图英文“,”逗号分隔。显示第一张")
    private String images;

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "样衣编号")
    private String code;

    @ApiModelProperty(value = "款式名称")
    private String styleName;

    @ApiModelProperty(value = "品类：(大类/品类/中类/小类)")
    private String categoryName;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "尺码")
    private String size;

    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供")
    private Integer type;

    @ApiModelProperty(value = "入仓时间")
    private String storeDate;

    @ApiModelProperty(value = "位置")
    private String position;

    @ApiModelProperty(value = "设计来源名称，人员名称或供应商名称")
    private String fromName;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
