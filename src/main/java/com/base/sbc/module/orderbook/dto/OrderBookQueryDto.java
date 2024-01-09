package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.common.base.Page;
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
}
