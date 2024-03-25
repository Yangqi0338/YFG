package com.base.sbc.module.report.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 吊牌充绒量报表（吊牌相关报表可扩张）
 */
@NoArgsConstructor
@Data
public class HangTagReportVo {
    /**
     * 大货款号
     */
    private String bulkStyleNo;
    /**
     * 图片
     */
    private String styleColorPic;
    /**
     * 唯一码
     */
    private String wareCode;
    /**
     * 颜色名称
     */
    private String colorName;
    /**
     * 颜色编码
     */
    private String colorCode;
    /**
     * 默认条码
     */
    private String defaultBarCode;
    /**
     * 吊牌价
     */
    private String tagPrice;
    /**
     * 品名
     */
    private String productName;
    /**
     * 款式名称
     */
    private String styleName;
    /**
     * 生产类型
     */
    private String devtTypeName;
    /**
     * 品牌
     */
    private String brandName;
    /**
     * 年份
     */
    private String yearName;
    /**
     * 季节
     */
    private String seasonName;
    /**
     * 波段
     */
    private String bandName;
    /**
     * 大类
     */
    private String prodCategory1stName;
    /**
     * 中类
     */
    private String prodCategory2ndName;
    /**
     * 小类
     */
    private String prodCategory3rdName;
    /**
     * 充绒量
     */
    private String downContent;
    /**
     * 成分信息
     */
    private String ingredient;
    /**
     * 特殊规格
     */
    private String specialSpec;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;
}
