package com.base.sbc.module.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：素材库详情表 实体类
 * @author 卞康
 * @date 创建时间：2023-3-24 9:44:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_material_details")
public class MaterialDetails extends BaseDataEntity<String> {
    /** 关联素材库id */
    private String materialId;
    /** 供应商编码 */
    private String supplierCode;
    /** 供应商货号 */
    private String supplierNo;
    /** 面料工艺 */
    private String fabricCraft;
    /** 默认仓库 */
    private String defRepository;
    /** 默认供应商 */
    private String defSupplier;
    /** 场地 */
    private String place;
    /** 成本价 */
    private String costPrice;
    /** 单位 */
    private String unit;
    /** 默认损耗% */
    private String defLossRate;
    /** 经缩 */
    private String longitude;
    /** 纬缩 */
    private String latitude;
    /** 采购损耗 */
    private String purchaseLossRate;
    /** 起订量 */
    private String bulkBatch;
    /** 采购周期 */
    private String purchasePeriod;
    /** 最小库存 */
    private String minStock;
    /** 安全库存 */
    private String safetyStock;
    /** 是否冻结（0：否，1：是） */
    private String freeze;
    /** 战略备料 */
    private String strategyStock;
    /** 门幅 */
    private String width;
    /** 克重 */
    private String gram;
    /** 卷重 */
    private String rollWeight;
    /** 纸筒 */
    private String paperCylinder;
    /** 空差 */
    private String emptyPoor;
    /** 空差比 */
    private String emptyPoorRatio;
    /** 物料销售价 */
    private String materialPrice;
    /** 物料成分 */
    private String materialIngredient;
}

