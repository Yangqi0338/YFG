package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderBookDetailProductionDto {

    /**
     * 当前用户登录id
     */
    private String userId;

    @ApiModelProperty()
    private String id;

    private String prodCategory1st;

    /**
     * 序号
     */
    private String serialNumber;
    /**
     * 订货本id
     */
    private String orderBookId;
    /**
     * 投产日期
     */
    private String commissioningDate;
    /**
     * 研发类型
     */
    private String researchType;
    /**
     * 颜色id
     */
    private String colourLibraryId;

    /**
     * 生产类型
     */
    private String devtTypeName;

    /**
     * 设计款号
     */
    private String designNo;

    /**
     * 大货款号
     */
    private String bulkStyleNo;
    /**
     * 大货款号 非模糊
     */
    private String bulkStyleNoFull;
    @ApiModelProperty(value = "品类")
    private String categoryCode;
    /**
     * 大类
     */
    @ApiModelProperty(value = "大类")
    private String category1stCode;
    /** 中类 */
    @ApiModelProperty(value = "中类"  )
    private String category2ndCode;

    private String designerName;

    /**
     * 波段名称
     */
    @ApiModelProperty(value = "波段名称")
    private String band;
    /**
     * 波段编码
     */
    @ApiModelProperty(value = "波段编码")
    private String bandCode;
    /**
     * 状态
     */
    private String status;
    private OrderBookDetailAuditStatusEnum auditStatus;

    private String isOrder;

    /**
     * 生产紧急程度
     */
    private String productionUrgency;

    private String companyCode;

    private String imgFlag;
    private String yearName;


    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /**
     * 是否有权限查询全部 0否，1是
     */
    private String isAll;

    private String ids;

    private String brandCode;
    private String positioningName;
    private String patternPositioningName;
    private String colorName;
    private String supplierColor;
    private String tagPrice;
    private String totalProduction;
    private String coefficientCode;
    private String cost;
    private String targetTime;
    private String productionUrgencyName;
    private String remark;
    private String other;
    private String fabricState;
    private String continuationPoint;
    private String companyFabricNumber;
    private String fabricFactoryCode;
    private String fabricFactoryName;
    private String fabricComposition;
    private String fabricFactoryColorNumber;
    private String fabricType;
    private String unitPrice;
    private String deliveryTime;
    private String inventoryFabricMeter;
    private String inventoryDoableNumber;
    private String fabricRemarks;
    private String fabricDrop;
    private String style;
    private String prodCategory2ndName;
    private String prodCategory2ndCode;
    private String oldDesignNo;
    private String registeringNo;
    private String suitNo;
    private String patternDesignName;
    private String styleDesignerName;
    private String offlineProduction;
    private String onlineProduction;
    private String material;
    private String braiding;
    private String dimensionInfo;
    private String gramWeight;

    private List<OrderBookChannelType> channel;
}
