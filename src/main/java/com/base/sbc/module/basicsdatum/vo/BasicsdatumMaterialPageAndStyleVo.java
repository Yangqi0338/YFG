package com.base.sbc.module.basicsdatum.vo;

import lombok.Data;

@Data
public class BasicsdatumMaterialPageAndStyleVo {
    /**
     * 物料图片
     */
    private String materialsImageUrl;

    /**
     * 物料颜色
     */
    private String materialsColor;

    /**
     * 物料规格
     */
    private String materialsSpec;

    /**
     * 厂家简称
     */
    private String supperSampleName;

    /**
     * 款式图
     */
    private String styleImageUrl;

    /**
     * 产品季
     */
    private String planningSeason;
    /**
     * 大类
     */
    private String prodCategory1stName;
    /**
     * 品类
     */
    private String prodCategoryName;
    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 大货款号
     */
    private String bulkNo;
    /**
     * 配色颜色
     */
    private String styleColor;

    /**
     * 物料code
     */
    private String materialsCode;

    /**
     * bom 阶段
     */
    private String bomPhase;
    /**
     * 设计款号图
     */
    private String stylePic;
    /**
     * 大货款号图
     */
    private String styleColorPic;
}
