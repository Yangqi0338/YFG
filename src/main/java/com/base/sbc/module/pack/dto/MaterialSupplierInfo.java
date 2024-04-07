package com.base.sbc.module.pack.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialSupplierInfo {
    @ApiModelProperty(value = "供应商简称")
    private String supplierAbbreviation;
    @ApiModelProperty(value = "供应商物料编码")
    private String supplierMaterialCode;
}
