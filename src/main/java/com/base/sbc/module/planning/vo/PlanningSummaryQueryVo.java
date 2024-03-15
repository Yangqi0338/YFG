package com.base.sbc.module.planning.vo;

import lombok.Data;

/**
 * @author 卞康
 * @date 2024-01-25 11:28:04
 * @mail 247967116@qq.com
 */
@Data
public class PlanningSummaryQueryVo {
    /**
     * 维度名称
     */
    private String dimensionName;
    /**
     * 波段名称
     */
    private String bandName;
    /**
     * 需求数量
     */
    private String demandNumber;
    /**
     * 需求占比
     */
    private String demandProportion;
    /**
     * 下单数量
     */
    private String orderNumber;
    /**
     * 下单占比
     */
     private String orderProportion;
    /**
     * 需求缺口(已开发)
     */
    private String demandGap;
    /**
     * 坑位缺口(下单)
     */
    private String seatGap;
}
