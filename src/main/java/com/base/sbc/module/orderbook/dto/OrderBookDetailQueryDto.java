package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderBookDetailQueryDto extends Page {

    /**
     * 当前用户登录id
     */
    private String userId;

    private String id;

    /** 序号 */
    private String serialNumber;
    /** 订货本id */
    private String orderBookId;
    /** 投产日期 */
    private String commissioningDate;
    /** 研发类型 */
    private String researchType;
    /** 颜色id */
    private String colourLibraryId;

    /**
     * 生产类型
     */
    private String devtTypeName;

    /**
     * 大货款号
     */
    private String bulkStyleNo;
    @ApiModelProperty(value = "品类")
    private String categoryCode;

    private String designerId;

    /** 波段编码 */
    @ApiModelProperty(value = "波段编码"  )
    private String band;
    /** 状态 */
    private String status;

    /** 生产紧急程度 */
    private String productionUrgency;

    private String companyCode;

    private String imgFlag;


    @ApiModelProperty(value = "产品季id"  )
    private String  planningSeasonId;
}
