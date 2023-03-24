/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 类描述： 实体类
 * @address com.base.sbc.pdm.entity.MaterialDetails
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-22 13:32:27
 * @version 1.0
 */
public class MaterialDetails extends Material {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 关联素材库id */
    private String materialId;
    /** 物料类型 */
    private String materialType;
    /** 物料分类 */
    private String materialClasses;
    /** 年份 */
    private String particularYear;
    /** 季节 */
    private String season;
    /** 物料编码 */
    private String materialCode;
    /** 供应商货号 */
    private String supplierNo;
    /** 面料工艺 */
    private String fabricCraft;
    /** 物料来源 */
    private String materialSource;
    /** 物料名称 */
    private String materialName;
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
    /*******************************************getset方法区************************************/
    public String getMaterialId() {
		return materialId;
	}
	public MaterialDetails setMaterialIdAnd(String materialId) {
		this.materialId = materialId;
		return this;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    public String getMaterialType() {
		return materialType;
	}
	public MaterialDetails setMaterialTypeAnd(String materialType) {
		this.materialType = materialType;
		return this;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
    public String getMaterialClasses() {
		return materialClasses;
	}
	public MaterialDetails setMaterialClassesAnd(String materialClasses) {
		this.materialClasses = materialClasses;
		return this;
	}
	public void setMaterialClasses(String materialClasses) {
		this.materialClasses = materialClasses;
	}
    public String getParticularYear() {
		return particularYear;
	}
	public MaterialDetails setParticularYearAnd(String particularYear) {
		this.particularYear = particularYear;
		return this;
	}
	public void setParticularYear(String particularYear) {
		this.particularYear = particularYear;
	}
    public String getSeason() {
		return season;
	}
	public MaterialDetails setSeasonAnd(String season) {
		this.season = season;
		return this;
	}
	public void setSeason(String season) {
		this.season = season;
	}
    public String getMaterialCode() {
		return materialCode;
	}
	public MaterialDetails setMaterialCodeAnd(String materialCode) {
		this.materialCode = materialCode;
		return this;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
    public String getSupplierNo() {
		return supplierNo;
	}
	public MaterialDetails setSupplierNoAnd(String supplierNo) {
		this.supplierNo = supplierNo;
		return this;
	}
	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}
    public String getFabricCraft() {
		return fabricCraft;
	}
	public MaterialDetails setFabricCraftAnd(String fabricCraft) {
		this.fabricCraft = fabricCraft;
		return this;
	}
	public void setFabricCraft(String fabricCraft) {
		this.fabricCraft = fabricCraft;
	}
    public String getMaterialSource() {
		return materialSource;
	}
	public MaterialDetails setMaterialSourceAnd(String materialSource) {
		this.materialSource = materialSource;
		return this;
	}
	public void setMaterialSource(String materialSource) {
		this.materialSource = materialSource;
	}
    public String getMaterialName() {
		return materialName;
	}
	public MaterialDetails setMaterialNameAnd(String materialName) {
		this.materialName = materialName;
		return this;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
    public String getDefRepository() {
		return defRepository;
	}
	public MaterialDetails setDefRepositoryAnd(String defRepository) {
		this.defRepository = defRepository;
		return this;
	}
	public void setDefRepository(String defRepository) {
		this.defRepository = defRepository;
	}
    public String getDefSupplier() {
		return defSupplier;
	}
	public MaterialDetails setDefSupplierAnd(String defSupplier) {
		this.defSupplier = defSupplier;
		return this;
	}
	public void setDefSupplier(String defSupplier) {
		this.defSupplier = defSupplier;
	}
    public String getPlace() {
		return place;
	}
	public MaterialDetails setPlaceAnd(String place) {
		this.place = place;
		return this;
	}
	public void setPlace(String place) {
		this.place = place;
	}
    public String getCostPrice() {
		return costPrice;
	}
	public MaterialDetails setCostPriceAnd(String costPrice) {
		this.costPrice = costPrice;
		return this;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
    public String getUnit() {
		return unit;
	}
	public MaterialDetails setUnitAnd(String unit) {
		this.unit = unit;
		return this;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
    public String getDefLossRate() {
		return defLossRate;
	}
	public MaterialDetails setDefLossRateAnd(String defLossRate) {
		this.defLossRate = defLossRate;
		return this;
	}
	public void setDefLossRate(String defLossRate) {
		this.defLossRate = defLossRate;
	}
    public String getLongitude() {
		return longitude;
	}
	public MaterialDetails setLongitudeAnd(String longitude) {
		this.longitude = longitude;
		return this;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
    public String getLatitude() {
		return latitude;
	}
	public MaterialDetails setLatitudeAnd(String latitude) {
		this.latitude = latitude;
		return this;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
    public String getPurchaseLossRate() {
		return purchaseLossRate;
	}
	public MaterialDetails setPurchaseLossRateAnd(String purchaseLossRate) {
		this.purchaseLossRate = purchaseLossRate;
		return this;
	}
	public void setPurchaseLossRate(String purchaseLossRate) {
		this.purchaseLossRate = purchaseLossRate;
	}
    public String getBulkBatch() {
		return bulkBatch;
	}
	public MaterialDetails setBulkBatchAnd(String bulkBatch) {
		this.bulkBatch = bulkBatch;
		return this;
	}
	public void setBulkBatch(String bulkBatch) {
		this.bulkBatch = bulkBatch;
	}
    public String getPurchasePeriod() {
		return purchasePeriod;
	}
	public MaterialDetails setPurchasePeriodAnd(String purchasePeriod) {
		this.purchasePeriod = purchasePeriod;
		return this;
	}
	public void setPurchasePeriod(String purchasePeriod) {
		this.purchasePeriod = purchasePeriod;
	}
    public String getMinStock() {
		return minStock;
	}
	public MaterialDetails setMinStockAnd(String minStock) {
		this.minStock = minStock;
		return this;
	}
	public void setMinStock(String minStock) {
		this.minStock = minStock;
	}
    public String getSafetyStock() {
		return safetyStock;
	}
	public MaterialDetails setSafetyStockAnd(String safetyStock) {
		this.safetyStock = safetyStock;
		return this;
	}
	public void setSafetyStock(String safetyStock) {
		this.safetyStock = safetyStock;
	}
    public String getFreeze() {
		return freeze;
	}
	public MaterialDetails setFreezeAnd(String freeze) {
		this.freeze = freeze;
		return this;
	}
	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}
    public String getStrategyStock() {
		return strategyStock;
	}
	public MaterialDetails setStrategyStockAnd(String strategyStock) {
		this.strategyStock = strategyStock;
		return this;
	}
	public void setStrategyStock(String strategyStock) {
		this.strategyStock = strategyStock;
	}
    public String getWidth() {
		return width;
	}
	public MaterialDetails setWidthAnd(String width) {
		this.width = width;
		return this;
	}
	public void setWidth(String width) {
		this.width = width;
	}
    public String getGram() {
		return gram;
	}
	public MaterialDetails setGramAnd(String gram) {
		this.gram = gram;
		return this;
	}
	public void setGram(String gram) {
		this.gram = gram;
	}
    public String getRollWeight() {
		return rollWeight;
	}
	public MaterialDetails setRollWeightAnd(String rollWeight) {
		this.rollWeight = rollWeight;
		return this;
	}
	public void setRollWeight(String rollWeight) {
		this.rollWeight = rollWeight;
	}
    public String getPaperCylinder() {
		return paperCylinder;
	}
	public MaterialDetails setPaperCylinderAnd(String paperCylinder) {
		this.paperCylinder = paperCylinder;
		return this;
	}
	public void setPaperCylinder(String paperCylinder) {
		this.paperCylinder = paperCylinder;
	}
    public String getEmptyPoor() {
		return emptyPoor;
	}
	public MaterialDetails setEmptyPoorAnd(String emptyPoor) {
		this.emptyPoor = emptyPoor;
		return this;
	}
	public void setEmptyPoor(String emptyPoor) {
		this.emptyPoor = emptyPoor;
	}
    public String getEmptyPoorRatio() {
		return emptyPoorRatio;
	}
	public MaterialDetails setEmptyPoorRatioAnd(String emptyPoorRatio) {
		this.emptyPoorRatio = emptyPoorRatio;
		return this;
	}
	public void setEmptyPoorRatio(String emptyPoorRatio) {
		this.emptyPoorRatio = emptyPoorRatio;
	}
    public String getMaterialPrice() {
		return materialPrice;
	}
	public MaterialDetails setMaterialPriceAnd(String materialPrice) {
		this.materialPrice = materialPrice;
		return this;
	}
	public void setMaterialPrice(String materialPrice) {
		this.materialPrice = materialPrice;
	}
    public String getMaterialIngredient() {
		return materialIngredient;
	}
	public MaterialDetails setMaterialIngredientAnd(String materialIngredient) {
		this.materialIngredient = materialIngredient;
		return this;
	}
	public void setMaterialIngredient(String materialIngredient) {
		this.materialIngredient = materialIngredient;
	}
}
