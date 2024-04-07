package com.base.sbc.module.sample.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@ApiModel("BOM使用面料")
@AllArgsConstructor
@NoArgsConstructor
public class BomFabricDto  extends Page {

    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    @ApiModelProperty(value = "物料编号列表")
    private List<String> materialCodes;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String categoryId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料编号")
    private String materialCodeName;

    @ApiModelProperty(value = "供应商料号")
    private String supplierMaterialCode;


}
