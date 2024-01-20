package com.base.sbc.module.orderbook.dto;

import com.base.sbc.config.common.base.Page;
import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-15 17:25:30
 * @mail 247967116@qq.com
 */
@Data
public class OrderBookFollowUpQueryDto extends Page {
    private String id;
    private String[] productionDate;
    private String seasonId;
}
