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


    @ApiModelProperty(value = "搭配编码")
    @NotBlank(message = "搭配不能为空")
    private String collocationCode;

    @ApiModelProperty(value = "搭配名称")
    @NotBlank(message = "搭配不能为空")
    private String collocationName;


    @ApiModelProperty(value = "材料")
    @NotBlank(message = "材料不能为空")
    private String materialCodeName;

    @ApiModelProperty(value = "供应商报价")
    @NotNull(message = "供应商报价不能为空")
    private BigDecimal supplierPrice;

    @ApiModelProperty(value = "供应商物料号")
    @NotBlank(message = "供应商物料号不能为空")
    private String supplierMaterialCode;


    @ApiModelProperty(value = "单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "采购单位")
    @NotBlank(message = "采购单位不能为空")
    private String purchaseUnitCode;

    @ApiModelProperty(value = "库存单位")
    @NotBlank(message = "库存单位不能为空")
    private String stockUnitCode;


    @ApiModelProperty(value = "颜色名称")
    @NotBlank(message = "颜色不能为空")
    private String color;

    @ApiModelProperty(value = "单件用量")
    @NotNull(message = "单件用量不能为空")
    private BigDecimal unitUse;

}
