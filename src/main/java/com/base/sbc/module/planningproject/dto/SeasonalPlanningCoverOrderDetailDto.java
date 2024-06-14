package com.base.sbc.module.planningproject.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SeasonalPlanningCoverOrderDetailDto {

    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName ;
    @ApiModelProperty(value = "大类编码")
    private String prodCategory1stCode;
    @ApiModelProperty(value = "品类编码")
    private String prodCategoryCode;
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;

    @ApiModelProperty(name = "中类名称"  )
    private String prodCategory2ndName;
    @ApiModelProperty(name = "中类编码"  )
    private String prodCategory2ndCode;

    @ApiModelProperty(value = "波段名称")
    private String bandName;
    @ApiModelProperty(value = "波段编码")
    private String bandCode;
    @ApiModelProperty(value = "SKC数量")
    private String skcCount;
    @ApiModelProperty(value = "样式类别")
    private String styleCategory;
    @ApiModelProperty(value = "样式类别")
    private String styleCategoryCode;

}
