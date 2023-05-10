package com.base.sbc.module.smp.entity;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/10 10:36:02
 * @mail 247967116@qq.com
 * 报价
 */
@Data
public class SmpQuot {
    /** 厂家有效门幅/规格 */
    private String supplierSize;
    /** 尺码URL */
    private String sizeURL;
    /** 材料颜色ID */
    private String supplierColorId;
    /** 材料颜色名称 */
    private String supplierColorName;
    /** 订货周期（天）*/
    private String orderGoodsDay;
    /** 生产周期（天） */
    private String productionDay;
    /** 起订量 */
    private String MOQInitial;
    /** 采购报价 */
    private String FOBFullPrice;
    /** 供应商物料号 */
    private String SupplierMaterial;
    /** 供应商编码*/
    private String supplierCode;
    /** 供应商名称 */
    private String supplierName;
    /** 意见 */
    private String comment;
    /** 结算方式 */
    private String tradeTermKey;
    /** 默认报价标识 */
    private String defaultQuote;
    /** 结算方式名称 */
    private String tradeTermName;
    /** 单位 */
    private String materialUOM;
}
