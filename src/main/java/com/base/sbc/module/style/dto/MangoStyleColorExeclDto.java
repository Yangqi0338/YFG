package com.base.sbc.module.style.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MangoStyleColorExeclDto {

    @ApiModelProperty(name = "年份")
    @Excel(name = "*年份", width = 20)
    private String year;

    @ApiModelProperty(name = "品牌")
    @Excel(name = "*品牌", width = 20)
    private String brandName;

    @ApiModelProperty(name = "季节")
    @Excel(name = "*季节", width = 20)
    private String season;

    /*@ApiModelProperty(name = "产品季")
    @Excel(name = "*产品季", width = 20)
    private String productSeason;*/

    @ApiModelProperty(name = "大货款号")
    @Excel(name = "*大货款号", width = 20)
    private String styleColorNo;

    @ApiModelProperty(name = "大类")
    @Excel(name = "*大类", width = 20)
    private String prodCategory1stName;

    @ApiModelProperty(name = "品类")
    @Excel(name = "*品类", width = 20)
    private String prodCategoryName;

    @ApiModelProperty(name = "中类")
    @Excel(name = "*中类", width = 20)
    private String prodCategory2ndName;

    @ApiModelProperty(name = "款式类型")
    @Excel(name = "*款式类型", width = 20)
    private String styleTypeName;

    @ApiModelProperty(name = "产品季性别")
    @Excel(name = "产品季性别", width = 20)
    private String gender;

    @ApiModelProperty(name = "号型类型")
    @Excel(name = "*号型类型", width = 20)
    private String sizeRangeName;

    /*@Excel(name = "款式配色图", width = 20)
    private String styleColorImage;*/

    //@Excel(name = "款式配色图",imageType = 2,type = 2,width = 20)
    //private byte[] styleColorPic1;

    //private String styleColorPic;

    @ApiModelProperty(name = "外部尺码")
    @Excel(name = "*外部尺码", width = 20)
    private String outsideSizeCode;

    @ApiModelProperty(name = "外部颜色编码")
    @Excel(name = "*外部颜色编码", width = 20)
    private String outsideColorCode;

    /*@ApiModelProperty(name = "外部颜色名称")
    @Excel(name = "*外部颜色名称", width = 20)
    private String outsideColorName;*/

    //@ApiModelProperty(name = "合作方条形码")
    @Excel(name = "外部条形码", width = 20)
    private String outsideBarcode;

    /*@ApiModelProperty(name = "颜色名称")
    @Excel(name = "*颜色名称", width = 20)
    private String colorName;

    @ApiModelProperty(name = "颜色代码")
    @Excel(name = "*颜色代码", width = 20)
    private String colorCode;*/



    /*@ApiModelProperty(name = "尺码")
    @Excel(name = "*尺码", width = 20)
    private String sizeCode;*/

    /*@ApiModelProperty(name = "生产类型")
    @Excel(name = "*生产类型", width = 20)
    private String devtTypeName;*/

    @ApiModelProperty(name = "吊牌价")
    @Excel(name = "*吊牌价", width = 20)
    private String tagPrice;

    @ApiModelProperty(name = "成本价")
    @Excel(name = "*成本价", width = 20)
    private String planCostPrice;

    @Excel(name = "包装形式", width = 20)
    private String packagingForm;

}
