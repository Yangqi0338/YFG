package com.base.sbc.module.smp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TagPrinting {
    /**
     * 唯一码
     */
    private String C8_Colorway_WareCode;

    /**
     * 是否赠品
     */
    private Boolean IsGift;

    /**
     * 颜色名称
     */
    private String ColorDescription;

    /**
     * 颜色编码
     */
    private String ColorCode;

    /**
     * 大货款号
     */
    private String StyleCode;

    /**
     * 批次
     */
    private String C8_Colorway_BatchNo;

    /**
     * 商品吊牌价确认
     */
    private Boolean MerchApproved;

    /**
     * 配饰款号
     */
    private String SecCode;

    /**
     * 主款款号
     */
    private String MainCode;

    /**
     * 吊牌价
     */
    private BigDecimal C8_Colorway_SalesPrice;

    /**
     * 是否内配饰
     */
    private Boolean IsAccessories;

    /**
     * 系列
     */
    private String C8_Colorway_Series;

    /**
     * 大货款号是否激活
     */
    private Boolean Active;

    /**
     * 销售类型
     */
    private String C8_Colorway_SaleType;

    /**
     * 品牌_描述
     */
    private String C8_Season_Brand;

    /**
     * 品类id
     */
    private String C8_Collection_ProdCategory;

    /**
     * 主题
     */
    private String Theme;

    /**
     * 小类编码
     */
    private String C8_Style_3rdCategory;

    /**
     * 中类编码
     */
    private String C8_Style_2ndCategory;

    /**
     * 尺码号型编码
     */
    private String SizeRangeCode;

    /**
     * 尺码号型名称
     */
    private String SizeRangeName;

    /**
     * 款式分类
     */
    private String ProductType;

    /**
     * 大类
     */
    private String C8_1stProdCategory;

    /**
     * 尺码号型:分类
     */
    private String SizeRangeDimensionType;

    /**
     * 成分
     */
    private String Composition;

    /**
     * 洗标
     */
    private String CareSymbols;

    /**
     * 质量等级
     */
    private String QualityClass;

    /**
     * 品名
     */
    private String ProductName;

    /**
     * 安全类别
     */
    private String SaftyType;

    /**
     * 执行标准
     */
    private String OPStandard;

    /**
     * 品控部确认
     */
    private Boolean Approved;

    /**
     * 温馨提示
     */
    private String Attention;

    /**
     * 后技术确认
     */
    private Boolean TechApproved;

    /**
     * 安全标题
     */
    private String SaftyTitle;

    /**
     * 洗唛材质备注
     */
    private String C8_APPBOM_Comment;

    /**
     * 贮藏要求
     */
    private String C8_APPBOM_StorageReq;

    /**
     * 产地
     */
    private String C8_APPBOM_MadeIn;

    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date c8_APPBOM_StorageTime;

    /**
     * 英文成分
     */
    private String CompsitionMix;


    /**
     * 英文温馨提示
     */
    private String WarmPointEN;


    /**
     * 英文贮藏要求
     */
    private String StorageReqEN;



    /**
     * 款式尺码明细
     */
    private List<Size> size;

    /**
     * 物料名称（多个使用,分割）
     */
    private String materialCodeNames;

    /**
     * 尺码明细类
     */
    @Data
    public static class Size {
        /**
         * 尺码
         */
        private String SIZECODE;

        /**
         * 代码
         */
        private String SORTCODE;

        /**
         * 号型
         */
        private String SIZENAME;

        /**
         * 尺码编号
         */
        private String SizeID;

        /**
         * External Size
         */
        private String EXTSIZECODE;

        /**
         * 显示尺码标识
         */
        private boolean ShowIntSize;

        /**
         * 欧码
         */
        private String EuropeCode;

        /**
         * 充绒量
         */
        private String SKUFiller;

        /**
         * 特殊规格
         */
        private String SpecialSpec;

        /**
         * 合作方条形码
         */
        private String outsideBarcode;

        @JsonIgnore
        public String getSystemSizeName(){
            return this.SIZENAME + "(" + this.SIZECODE + ")";
        }
    }

}
