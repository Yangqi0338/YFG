package com.base.sbc.module.orderbook.dto;

import lombok.Data;

@Data
public class QueryOrderDetailDTO {
    // 产品季 id
    private String seasonId;

    // 渠道
    private String channel;

    // 款式类型
    private String styleCategory;
}
