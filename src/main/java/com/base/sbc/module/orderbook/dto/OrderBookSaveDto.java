package com.base.sbc.module.orderbook.dto;

import com.base.sbc.module.orderbook.entity.OrderBook;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/12/5 14:07:33
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookSaveDto extends OrderBook {

    @ApiModelProperty(value = "年")
    private String year;

    @ApiModelProperty(value = "季节")
    private String      season;

    @ApiModelProperty(value = "品牌")
    private String   brand;

}
