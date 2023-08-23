package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackBomEmptyCheckDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-14 19:59
 */
@Data
@ApiModel("资料包-物料清单非空校验dto PackBomEmptyCheckDto")
public class PackBomEmptyCheckDto {


    @NotBlank(message = "搭配")
    private String collocationCode;

    @ApiModelProperty(value = "搭配名称")
    @NotBlank(message = "搭配")
    private String collocationName;


    @ApiModelProperty(value = "材料")
    @NotBlank(message = "材料")
    private String materialCodeName;

    @ApiModelProperty(value = "供应商报价")
    @NotNull(message = "供应商报价")
    private BigDecimal supplierPrice;

    @ApiModelProperty(value = "供应商物料号")
    @NotBlank(message = "供应商物料号")
    private String supplierMaterialCode;


    @ApiModelProperty(value = "单价")
    @NotNull(message = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "库存单位")
    @NotBlank(message = "库存单位")
    private String stockUnitCode;


    @ApiModelProperty(value = "颜色名称")
    @NotBlank(message = "颜色")
    private String color;

    @ApiModelProperty(value = "单件用量")
    @NotNull(message = "单件用量")
    private BigDecimal unitUse;

    @ApiModelProperty(value = "成本")
    @NotNull(message = "成本")
    private BigDecimal cost;

}
