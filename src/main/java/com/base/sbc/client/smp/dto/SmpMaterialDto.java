package com.base.sbc.client.smp.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/9 10:37:13
 * @mail 247967116@qq.com
 */
@Data
public class  SmpMaterialDto {
    /** 材料编号 */
    private String materialCode;
    /** 材料名称名称 */
    private String materialName;
    /** 材料单位（采购单位） */
    private String materialUnit;
    /** 库存单位（新增） */
    private String stockUnit;
    /** 三级分类 */
    private String thirdLevelCategory;
    /** 物料来源 */
    private String materialSource;
    /** 二级分类 */
    private String secondLevelCategory;
    /** 开发年份 */
    private String seasonYear;
    /** 开发季节 */
    private String seasonQuarter;
    /** 开发季节ID */
    private String seasonQuarterId;
    /** 开发品牌 */
    private String seasonBrand;
    /** 开发品牌ID */
    private String seasonBrandId;
    /** 公斤米数 */
    private String GJMS;
    /** 开发员 */
    private String developer;
    /** 采购员 */
    private String buyer;
    /** 采购组 */
    private String buyerTeam;
    /** 经缩(%) */
    private Double longitudinalShrinkage;
    /** 纬缩(%) */
    private Double weftShrinkage;
    /** 克重(g/m2) */
    private String weight;
    /** 成分 */
    private String composition;
    /** JIT标识 */
    private Boolean JIT;
    /** 物料类型id */
    private String productTypeId;
    /** 物料类型 */
    private String productType;
    /** 材料启用标识 */
    private Boolean active;
    /** 采购模式 */
    private String procurementMode;
    /** 默认超收比例 */
    private String tolerance;
    /** 厂家成分 */
    private String supplierComposition;
    /** 领料方式 */
    private String pickingMethod;
    /** 颜色集合 */
    private List<SmpColor> colorList;
    /** 材料尺码集合 */
    private List<SmpModuleSize> moduleSizeList;
    /** 报价集合 */
    private List<SmpQuot> quotList;
    /**创建人*/
    private String creator;
    /**创建时间*/
    private Date createTime;
    /**修改人*/
    private String modifiedPerson;
    /**修改时间*/
    private Date modifiedTime;
    /**PLMID*/
    private String PLMId;
    /**同步ID*/
    private String syncID;
}
