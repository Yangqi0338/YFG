package com.base.sbc.module.report.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MaterialSupplierQuoteVo {

    private String imageUrl;
    /**
     * 材料
     */
    private String materialCodeName;
    /**
     * 创建
     */
    private String createDate;
    /**
     * 一级分类
     */
    private String category1Name;
    /**
     * 二级分类
     */
    private String category2Name;
    /**
     * 三级分类
     */
    private String category3Name;
    /**
     * 厂家有效门幅/规格
     */
    private String widthName;
    /**
     * 单位
     */
    private String stockUnitName;
    /**
     * 厂家成分
     */
    private String factoryComposition;
    /**
     * 材料成分
     */
    private String ingredient;
    /**
     * 颜色规格
     */
    private String colorNameCode;
    /**
     * 供应商色号
     */
    private String supplierColorCode;
    /**
     * 供应商
     */
    private String supplierName;
    /**
     * 供应商物料号
     */
    private String supplierMaterialCode;
    /**
     * 采购报价
     */
    private String quotationPrice;
    /**
     * 订货周期（天）
     */
    private String productionDay;
    /**
     * 生产周期（天）
     */
    private String orderDay;
    /**
     * 状态
     */
    private String status;
    /**
     * FOB
     */
    private String fob;
    /**
     * 产品供应商
     */
    private String supplierNameShort;
    /**
     * 起订量
     */
    private String minimumOrderQuantity;
    /**
     * 每色起订量
     */
    private String minimumOrderQuantityColor;
    /**
     * 意见
     */
    private String remarks;
    /**
     * 供应商报价
     */
    private String supplierQuote;
    /**
     * 供应商编码
     */
    private String supplierId;
    /**
     * 克重(g/m2)
     */
    private String gramWeight;
    /**
     * 旧料号
     */
    private String materialCodeOld;
    /**
     * 发送状态
     */
    private String distribute;
    /**
     * 材料编码
     */
    private String materialCode;
    /**
     * 年份
     */
    private String yearName;
    /**
     * 季节
     */
    private String seasonName;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;

}
