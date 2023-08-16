package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料样式设计类
 * @author lizan
 * @date 2023-07-14 14:07
 */
@Data
public class MaterialSampleDesignVO {

    @ApiModelProperty(value = "样衣-款式配色id")
    private String id;

    @ApiModelProperty(value = "样衣图片地址")
    private String url;

    @ApiModelProperty(value = "波段编码")
    private String bandCode;

    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类")
    private String categoryName;

    @ApiModelProperty(value = "款式(大货款号)")
    private String styleNo;

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "颜色")
    private String colorName;

    @ApiModelProperty(value = "颜色规格")
    private String colorSpecification;

    @ApiModelProperty(value = "销售类型")
    private String salesSype;

    @ApiModelProperty(value = "是否是内饰款(0否,1:是)")
    private String isTrim;

    @ApiModelProperty(value = "主款")
    private String principalStyle;

    @ApiModelProperty(value = "品类")
    private String prodCategoryName;

    @ApiModelProperty(value = "中类")
    private String prodCategory2ndName;

    @ApiModelProperty(value = "小类")
    private String prodCategory3ndName;

    @ApiModelProperty(value = "部位名称")
    private String partName;

    @ApiModelProperty(value = "损耗")
    private String lossRate;

    @ApiModelProperty(value = "物料颜色")
    private String color;

    @ApiModelProperty(value = "单件用量")
    private String unitUse;

    @ApiModelProperty(value = "投产数量")
    private String productionQuantity;

    @ApiModelProperty(value = "采购单位")
    private String purchaseUnitName;

    @ApiModelProperty(value = "需求米数")
    private String requiredMeters;

    @ApiModelProperty(value = "供应商色号")
    private String supplierColorNo;

    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;

    @ApiModelProperty(value = "材料成分")
    private String  ingredient;

    @ApiModelProperty(value = "备注")
    private String  remarks;


}
