package com.base.sbc.module.sample.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FabricSummaryExportExcel {

    @ApiModelProperty(value = "理化检测结果（1是0否)-面料检测"  )
    public static String physicochemistryDetectionResult = "physicochemistryDetectionResult";

    @ApiModelProperty(value = "试穿结果 0不合适，1合适"  )
    public static String fittingResult = "fittingResult";

    @ApiModelProperty(value = "询价编号"  )
    public static String enquiryCode = "enquiryCode";

    @ApiModelProperty(value = "持续环保（1是 0否 空白）")
    public static String isProtection = "isProtection";

    @ApiModelProperty(value = "物料编号"  )
    public static String materialCode = "materialCode";

    @ApiModelProperty(value = "年份尾缀"  )
    public static String yearSuffix = "yearSuffix";

    @ApiModelProperty(value = "默认供应商名称-供应商简称"  )
    public static String supplierName = "supplierName";

    @ApiModelProperty(value = "供应商物料编号-供应商物料号"  )
    public static String supplierFabricCode = "supplierFabricCode";

    @ApiModelProperty(value = "供应商厂家成分"  )
    public static String supplierFactoryIngredient = "supplierFactoryIngredient";

    @ApiModelProperty(value = "规格名称-有效门幅"  )
    public static String widthName = "widthName";

    @ApiModelProperty(value = "面料克重"  )
    public static String gramWeight = "gramWeight";

    @ApiModelProperty(value = "纱支规格"  )
    public static String specification = "specification";

    @ApiModelProperty(value = "密度"  )
    public static String density = "density";

    @ApiModelProperty(value = "起订量"  )
    public static String minimumOrderQuantity = "minimumOrderQuantity";

    @ApiModelProperty(value = "生产周期-期货"  )
    public static String productionDay = "productionDay";

    @ApiModelProperty(value = "供应商报价-含税价格"  )
    public static String supplierQuotationPrice = "supplierQuotationPrice";

    @ApiModelProperty(value = "制单人"  )
    public static String createName = "createName";

    //=========================================================================

    @ApiModelProperty(value = "款式图"  )
    public static String styleColorPic = "styleColorPic";

    @ApiModelProperty(value = "部位名称"  )
    public static String partName = "partName";

    @ApiModelProperty(value = "波段名称"  )
    public static String bandName = "bandName";

    @ApiModelProperty(value = "款式(大货款号)"  )
    public static String styleNo = "styleNo";

    @ApiModelProperty(value = "款式设计id（款式表）"  )
    public static String styleId = "styleId";

    @ApiModelProperty(value = "下稿设计师名称---版师"  )
    public static String senderDesignerName = "senderDesignerName";

    @ApiModelProperty(value = "是否撞色,0否 1是"  )
    public static String colorCrash = "colorCrash";

    @ApiModelProperty(value = "颜色名称"  )
    public static String materialColor = "materialColor";

    @ApiModelProperty(value = "供应商颜色"  )
    public static String supplierColor = "supplierColor";

    @ApiModelProperty(value = "供应商色号"  )
    public static String supplierColorNo = "supplierColorNo";

    @ApiModelProperty(value = "单件用量"  )
    public static String unitUse = "unitUse";

    @ApiModelProperty(value = "总投产"  )
    public static String totalProduction = "totalProduction";

}
