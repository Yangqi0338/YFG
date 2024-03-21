package com.base.sbc.module.report.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class StyleSizeReportVo {
    /**
     * 尺码ids
     */
    private String sizeIds;
    /**
     * 标准值
     */
    private String standard;
    /**
     * 品牌
     */
    private String brandName;
    /**
     * 大货款号
     */
    private String styleNo;
    /**
     *  款图
     */
    private String styleColorPic;
    /**
     * 颜色
     */
    private String colorName;
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
     * 品类
     */
    private String prodCategoryName;
    /**
     * 中类
     */
    private String prodCategory2ndName;
    /**
     * 小类
     */
    private String prodCategory3rdName;
    /**
     * 部位
     */
    private String partName;
    /**
     * 测量方法
     */
    private String method;

    /**
     * 尺寸明细数据
     */
    private Map<String,String> sizeMap;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;
}
