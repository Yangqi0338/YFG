package com.base.sbc.module.orderbook.vo;

import com.base.sbc.module.orderbook.entity.OrderBook;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/12/15 15:10:34
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookVo extends OrderBook {
    /**
     * 款数
     */
    @ApiModelProperty(value = "款数")
    private Integer count = 0;

}
