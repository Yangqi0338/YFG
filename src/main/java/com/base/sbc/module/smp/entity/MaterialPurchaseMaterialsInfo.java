package com.base.sbc.module.smp.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能描述
 *
 * @author XHTE
 * @create 2024/7/22
 */
@Data
public class MaterialPurchaseMaterialsInfo {

    /**
     * 下单品牌编码
     */
    private String brandCode;
    /**
     * 物料编号
     */
    private String materialsNo;
    /**
     * 物料规格编号
     */
    private String specificationsNo;
    /**
     * 物料颜色编码
     */
    private String materialsColorCode;
    /**
     * 物料名称
     */
    private String materialsName;
    /**
     * 采购单价
     */
    private String priceUnit;

}
