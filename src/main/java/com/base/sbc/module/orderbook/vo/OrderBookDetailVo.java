package com.base.sbc.module.orderbook.vo;

import com.base.sbc.module.orderbook.entity.OrderBookDetail;
import com.base.sbc.module.smp.dto.ScmProductionBudgetDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderBookDetailVo extends OrderBookDetail {


    private String orderBookName;

    /** 款式定位 */
    @ApiModelProperty(value = "款式定位"  )
    private String positioning;
    /** 款式定位名称 */
    @ApiModelProperty(value = "款式定位名称"  )
    private String positioningName;

    /**
     * 款式设计师
     */
    @ApiModelProperty(value = "款式设计师")
    private String styleDesignerName;
    /**
     * 款式设计师id
     */
    @ApiModelProperty(value = "款式设计师id")
    private String styleDesignerId;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;
    /**
     * 原大货款号
     */
    @ApiModelProperty(value = "原大货款号")
    private String hisStyleNo;
    /**
     * 关联大货款名
     */
    @ApiModelProperty(value = "关联大货款名")
    private String bomName;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String stylePic;

    /**
     * 配色图
     */
    @ApiModelProperty(value = "配色图")
    private String styleColorPic;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 品牌编码
     */
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;

    /**
     * 波段名称
     */
    @ApiModelProperty(value = "波段名称")
    private String bandName;
    /**
     * 波段编码
     */
    @ApiModelProperty(value = "波段编码")
    private String bandCode;

    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String  categoryName;
    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String  categoryCode;

    /**
     * 生产类型
     */
    @ApiModelProperty(value = "生产类型")
    public String getDevtTypeName(){
        if (this.getDevtType() == null) return "";
        return this.getDevtType().getText();
    }

    /**
     * 面料类型
     */
    @ApiModelProperty(value = "面料类型")
    private String fabricCompositionType;

    /**
     * 厂家
     */
    @ApiModelProperty(value = "FOB厂家")
    private String supplierAbbreviation;

    /**
     * 厂家款号
     */
    @ApiModelProperty(value = "FOB厂家款号")
    private String supplierNo;

    /**
     * 色号
     */
    @ApiModelProperty(value = "FOB色号")
    private String supplierColor;

    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * FOB成衣厂家编码
     */
    @ApiModelProperty(value = "FOB成衣厂家编码")
    private String fobClothingFactoryCode;
    /**
     * FOB成衣厂家名称
     */
    @ApiModelProperty(value = "FOB成衣厂家名称")
    private String fobClothingFactoryName;

    /**
     * FOB成衣厂家名称
     */
    @ApiModelProperty(value = "FOB成衣厂家名称")
    private String fobSupplier;

    /**
     * 中类
     */
    @ApiModelProperty(value = "中类")
    private String prodCategory2ndName;

    /**
     * 总成本
     */
    @ApiModelProperty(value = "总成本")
    private BigDecimal cost;


    /**
     * CMT成本
     */
    @ApiModelProperty(value = "CMT成本")
    private BigDecimal cmtCost;

    /**
     * CMT总成本（含税)
     */
    @ApiModelProperty(value = "CMT总成本（含税")
    private BigDecimal cmtTotalCost;
    /** 版型定位编码 */
    @ApiModelProperty(value = "版型定位编码"  )
    private String patternPositioningCode;

    /** 版型定位名称 */
    @ApiModelProperty(value = "版型定位名称"  )
    private String patternPositioningName;
    /**
     * CMT车缝工价
     */
    @ApiModelProperty(value = "CMT车缝工价")
    private BigDecimal cmtCarpetCost;

    /**
     * FOB成本价
     */
    @ApiModelProperty(value = "FOB成本价")
    private BigDecimal fobCost;
    /**
     * 后道
     */
    @ApiModelProperty(value = "后道")
    private BigDecimal honest;

    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;

    /**
     * bom
     */
    @ApiModelProperty(value = "bom")
    private String bom;

    /**
     * bom状态
     */
    @ApiModelProperty(value = "bom状态")
    private String bomStatus;

    private String styleId;

    private String style;

    private String packInfoId;

    private String bomVersionId;

    private String packType;
    private String foreignId;


    @ApiModelProperty(value = "配色下发标记")
    private String   scmSendFlag;

    private String oldDesignNo;

    private String stylePricingId;

    private String productStyle;

    private String productStyleName;


    /**
     * 单件面料用量/米
     */
    @ApiModelProperty(value = "单件面料用量/米 " )
    private String unitFabricDosage;

    /**
     * 单件用量/里
     */
    @ApiModelProperty(value = "单件用量/里")
    private String unitDosage;

    /**
     * 版师
     */
    private String patternDesignName;

    /**
     * 套版款号
     */
    private String registeringNo;

    private String sumMaterial;

    private String sumBraiding;

    private String sumOfflineProduction;
    private String sumOnlineProduction;
    private String sumTagPrice;
    private String materialMoney;


    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /**
     * 渠道编码
     */
    @ApiModelProperty(value = "渠道编码")
    private String channel;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channelName;

    /**
     * 尺码codes
     */
    @JsonIgnore
    @ApiModelProperty(value = "尺码codes")
    private String sizeCodes;

    /**
     * 是否轻奢款
     */
    @ApiModelProperty(value = "尺码codes")
    private String high;

    private String yearName;


    private String seasonName;


    private OrderBookSimilarStyleVo similarStyle;

    /**
     * 投产日期
     */
    @ApiModelProperty(value = "投产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date commissioningDate;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date orderDate;

    /** 商品要求货期 */
    @ApiModelProperty(value = "商品要求货期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deliveryAt;

    /** 动态列 */
    private Integer groupCount;

    @ApiModelProperty(value = "线下投产占比")
    private Double offLinkSizeProportion;

    @ApiModelProperty(value = "线上投产占比")
    private Double onLinkSizeProportion;

    @ApiModelProperty(value = "预算号查询")
    private List<ScmProductionBudgetDto> productionBudgetNoList;
}
