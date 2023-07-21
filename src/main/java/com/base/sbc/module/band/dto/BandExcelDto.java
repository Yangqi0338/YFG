package com.base.sbc.module.band.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("基础资料-波段导入 BandExcelDto")
public class BandExcelDto {
//    @Excel(name = "id",width = 20)
    private String id;
    /** 编码 */
    @Excel(name = "编码",width = 20)
    private String code;
    /** 波段名称 */
    @Excel(name = "波段名称",width = 20)
    private String bandName;

    private String season;
    /** 季节 */
    @Excel(name = "季节",width = 20)
    private String seasonName;
    /** 月份 */
    @Excel(name = "月份",width = 20)
    private String month;
    /** 排序 */
    @Excel(name = "排序",width = 20)
    private Integer sort;
}
