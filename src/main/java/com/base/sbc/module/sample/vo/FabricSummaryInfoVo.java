package com.base.sbc.module.sample.vo;


import com.base.sbc.module.fabricsummary.entity.FabricSummary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FabricSummaryInfoVo extends FabricSummary {
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 款式设计id（款式表） */
    @ApiModelProperty(value = "款式设计id（款式表）"  )
    private String styleId;
    /** 款式配色图 */
    @ApiModelProperty(value = "款式配色图"  )
    private String styleColorPic;
    /** 配色 */
    @ApiModelProperty(value = "配色"  )
    private String colorName;
    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colorSpecification;
    /** 颜色id */
    @ApiModelProperty(value = "颜色id"  )
    private String colourLibraryId;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designNo;
    /** 关联的设计款号 */
    @ApiModelProperty(value = "关联的设计款号"  )
    private String seatDesignNo;
    /** 波段编码 */
    @ApiModelProperty(value = "波段编码"  )
    private String bandCode;
    /** 波段名称 */
    @ApiModelProperty(value = "波段名称"  )
    private String bandName;
    /** 供应商 */
    @ApiModelProperty(value = "供应商"  )
    private String supplier;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;
    /** 供应商简称 */
    @ApiModelProperty(value = "供应商简称"  )
    private String supplierAbbreviation;
    /** 供应商款号 */
    @ApiModelProperty(value = "供应商款号"  )
    private String supplierNo;
    /** 供应商颜色 */
    @ApiModelProperty(value = "供应商颜色"  )
    private String supplierColor;
    /** 坑位信息id */
    @ApiModelProperty(value = "坑位信息id"  )
    private String planningCategoryItemId;
    /** 下稿设计师 */
    @ApiModelProperty(value = "下稿设计师"  )
    private String senderDesignerId;
    /** 下稿设计师名称 */
    @ApiModelProperty(value = "下稿设计师名称"  )
    private String senderDesignerName;
    /** 是否撞色,0否 1是 */
    @ApiModelProperty(value = "是否撞色,0否 1是"  )
    private String colorCrash;
    /** 单件用量 */
    @ApiModelProperty(value = "单件用量"  )
    private BigDecimal unitUse;
    /** 总投产 */
    @ApiModelProperty(value = "总投产"  )
    private String totalProduction;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 部位编码 */
    @ApiModelProperty(value = "部位编码"  )
    private String partCode;
    /** 部位名称 */
    @ApiModelProperty(value = "部位名称"  )
    private String partName;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String materialColor;
    /** 颜色hex */
    @ApiModelProperty(value = "颜色hex"  )
    private String materialColorHex;
    /** 颜色代码 */
    @ApiModelProperty(value = "颜色代码"  )
    private String materialColorCode;
    /** 颜色图片 */
    @ApiModelProperty(value = "颜色图片"  )
    private String materialColorPic;
    /** 排序 */
    @ApiModelProperty(value = "排序"  )
    private Integer sort;
    /** 面料汇总id */
    @ApiModelProperty(value = "面料汇总id"  )
    private String fabricSummaryId;

    /** 主数据id-packbom */
    @ApiModelProperty(value = "主数据id-packbom"  )
    private String foreignId;

    /** 款式图 */
    @ApiModelProperty(value = "款式图"  )
    private String stylePic;
}
