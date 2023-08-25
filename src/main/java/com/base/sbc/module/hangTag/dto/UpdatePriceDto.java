package com.base.sbc.module.hangTag.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 卞康
 * @date 2023/8/25 13:42:37
 * @mail 247967116@qq.com
 */
@Data
public class UpdatePriceDto {
    /**
     * 大货款号
     */
    private String bulkStyleNo;
    /**
     * 吊牌价
     */
    private BigDecimal tagPrice;
}
