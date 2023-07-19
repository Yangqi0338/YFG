package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("基础资料-规格组/门幅组导入 SpecificationGroupExcelDto")
public class SpecificationGroupExcelDto {




    @ApiModelProperty(value = "编码")
    @Excel(name = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    @Excel(name = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    @Excel(name = "类型")
    private String type;

    @ApiModelProperty(value = "使用范围")
    @Excel(name = "使用范围")
    private String applyRange;

    @ApiModelProperty(value = "规格id集合")
    private String specificationIds;

    @ApiModelProperty(value = "规格名称")
    @Excel(name = "规格名称")
    private String specificationNames;

    @ApiModelProperty(value = "描述")
    @Excel(name = "描述")
    private String remarks;

    @ApiModelProperty(value = "状态")
    @Excel(name = "状态",replace = {"true_1", "false_0"})
    private String status;


}
