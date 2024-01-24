package com.base.sbc.module.orderbook.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.enums.business.orderBook.OrderBookOrderStatusEnum;
import com.base.sbc.config.enums.business.orderBook.OrderBookStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/12/5 13:30:55
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_order_book")
public class OrderBook extends BaseDataEntity<String> {
    /**
     * 订货本名称
     */
    @ApiModelProperty(value = "订货本名称")
    private String name;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String seasonId;
    /**
     * 产品季节名称
     */
    @ApiModelProperty(value = "产品季节名称")
    private String seasonName;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channel;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "状态:0:未提交,1:待确认,2:已确认,3:已下单,4:已驳回")
    private OrderBookStatusEnum status;

    @ApiModelProperty(value = "下单状态:0:未下单,1:部分下单,2:已下单")
    private OrderBookOrderStatusEnum orderStatus;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private String serialNumber;
}
