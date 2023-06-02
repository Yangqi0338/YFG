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
    private Boolean merchApproved;
    /** 配饰款号 */
    private String secCode;
    /** 主款款号 */
    private String mainCode;
    /** 吊牌价 */
    private String colorwaySalesPrice;
    /** 是否内配饰 */
    private Boolean isAccessories;
    /** 系列 */
    private String colorwaySeries;
    /** 大货款号是否激活 */
    private Boolean active;
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
    private Boolean approved;
    /** 温馨提示 */
    private String attention;
    /** 后技术确认 */
    private Boolean techApproved;
    /** 安全标题 */
    private String saftyTitle;
    /** 洗唛材质备注 */
    private String appBomComment;
    /** 贮藏要求 */
    private String appBomStorageReq;
    /** 入库时间 */
    private String appBomStorageTime;
    /** 产地 */
    private String appBomMadeIn;
    /** 英文成分 */
    private String compsitionMix;
    /** 英文温馨提示 */
    private String warmPointEn;
    /** 英文贮藏要求 */
    private String storageReqEn;
    /** 尺码信息 */
    private List<Size> size;

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
