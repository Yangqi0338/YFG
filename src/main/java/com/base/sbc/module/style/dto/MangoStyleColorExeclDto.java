package com.base.sbc.module.style.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class MangoStyleColorExeclDto {

    @Excel(name = "*年份", width = 20)
    private String year;

    @Excel(name = "*品牌", width = 20)
    private String brandName;

    @Excel(name = "*季节", width = 20)
    private String season;

    @Excel(name = "*产品季", width = 20)
    private String productSeason;

    @Excel(name = "*品类", width = 20)
    private String prodCategoryName;

    @Excel(name = "*大货款号", width = 20)
    private String styleColorNo;

    @Excel(name = "款式配色图", width = 20)
    private String styleColorImage;

    /*@Excel(name = "合作方颜色名称", width = 20, orderNum = "20")
    private String outsideColorName;

    @Excel(name = "合作方颜色编码", width = 20, orderNum = "21")
    private String outsideColorCode;*/

    @Excel(name = "*颜色名称", width = 20)
    private String colorName;

    @Excel(name = "*颜色代码", width = 20)
    private String colorCode;

    @Excel(name = "*款式类型", width = 20)
    private String styleTypeName;

    @Excel(name = "*大类", width = 20)
    private String prodCategory1stName;

    @Excel(name = "*中类", width = 20)
    private String prodCategory2ndName;

    @Excel(name = "*号型类型", width = 20)
    private String sizeRangeName;

    @Excel(name = "*尺码", width = 20)
    private String sizeCode;

    @Excel(name = "*合作方条形码", width = 20)
    private String outsideBarcode;

    @Excel(name = "*生产类型", width = 20)
    private String devtTypeName;

    @Excel(name = "*吊牌价", width = 20)
    private String tagPrice;

}
