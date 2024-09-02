package com.base.sbc.module.band.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("基础资料-波段导入 BandExcelDto")
public class BandExcelDto {
//    @Excel(name = "id")
    private String id;
    /** 编码 */
    @Excel(name = "编码")
    private String code;
    /** 波段名称 */
    @Excel(name = "波段名称")
    private String bandName;

    private String season;
    /** 季节 */
    @Excel(name = "季节")
    private String seasonName;

    @ApiModelProperty(value = "品牌"  )
    private String brand;

    @ApiModelProperty(value = "品牌名称"  )
    @Excel(name = "品牌")
    private String brandName;

    /** 月份 */
    @Excel(name = "月份")
    private String month;
    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    public static void main(String[] args) {
        JSONObject entries = new JSONObject();
        for (String s : entries.keySet()) {

        }
    }
}
