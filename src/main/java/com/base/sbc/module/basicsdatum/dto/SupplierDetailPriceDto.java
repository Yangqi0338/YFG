package com.base.sbc.module.basicsdatum.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 获取供应商详情报价
 */
@Data
public class SupplierDetailPriceDto {

    @ApiModelProperty(name = "物料编号",  required = true, dataType = "String")
    @NotNull(message = "物料编号不能为空")
    private String materialCode;

    @ApiModelProperty(name = "供应商id",   required = true, dataType = "String")
    @NotNull(message = "供应商id不能为空")
    private String supplierId;

    @ApiModelProperty(name = "颜色",  required = true, dataType = "String")
    private String color;

    @ApiModelProperty(name = "规格",  required = true, dataType = "String")
    private String width;
}
