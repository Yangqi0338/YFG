package com.base.sbc.open.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/7/20 10:29:11
 * @mail 247967116@qq.com
 */
@Data
public class SmpOpenMaterialDto {
    /**
     * 材料成分备注
     */
    private String c8MaterialAttrCompositionComment;
    /**
     * 工艺要求
     */
    private String finish;
    /**
     * 辅料材质
     */
    private String width;
    /**
     * 号型类型
     */
    private String actualSizeRange;
    /**
     * 质检结果
     */
    private String c8MaterialAttrIspectionStatus;
    /**
     * 纱织规格
     */
    private String c8MaterialAttrSaoriSpecifications;
    /**
     * 密度
     */
    private String c8MaterialAttrDensity;

    /**
     * 材料编号
     */
    private String Code;

    /**
     * 材料名称
     */
    private String MaterialName;

    /**
     * 库存单位（新增）
     */
    private String C8_Material_UOM;

    /**
     * 三级分类
     */
    private String C8_Material_3rdCategory;

    /**
     * 四级分类(预留字段，后面可能会用到)
     */
    private String C8_Material_4rdCategory;

    /**
     * 物料来源
     */
    private String Source;

    /**
     * 二级分类
     */
    private String C8_Material_2ndCategory;

    /**
     * 开发年份
     */
    private String C8_Season_Year;

    /**
     * 开发季节
     */
    private String C8_Season_Quarter;

    /**
     * 开发季节ID
     */
    private String C8_Season_QuarterID;

    /**
     * 开发品牌
     */
    private String C8_Season_Brand;

    /**
     * 开发品牌ID
     */
    private String C8_Season_BrandID;

    /**
     * 公斤米数
     */
    private BigDecimal GJMS;

    /**
     * 开发员
     */
    private String Developer;

    /**
     * 采购员
     */
    private String C8_MaterialAttr_Purchanser;

    /**
     * 经缩(%)
     */
    private BigDecimal C8_MaterialAttr_LongitlShrinkage;

    /**
     * 纬缩(%)
     */
    private BigDecimal C8_MaterialAttr_WeftShrinkage;

    /**
     * 采购组
     */
    private String PurchaseTeam;

    /**
     * 克重(g/m2)
     */
    private String Weight;

    /**
     * 成分
     */
    private String Composition;

    /**
     * JIT标识
     */
    private boolean C8_JIT;

    /**
     * 物料类型
     */
    private String ProductType;

    /**
     * 材料启用标识
     */
    private boolean Active;

    /**
     * 材料类型
     */
    private String ProductTypeID;

    /**
     * 采购模式
     */
    private String procurementMode;

    /**
     * 默认超收比例
     */
    private BigDecimal Tolerance;

    /**
     * 厂家成分
     */
    private String SupplierComposition;

    /**
     * 领料方式
     */
    private String PickingMethod;


    /**
     * 图片列表
     */
    private List<String> Images;

    /**
     * 同步ID
     */
    private String SyncID;

    /**
     * PLM ID
     */
    private String PLMId;

    /**
     * 创建人
     */
    private String Creator;

    /**
     * 创建日期
     */
    private Date CreateTime;

    /**
     * 修改人
     */
    private String ModifiedPerson;

    /**
     * 修改日期
     */
    private Date ModifiedTime;


    /**
     * 颜色规格列表
     */
    private List<ColorItem> COLORITEMS;

    /**
     * 材料尺码列表
     */
    private List<ModelItem> MODELITEMS;

    /**
     * 报价列表
     */
    private List<QuotItem> QuotItems;

