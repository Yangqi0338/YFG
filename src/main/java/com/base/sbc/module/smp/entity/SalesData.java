package com.base.sbc.module.smp.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 合并款号相关操作返回值
 *
 * @author XHTE
 * @create 2024/8/1
 */
@Data
public class SalesData {

    /**
     * 大货款号
     */
    private String goodsNo;

    /**
     * 合并款号
     */
    private String mergeGoodsNo;

    /**
     * 销售量
     */
    private BigDecimal salesNum;

    /**
     * 投产量
     */
    private BigDecimal productionNum;

}
