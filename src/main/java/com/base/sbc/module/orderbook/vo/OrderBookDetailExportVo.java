package com.base.sbc.module.orderbook.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 卞康
 * @date 2023/12/8 14:14:03
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookDetailExportVo {

    /**
     * 品牌
     */
    @Excel(name = "品牌")
    private String brandName;

    /**
     * 投产日期
     */
    @Excel(name = "投产日期", format = "yyyy-MM-dd", timezone = "GMT+8")
    private Date commissioningDate;
    /**
     * 生产类型
     */
    @Excel(name = "生产类型")
    private String devtTypeName;
    /** 版型定位 */
    @Excel(name = "版型定位"  )
    private String patternPositioningName;

    /** 款式定位 */
    @Excel(name = "款式定位"  )
    private String positioning;
    /**
     * 厂家
     */
    @Excel(name = "厂家")
    private String supplierAbbreviation;

    /**
     * 厂家款号
     */
    @Excel(name = "厂家款号")
    private String supplierNo;
    /**
     * 波段名称
     */
    @Excel(name = "波段")
    private String bandName;
    /**
     * 颜色波段
     */
    @Excel(name = "颜色波段")
    private String colorBand;
    /**
     * 品类名称
     */
    @Excel(name = "品类")
    private String  categoryName;

    // @Excel(name = "款式图",type = 2,imageType = 2)
    private String stylePic;
    @Excel(name = "款式图",type = 2,imageType = 2, width = 80.0, height = 80.0)
    private byte[] stylePic1;
    /**
     * 大货款号
     */
    @Excel(name = "大货款号")
    private String bulkStyleNo;
    // @Excel(name = "配色图",type = 2,imageType = 2)
    private String styleColorPic;
    @Excel(name = "配色图",type = 2,imageType = 2, width = 80.0, height = 80.0)
    private byte[] styleColorPic1;
    @Excel(name = "颜色")
    private String colorName;
    @Excel(name = "色号")
    private String supplierColor;
    @Excel(name = "吊牌价")
    private BigDecimal tagPrice;
    @Excel(name = "总投产")
    private String totalProduction;
    @Excel(name = "倍率")
    private String rate;
    @Excel(name = "系数")
    private String productStyle;
    @Excel(name = "目标入仓日期")
    private String targetTime;
    @Excel(name = "4倍价")
    private String multiplePrice;
    @Excel(name = "生产紧急程度名称")
    private String productionUrgencyName;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "CMT车缝工价")
    private String cmtCarpetCost;
    @Excel(name = "FOB成本价")
    private String fobCost;
    @Excel(name = "单件面料用量/米")
    private String unitFabricDosage;
    @Excel(name = "单件用量/里")
    private String unitDosage;
    @Excel(name = "后道")
    private String honest;
    @Excel(name = "其他")
    private String other;
    @Excel(name = "面料状态")
    private String fabricState;
    @Excel(name = "是否延续")
    private String continuationPoint;
    /**
     * 面料吊牌
     */
    @Excel(name = "面料吊牌")
    private String fabricDrop;
    @Excel(name = "线下投产")
    private String offlineProduction;
    @Excel(name = "线上投产")
    private String onlineProduction;
    @Excel(name = "设计师名称")
    private String designerName;
    @Excel(name = "MM设计款号"  )
    private String style;
    @Excel(name = "备料")
    private String material;
    @Excel(name = "备胚")
    private String braiding;
    /**
     * 公司面料备注
     */
    @Excel(name = "公司面料备注")
    private String companyFabricRemarks;
    /**
     * 面料编号
     */
    @Excel(name = "面料编号")
    private String companyFabricNumber;
    /**
     * 面料厂家
     */
    @ApiModelProperty(value = "面料厂家")
    private String fabricFactoryName;
    @Excel(name = "面料色号")
    private String fabricFactoryColorNumber;
    @Excel(name = "面料类型")
    private String fabricType;
    @Excel(name = "价格-单价/米")
    private String unitPrice;
    @Excel(name = "货期")
    private String deliveryTime;
    @Excel(name = "库存/现货面料米数")
    private String inventoryFabricMeter;
    @Excel(name = "库存/现货可做件数")
    private String inventoryDoableNumber;
    @Excel(name = "面料备注")
    private String fabricRemarks;
    /** 中类名称 */
    @Excel(name = "中类"  )
    private String prodCategory2ndName;
    @Excel(name = "套装款号"  )
    private String suitNo;

    private int sizeXs;

    private int sizeS;
    private int sizeM;
    private int sizeL;
    private int sizeXl;

    private int total;

    public int getTotal() {
       return sizeXs+sizeS+sizeM+sizeL+sizeXl;

    }
}
