package com.base.sbc.module.smp.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 卞康
 * @date 2023/7/13 14:35:46
 * @mail 247967116@qq.com
 * bom材料清单
 */
@Data
public class BomMaterial {

    /**材料编号*/
    private String code;

    /**材料色号*/
    private String matColorCode;

    /**材料单位*/
    private String materialUom;

    /**bom行项目id*/
    private String bomId;

    /**部位*/
    private String position;

    /**损耗率*/
    private BigDecimal costRate;

    /**是否启用*/
    private Boolean active;

    /**搭配名称*/
    private String placementName;

}
