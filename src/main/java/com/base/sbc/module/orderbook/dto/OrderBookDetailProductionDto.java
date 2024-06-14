package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.ProductionType;
import com.base.sbc.config.enums.business.orderBook.OrderBookChannelType;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.smp.StylePutIntoType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderBookDetailProductionDto {

    /** 订货本id */
    private  List<String> orderBookDetailIdList;

    /** 商品要求货期 */
    private Date deliveryAt;

    /** 下单类型 */
    private StylePutIntoType placeOrderTypeCode;

    /** 投产类型 */
    private ProductionType devtType;

    /** 销售分类 */
    private String saleTypeId;

    /** 预算号 */
    private String budgetNo;

    /** 是否投产合并 */
    private String facMerge;

}