    public BasicsdatumMaterial toBasicsdatumMaterial(){
        BasicsdatumMaterial basicsdatumMaterial =new BasicsdatumMaterial();
        basicsdatumMaterial.setMaterialCode(Code);
        basicsdatumMaterial.setMaterialName(MaterialName);
        //basicsdatumMaterial.setPurchaseUnitCode(C8_Material_UOM);
        basicsdatumMaterial.setStockUnitCode(C8_Material_UOM);
        basicsdatumMaterial.setMaterialSourceName(Source);
        basicsdatumMaterial.setCategory1Code(ProductTypeID);
        basicsdatumMaterial.setCategory1Name(ProductType);
        basicsdatumMaterial.setMaterialCodeName(Code+"_"+MaterialName);
        basicsdatumMaterial.setCategoryName(ProductType);
        //basicsdatumMaterial.setMaterialCategoryName(ProductType);
        //basicsdatumMaterial.setMaterialCategory(ProductTypeID);
        basicsdatumMaterial.setIngredientSay(c8MaterialAttrCompositionComment);
        basicsdatumMaterial.setProcessRequire(finish);
        basicsdatumMaterial.setAuxiliaryMaterial(width);
        basicsdatumMaterial.setCheckResult(c8MaterialAttrIspectionStatus);
        basicsdatumMaterial.setSpecification(c8MaterialAttrSaoriSpecifications);
        basicsdatumMaterial.setWidthGroup(actualSizeRange);
        basicsdatumMaterial.setDensity(c8MaterialAttrDensity);
//        basicsdatumMaterial.setCategoryIds(ProductTypeID+","+C8_Material_2ndCategory+","+C8_Material_3rdCategory);
        basicsdatumMaterial.setYear(C8_Season_Year);
        basicsdatumMaterial.setCategory1Code(ProductTypeID);
        basicsdatumMaterial.setCategory2Code(C8_Material_2ndCategory);
        basicsdatumMaterial.setCategory3Code(C8_Material_3rdCategory);
        basicsdatumMaterial.setCategoryId(C8_Material_3rdCategory);
        basicsdatumMaterial.setSeasonName(C8_Season_Quarter);
        basicsdatumMaterial.setSeason(C8_Season_QuarterID);
        basicsdatumMaterial.setBrandName(C8_Season_Brand);
        basicsdatumMaterial.setBrand(C8_Season_BrandID);
        basicsdatumMaterial.setKgMNum(GJMS);
        basicsdatumMaterial.setDevName(Developer);
        basicsdatumMaterial.setPurchaseName(C8_MaterialAttr_Purchanser);
        basicsdatumMaterial.setPurchaseDeptName(PurchaseTeam);
        basicsdatumMaterial.setProcMode(procurementMode);
        basicsdatumMaterial.setLongitudeShrink(C8_MaterialAttr_LongitlShrinkage);
        basicsdatumMaterial.setLatitudeShrink(C8_MaterialAttr_WeftShrinkage);
        basicsdatumMaterial.setGramWeight(Weight);
        // basicsdatumMaterial.setIngredient( Composition);
        basicsdatumMaterial.setFactoryComposition(SupplierComposition);
        if (Images!= null){
            String join = StringUtils.join(Images, ";");
            basicsdatumMaterial.setImageUrl(join);
        }
        basicsdatumMaterial.setPickingMethod(PickingMethod);
        basicsdatumMaterial.setCreateName(Creator);
        basicsdatumMaterial.setCreateDate(CreateTime);
        basicsdatumMaterial.setUpdateName(ModifiedPerson);
        basicsdatumMaterial.setUpdateDate(ModifiedTime);
        basicsdatumMaterial.setStatus(Active ? "0" : "1");


        return basicsdatumMaterial;
    }

    /**
     * 颜色规格类
     */

    @Data
    public class ColorItem {
        /**
         * 颜色编码
         */
        private String colorCode;

        /**
         * 颜色名称
         */
        private String colorName;

        /**
         * 颜色是否启用
         */
        private boolean active;

        /**
         * 供应商物料颜色
         */
        private String matColor4Supplier;
    }

    /**
     * 材料尺码类
     */
    @Data
    public class ModelItem {
        /**
         * 尺码
         */
        private String SIZECODE;

        /**
         * 排序码
         */
        private String SORTCODE;

        /**
         * 尺码号型
         */
        private String SIZENAME;

        /**
         * 尺码排序码
         */
        private String CODE;

        /**
         * 规格是否启用
         */
        private boolean Active;

        /**
         * 尺码URL
         */
        private String SizeURL;
    }

    /**
     * 报价类
     */
    @Data
    public static class QuotItem {
        /**
         * 厂家有效门幅/规格
         */
        private String SUPPLIERSIZE;

        /**
         * 尺码URL
         */
        private String Mat_SizeURL;

        /**
         * 材料颜色ID
         */
        private String SUPPLIERCOLORID;

        /**
         * 材料颜色名称
         */
        private String SUPPLIERCOLORNAME;

        /**
         * 订货周期（天）
         */
        private String LeadTime;

        /**
         * 起订量
         */
        private int MOQInitial;

        /**
         * 生产周期
         */
        private String C8_SupplierItemRev_MLeadTime;
        /**
         * 采购报价
         */
        private BigDecimal FOBFullPrice;
        /**
         * 供应商物料号
         */
        private String SupplierMaterial;
        /**
         * 供应商编码
         */
        private String SupplierCode;
        /**
         * 供应商名称
         */
        private String SupplierName;
        /**
         * 意见
         */
        private String Comment;
        /**
         * 结算方式
         */
        private String TradeTermKEY;
        /**
         * 默认报价标识
         */
        private Boolean DefaultQuote;
        /**
         * 结算方式名称
         */
        private String TradeTermNAME;
        /**
         * 单位
         */
        private String C8_Material_UOM;
    }

}




