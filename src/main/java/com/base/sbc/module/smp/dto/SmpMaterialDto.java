package com.base.sbc.module.smp.dto;

import com.base.sbc.module.smp.base.SmpBaseDto;
import com.base.sbc.module.smp.entity.SmpColor;
import com.base.sbc.module.smp.entity.SmpModuleSize;
import com.base.sbc.module.smp.entity.SmpQuot;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/9 10:37:13
 * @mail 247967116@qq.com
 */
@Data
public class  SmpMaterialDto extends SmpBaseDto {
    /** 材料编号 */
    private String materialCode;
    /** 材料名称 */
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
    private BigDecimal kilogramsAndMeters;
    /** 开发员 */
    private String developer;
    /** 采购员 */
    private String buyer;
    /** 采购组 */
    private String buyerTeam;
    /** 经缩(%) */
    private BigDecimal longitudinalShrinkage;
    /** 纬缩(%) */
    private BigDecimal weftShrinkage;
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
    /** 采购模式 */
    private String procurementMode;
    /** 默认超收比例 */
    private BigDecimal tolerance;
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
    /**图片地址集合*/
    private List<String> imgList;
}
