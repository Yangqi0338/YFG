package com.base.sbc.module.style.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class MangoStyleColorExeclDto {

    @Excel(name = "年份")
    private String year;

    @Excel(name = "品牌")
    private String brandName;

    @Excel(name = "季节")
    private String season;

    //
}
