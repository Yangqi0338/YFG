//package com.base.sbc.open.entity;
//
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//
//@Data
//@TableName("bi_colorway")
//public class BiColorway {
//    /**
//     * 主键
//     */
//    private String id;
//
//    /**
//     * 大货款号
//     */
//    private String colorwayCode;
//
//    /**
//     * 颜色规格
//     */
//    private String colorSpecification;
//
//    /**
//     * 代码
//     */
//    private String colorCode;
//
//    /**
//     * 颜色名称
//     */
//    private String colorName;
//
//    /**
//     * 波段
//     */
//    private String c8ColorwayBand;
//
//    /**
//     * 吊牌价
//     */
//    private String c8ColorwaySalesPrice;
//
//    /**
//     * 上新时间
//     */
//    private Date c8ColorwaySaleTime;
//
//    /**
//     * 产品风格
//     */
//    private String c8ColorwayStyles;
//
//    /**
//     * 版型名称
//     */
//    private String patternName;
//
//    /**
//     * 总成本
//     */
//    private BigDecimal c8ColorwayTotalCosts;
//
//    /**
//     * 实际倍率
//     */
//    private BigDecimal c8ColorwayMarckup;
//
//    /**
//     * 商品吊牌价确认
//     */
//    private Boolean c8ColorwaySpPriceConfirm;
//
//    /**
//     * 计控实际成本
//     */
//    private BigDecimal c8ColorwayJkCosts;
//
//    /**
//     * 计控实际倍率
//     */
//    private BigDecimal c8ColorwayMarckup4Pc;
//
//    /**
//     * 车缝加工费
//     */
//    private BigDecimal c8ColorwayLaborCosts;
//
//    /**
//     * 材料成本
//     */
//    private BigDecimal c8ColorwayMaterialCost;
//
//    /**
//     * 品名*
//     */
//    private String c8AppbomProductName;
//
//    /**
//     * 系列
//     */
//    private String c8ColorwaySeries;
//
//    /**
//     * 唯一码
//     */
//    private String c8ColorwayWareCode;
//
//    /**
//     * 启用
//     */
//    private String colorwayActive;
//
//    /**
//     * BOM阶段
//     */
//    private String c8ColorwayBomPhase;
//
//    /**
//     * 发送状态
//     */
//    private String c8SyncSendStatus;
//
//    /**
//     * 含外辅工艺
//     */
//    private String c8ColorwayIsOutsource;
//
//    /**
//     * 包含图片
//     */
//    private String c8ColorwayBimages;
//
//    /**
//     * 6.5倍率
//     */
//    private String c8ColorwayFixedRatio;
//
//    /**
//     * 包装费
//     */
//    private String c8ColorwayPackageCost;
//
//    /**
//     * 倍价
//     */
//    private String c8ColorwayFixedTagPrice;
//
//    /**
//     * 产品细分
//     */
//    private String c8ColorwayProdSeg;
//
//    /**
//     * 充绒量
//     */
//    private String c8ColorwayFabricQuantity;
//
//    /**
//     * 除毛领成本
//     */
//    private String c8ColorwayTotalCostWOCollar;
//
//    /**
//     * 次品编号
//     */
//    private String c8ColorwayCpNum;
//
//    /**
//     * 检测费
//     */
//    private String c8ColorwayInspectionFee;
//
//    /**
//     * 计控成本确认
//     */
//    private String c8ColorwayJkCostConfirm;
//
//    /**
//     * 计控吊牌价确认
//     */
//    private String c8ColorwaySalesApproved;
//
//    /**
//     * 开发类型
//     */
//    private String developmentType;
//
//    /**
//     * 毛领成本
//     */
//    private String c8ColorwayCollarCost;
//
//    /**
//     * 毛纱加工费
//     */
//    private String c8ColorwayMaoshaCosts;
//
//    /**
//     * 模式
//     */
//    private String developmentMode;
//
//    /**
//     * 默认条形码
//     */
//    private String c8ColorwayDefaultBarcode;
//
//    /**
//     * 配饰款号
//     */
//    private String c8ColorwayCollectAccCodes;
//
//    /**
//     * 轻奢款
//     */
//    private String c8ColorwayIfQ;
//
//    /**
//     * 去毛领倍率
//     */
//    private String c8ColorwayMarckupWOCollar;
//
//    /**
//     * 上新价
//     */
//    private String c8ColorwayFinalSalesPrice;
//
//    /**
//     * 设计下明细单
//     */
//    private String c8ColorwayDetailedListTs;
//
//    /**
//     * 设计下正确样
//     */
//    private String c8ColorwayCorrectSampleTs;
//
//    /**
//     * 是否上会
//     */
//    private String c8ColorwayIfOrderMeeting;
//
//    /**
//     * 是否是"内配饰"
//     */
//    private String c8ColorwayAccessories;
//
//    /**
//     * 外协加工费
//     */
//    private String c8ColorwaySubcontractCosts;
//
//    /**
//     * 编码方式结束
//     */
//    private String c8ColorwayCodeWayEnd;
//
//    /**
//     * 下单量
//     */
//    private String c8ColorwayOrderQty;
//
//    /**
//     * 下里布单
//     */
//    private String c8ColorwayInterMatTs;
//
//    /**
//     * 下配料1
//     */
//    private String c8ColorwaySubMat1Ts;
//
//    /**
//     * 下配料2
//     */
//    private String c8ColorwaySubMat2Ts;
//
//    /**
//     * 下主面料单
//     */
//    private String c8ColorwayMainMatts;
//
//    /**
//     * 销售类型
//     */
//    private String c8ColorwaySaleType;
//
//    /**
//     * 预计销售价
//     */
//    private String c8ColorwayTargetPrice;
//
//    /**
//     * 折扣率
//     */
//    private String c8ColorwayDiscountRate;
//
//    /**
//     * 主款款号
//     */
//    private String c8ColorwayMainStylesCodes;
//
//    /**
//     * BOM发送状态
//     */
//    private String c8ColorwayBomSendStatus;
//
//    /**
//     * 备注
//     */
//    private String c8ColorwayInactivedCause;
//
//    /**
//     * 厂家
//     */
//    private String c8ColorwaySupplier;
//
//    /**
//     * 厂家款号
//     */
//    private String c8ColorwaySupplierArticle;
//
//    /**
//     * 厂家款颜色
//     */
//    private String c8ColorwaySupplierArticleColor;
//
//    /**
//     * Colorway URL
//     */
//    private String c8ColorwayPlmid;
//
//    /**
//     * Style URL
//     */
//    private String c8StylePlmid;
//
//    /**
//     * 是否主推
//     */
//    private String c8ColorwayIsFeaturedPro;
//
//    /**
//     * 包含于
//     */
//    private String containedBy;
//
//    /**
//     * 创建时间
//     */
//    private String createdAt;
//
//    /**
//     * 创建人
//     */
//    private String createdBy;
//
//    /**
//     * 修改时间
//     */
//    private String modifiedAt;
//
//    /**
//     * 修改者
//     */
//    private String modifiedBy;
//
//    /**
//     * 后技术工艺师
//     */
//    private String c8AppbomProTechnician;
//
//    /**
//     * 后技术下单员
//     */
//    private String c8AppbomOrdePersonnel;
//
//    /**
//     * 下单时间
//     */
//    private Date c8AppbomOrderTime;
//}
