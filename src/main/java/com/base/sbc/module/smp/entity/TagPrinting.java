package com.base.sbc.module.smp.entity;

import lombok.Data;

import java.util.List;

@Data
public class TagPrinting {
    /** 唯一码 */
    private String colorwayWareCode;
    /** 是否赠品 */
    private String isGift;
    /** 颜色名称 */
    private String colorDescription;
    /** 颜色编码 */
    private String colorCode;
    /** 大货款号 */
    private String styleCode;
    /** 批次 */
    private String colorwayBatchNo;
    /** 商品吊牌价确认 */
    private String merchApproved;
    /** 配饰款号 */
    private String secCode;
    /** 主款款号 */
    private String mainCode;
    /** 吊牌价 */
    private String colorwaySalesPrice;
    /** 是否内配饰 */
    private String isAccessories;
    /** 系列 */
    private String colorwaySeries;
    /** 大货款号是否激活 */
    private String active;
    /** 销售类型 */
    private String colorwaySaleType;
    /** 品牌_描述 */
    private String seasonBrand;
    /** 品类id */
    private String collectionProdCategory;
    /** 主题 */
    private String theme;
    /** 小类编码 */
    private String style3rdCategory;
    /** 中类编码 */
    private String style2ndCategory;
    /** 尺码号型编码 */
    private String sizeRangeCode;
    /** 尺码号型名称 */
    private String sizeRangeName;
    /** 款式分类 */
    private String productType;
    /** 大类 */
    private String firstProdCategory;
    /** 尺码号型:分类 */
    private String sizeRangeDimensionType;
    /** 成分 */
    private String composition;
    /** 洗标 */
    private String careSymbols;
    /** 质量登记 */
    private String qualityClass;
    /** 品名 */
    private String productName;
    /** 安全类别 */
    private String saftyType;
    /** 执行标准 */
    private String opStandard;
    /** 品控部确认 */
    private String approved;
    /** 温馨提示 */
    private String attention;
    /** 后技术确认 */
    private String techApproved;
    /** 安全技术类别 */
    private String saftyTitle;
    /** 洗唛材质备注 */
    private String appBomComment;
    /** 系列描述 */
    private String seriesDesc;
    /** 前片色 */
    private String frontFabricColor;
    /** 后片色 */
    private String backFabricColor;
    /** 毛边 */
    private String appHem;
    /** 备注 */
    private String appRemark;
    /** 代码规则名称 */
    private String colorwayRuleName;
    /** 代码规则值 */
    private String colorwayRuleValue;
    /** 染色材质 */
    private String dyeingComposition;
    /** 技术类别 */
    private String techType;
    /** 安全类别 */
    private String saftyClass;
    /** 季节 */
    private String season;
    /** 是否主料 */
    private String isMainMaterial;
    /** 配件颜色编码 */
    private String accessoriesColorCode;
    /** 配件颜色名称 */
    private String accessoriesColorDescription;
    /** 款式编码 */
    private String styleCodeEN;
    /** 码 */
    private String sizeIDEN;
    /** 促销类型 */
    private String promoType;
    /** 促销描述 */
    private String promoDesc;
    /** 是否首次购买 */
    private String isFirstBuy;
    /** 是否样品 */
    private String isSample;
    /** 样品编码 */
    private String sampleCode;
    /** 是否样卡 */
    private String isSampleCard;
    /** 样卡编码 */
    private String sampleCardCode;
    /** 样卡等级 */
    private String sampleCardGrade;
    /** 尺码信息 */
    private List<Size> sizeInfo;

    /**
     * 尺码信息
     */
    @Data
    public static class Size {
        /** 尺码 */
        private String sizeCode;
        /** 代码 */
        private String sortCode;
        /** 号型 */
        private String sizeName;
        /** 尺码编号 */
        private String sizeID;
        /** External Size */
        private String extSizeCode;
        /** 显示尺码标识 */
        private String showIntSize;
        /** 欧码 */
        private String europeCode;
        /** 充绒量 */
        private String skuFiller;
        /** 特殊规格 */
        private String specialSpec;
    }
}
