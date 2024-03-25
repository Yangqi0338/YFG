package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailAuditStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookDetailStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookOrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/12/5 13:47:13
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookQueryDto extends Page {
    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String  seasonId;
    private String name;
    private OrderBookOrderStatusEnum orderStatus;
    private OrderBookDetailAuditStatusEnum detailStatus;
}
