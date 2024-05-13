package com.base.sbc.open.vo;

import lombok.Data;

@Data
public class OrderBookDetailDataVo {

    /**
     * 序号
     */
    private String serialNumber;
    /**
     * 年份
     */
    private String year;
    /**
     * 季节名称
     */
    private String seasonName;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 大货款号
     */
    private String bulkStyleNo;

    /**
     * 设计款号
     */
    private String style;

    /**
     * 波段名称
     */
    private String bandName;
    /**
     * 波段编码
     */
    private String bandCode;

    /**
     * 颜色编码
     */
    private String colorCode;
    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 品类名称
     */
    private String  categoryName;
    /**
     * 品类编码
     */
    private String  categoryCode;

    /**
     * 状态:0：未提交，1：分配设计师，2：分配商企，3：待审核,4:审核通过,5:审核未通
     */
    private String status;
}
