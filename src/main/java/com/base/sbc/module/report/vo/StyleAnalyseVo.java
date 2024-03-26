package com.base.sbc.module.report.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class StyleAnalyseVo {

    private String id;

    private String styleColorId;

    /**
     * 动态字段
     */
    private Map<String, String> dynamicColumn;

    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 设计款图
     */
    private String stylePic;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 年份
     */
    private String year;
    /**
     * 年份名称
     */
    private String yearName;
    /**
     * 季节
     */
    private String season;
    /**
     * 季节名称
     */
    private String seasonName;
    /**
     * 月份
     */
    private String month;
    /**
     * 月份名称
     */
    private String monthName;
    /**
     * 波段
     */
    private String bandCode;
    /**
     * 波段名称
     */
    private String bandName;
    /**
     * 款式定位
     */
    private String positioning;
    /**
     * 款式定位名称
     */
    private String positioningName;
    /**
     * 款式风格
     */
    private String styleFlavour;
    /**
     * 款式风格名称
     */
    private String styleFlavourName;
    /**
     * 号型类型
     */
    private String sizeRange;
    /**
     * 号型类型名称
     */
    private String sizeRangeName;
    /**
     * 设计师
     */
    private String designer;
    /**
     * 设计师
     */
    private String designerId;
    /**
     * 设计工艺员名称
     */
    private String technicianName;
    /**
     * 设计工艺员
     */
    private String technicianId;
    /**
     * 材料专员
     */
    private String fabDevelopeId;
    /**
     * 材料专员名称
     */
    private String fabDevelopeName;
    /**
     * 版师
     */
    private String patternDesignId;
    /**
     * 版师名称
     */
    private String patternDesignName;
    /**
     * 打版难度名称
     */
    private String patDiffName;
    /**
     * 打版难度
     */
    private String patDiff;
    /**
     * 款式单位
     */
    private String styleUnit;
    /**
     * 款式单位
     */
    private String styleUnitCode;
    /**
     * 款式类型
     */
    private String styleType;
    /**
     * 款式类型名称
     */
    private String styleTypeName;
    /**
     * 大类
     */
    private String prodCategory1st;
    /**
     * 大类名称
     */
    private String prodCategory1stName;
    /**
     * 品类
     */
    private String prodCategory;
    /**
     * 品类名称
     */
    private String prodCategoryName;
    /**
     * 中类
     */
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    private String prodCategory2ndName;
    /**
     * 小类
     */
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    private String prodCategory3rdName;
    /**
     * 开发分类
     */
    private String devClass;
    /**
     * 开发分类名称
     */
    private String devClassName;
    /**
     * 款式来源
     */
    private String styleOrigin;
    /**
     * 款式来源名称
     */
    private String styleOriginName;
    /**
     * 生产类型
     */
    private String devtType;
    /**
     * 生产类型名称
     */
    private String devtTypeName;
    /**
     * 套版款号
     */
    private String registeringNo;
    /**
     * 销售渠道
     */
    private String salesType;
    /**
     * 销售渠道名称
     */
    private String salesTypeName;
    /**
     * 大货波段编码
     */
    private String styleBandCode;
    /**
     * 大货波段名称
     */
    private String styleBandName;
    /**
     * 大货款号
     */
    private String styleNo;
    /**
     * 大货款图
     */
    private String styleColorPic;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 列头筛选数量
     */
    private Integer groupCount;
    /**
     * 动态字段值
     */
    private String valName;

}
