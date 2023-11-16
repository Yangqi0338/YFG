/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.smp.dto.SmpMaterialDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-物料档案 实体类
 *
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-31 16:28:49
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_material")
@ApiModel("基础资料-物料档案 BasicsdatumMaterial")
public class BasicsdatumMaterial extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

	/**********************************
	 * 实体存放的其他字段区 不替换的区域 【other_start】
	 ******************************************/
	public SmpMaterialDto toSmpMaterialDto() {
		IdGen idGen = new IdGen();
		SmpMaterialDto smpMaterialDto = new SmpMaterialDto();
		smpMaterialDto.setMaterialCode(materialCode);
		smpMaterialDto.setCode(materialCode);
		smpMaterialDto.setMaterialName(materialName);
		smpMaterialDto.setMaterialUnit(purchaseUnitCode);
		smpMaterialDto.setStockUnit(stockUnitCode);
		smpMaterialDto.setMaterialSource(materialSourceName);
		smpMaterialDto.setSecondLevelCategory(category2Code);
		smpMaterialDto.setThirdLevelCategory(category3Code);
		smpMaterialDto.setSeasonYear(year);
		smpMaterialDto.setSeasonQuarter(seasonName);
		smpMaterialDto.setSeasonQuarterId(season);
		smpMaterialDto.setSeasonBrand(brandName);
		smpMaterialDto.setSeasonBrandId(brand);
		smpMaterialDto.setKilogramsAndMeters(kgMNum==null ? BigDecimal.valueOf(0) : kgMNum);
		smpMaterialDto.setDeveloper(devName);
		smpMaterialDto.setBuyer(purchaseName);
		smpMaterialDto.setBuyerTeam(purchaseDeptName);
		smpMaterialDto.setLongitudinalShrinkage(longitudeShrink);
		smpMaterialDto.setWeftShrinkage(latitudeShrink);
		smpMaterialDto.setWeight(gramWeight);
		smpMaterialDto.setComposition(ingredient);
		smpMaterialDto.setJIT(null);
		smpMaterialDto.setProductTypeId(category1Code);
		smpMaterialDto.setProductType(category1Name);
		smpMaterialDto.setProcurementMode(procMode);
		smpMaterialDto.setTolerance(null);
		smpMaterialDto.setSupplierComposition(factoryComposition);
		smpMaterialDto.setPickingMethod(pickingMethod);
		try {
			smpMaterialDto.setImgList(Arrays.asList(imageUrl.split(",")));
		}catch (Exception ignored){}

		smpMaterialDto.setId(id);
		smpMaterialDto.setCreator(getCreateName());
		smpMaterialDto.setCreateTime(getCreateDate());
		smpMaterialDto.setModifiedPerson(getUpdateName());
		smpMaterialDto.setModifiedTime(getUpdateDate());
		smpMaterialDto.setPlmId(null);
		smpMaterialDto.setSyncId(String.valueOf(idGen.nextId()));
		smpMaterialDto.setActive("0".equals(status));
		return smpMaterialDto;
	}

	/**********************************
	 * 实体存放的其他字段区 【other_end】
	 ******************************************/

	/*****************************
	 * 数据库字段区 不包含父类公共字段(属性) 【start】
	 ***********************************/
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 物料名称 */
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 材料 */
	@ApiModelProperty(value = "材料")
	private String materialCodeName;
	/** 品牌 */
	@ApiModelProperty(value = "品牌")
	private String brand;
	/** 品牌名称 */
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	/** 大类编码 */
	@ApiModelProperty(value = "大类编码")
	private String category1Code;
	/** 大类名称 */
	@ApiModelProperty(value = "大类名称")
	private String category1Name;
	/** 中类编码 */
	@ApiModelProperty(value = "中类编码")
	private String category2Code;
	/** 中类名称 */
	@ApiModelProperty(value = "中类名称")
	private String category2Name;
	/** 小类编码 */
	@ApiModelProperty(value = "小类编码")
	private String category3Code;
	/** 小类名称 */
	@ApiModelProperty(value = "小类名称")
	private String category3Name;
	/** 物料类别第4级编码 */
	@ApiModelProperty(value = "物料类别第4级编码")
	private String categoryId;
	/** 类别名称第4级名称 */
	@ApiModelProperty(value = "类别名称第4级名称")
	private String categoryName;
	/** 图片地址 */
	@ApiModelProperty(value = "图片地址")
	private String imageUrl;
	/** 年份 */
	@ApiModelProperty(value = "年份")
	private String year;
	/** 年份名称 */
	@ApiModelProperty(value = "年份名称")
	private String yearName;
	/** 季节 */
	@ApiModelProperty(value = "季节")
	private String season;
	/** 季节名称 */
	@ApiModelProperty(value = "季节名称")
	private String seasonName;
	/** 材料类型 */
	@ApiModelProperty(value = "材料类型")
	private String materialCategory;
	/** 材料类型名称 */
	@ApiModelProperty(value = "材料类型名称")
	private String materialCategoryName;
	/** 门幅/规格组 */
	@ApiModelProperty(value = "门幅/规格组")
	private String widthGroup;
	/** 门幅/规格组名称 */
	@ApiModelProperty(value = "门幅/规格组名称")
	private String widthGroupName;

	/** 面料成分 */
	@ApiModelProperty(value = "面料成分")
	private String ingredient;
	/** 采购模式编码 */
	@ApiModelProperty(value = "采购模式编码")
	private String procMode;
	/** 采购模式名称 */
	@ApiModelProperty(value = "采购模式名称")
	private String procModeName;
	/** 领料方式编码 */
	@ApiModelProperty(value = "领料方式编码")
	private String pickingMethod;
	/** 领料方式名称 */
	@ApiModelProperty(value = "领料方式名称")
	private String pickingMethodName;
	/** 物料来源 */
	@ApiModelProperty(value = "物料来源")
	private String materialSource;
	/** 物料来源名称 */
	@ApiModelProperty(value = "物料来源名称")
	private String materialSourceName;
	/** 开发员id */
	@ApiModelProperty(value = "开发员id")
	private String devId;
	/** 开发员 */
	@ApiModelProperty(value = "开发员")
	private String devName;
	/** 采购组 */
	@ApiModelProperty(value = "采购组")
	private String purchaseDept;
	/** 采购组名称 */
	@ApiModelProperty(value = "采购组名称")
	private String purchaseDeptName;
	/** 采购员id */
	@ApiModelProperty(value = "采购员id")
	private String purchaseId;
	/** 采购员 */
	@ApiModelProperty(value = "采购员")
	private String purchaseName;
	/** 总库存 */
	@ApiModelProperty(value = "总库存")
	private BigDecimal totalInventory;
	/** 自由库存 */
	@ApiModelProperty(value = "自由库存")
	private BigDecimal inventoryAvailability;
	/** 面料难度 */
	@ApiModelProperty(value = "面料难度")
	private String fabricDifficulty;
	/** 面料难度名称 */
	@ApiModelProperty(value = "面料难度名称")
	private String fabricDifficultyName;
	/** 备注 */
	@ApiModelProperty(value = "备注")
	private String remarks;
	/** 采购单位 */
	@ApiModelProperty(value = "采购单位")
	private String purchaseUnitCode;
	/** 采购单位名称 */
	@ApiModelProperty(value = "采购单位名称")
	private String purchaseUnitName;
	/** 库存单位 */
	@ApiModelProperty(value = "库存单位")
	private String stockUnitCode;
	/** 库存单位名称 */
	@ApiModelProperty(value = "库存单位名称")
	private String stockUnitName;
	/** 采购转库存 */
	@ApiModelProperty(value = "采购转库存")
	private String purchaseConvertStock;
	/** 默认供应商名称 */
	@ApiModelProperty(value = "默认供应商名称")
	private String supplierName;
	/** 默认供应商id */
	@ApiModelProperty(value = "默认供应商id")
	private String supplierId;
	/** 供应商物料编号 */
	@ApiModelProperty(value = "供应商物料编号")
	private String supplierFabricCode;
	/** 供应商默认报价 */
	@ApiModelProperty(value = "供应商报价")
	private BigDecimal supplierQuotationPrice;
	/** 供应商色号 */
	@ApiModelProperty(value = "供应商色号")
	private String supplierColorNo;
	/** 供应商颜色描述 */
	@ApiModelProperty(value = "供应商颜色描述")
	private String supplierColorSay;
//	/** 供应商面料成分 */
//	@ApiModelProperty(value = "供应商面料成分")
//	private String supplierIngredient;
//	/** 供应商厂家成分 */
//	@ApiModelProperty(value = "供应商厂家成分")
//	private String supplierFactoryIngredient;
	/** 面料成分说明 */
	@ApiModelProperty(value = "面料成分说明")
	private String ingredientSay;
	/** 面料卖点 */
	@ApiModelProperty(value = "面料卖点")
	private String fabricSalePoint;
	/** 有胚周期(天) */
	@ApiModelProperty(value = "有胚周期(天)")
	private BigDecimal embryonicCycle;
	/** 无胚周期(天) */
	@ApiModelProperty(value = "无胚周期(天)")
	private BigDecimal embryonicFreeCycle;
	/** 补单生产周期 */
	@ApiModelProperty(value = "补单生产周期")
	private BigDecimal replenishmentProductionCycle;
	/** 纱支规格 */
	@ApiModelProperty(value = "纱支规格")
	private String specification;
	/** 密度 */
	@ApiModelProperty(value = "密度")
	private String density;
	/** 克重 */
	@ApiModelProperty(value = "克重")
	private String gramWeight;
	/**持续环保（1是 0否 空白） */
	@ApiModelProperty(value = "持续环保（1是 0否 空白）")
	private String isProtection;
	/** 织造类型 */
	@ApiModelProperty(value = "织造类型")
	private String weaveType;
	/** 织造类型名称 */
	@ApiModelProperty(value = "织造类型名称")
	private String weaveTypeName;
	/** 胚布类型 */
	@ApiModelProperty(value = "胚布类型")
	private String embryoType;
	/** 胚布类型名称 */
	@ApiModelProperty(value = "胚布类型名称")
	private String embryoTypeName;
	/** 面料属性分类 */
	@ApiModelProperty(value = "面料属性分类")
	private String fabricPropertyType;
	/** 面料属性分类名称 */
	@ApiModelProperty(value = "面料属性分类名称")
	private String fabricPropertyTypeName;
	/** 辅料材质 */
	@ApiModelProperty(value = "辅料材质")
	private String auxiliaryMaterial;
	/** 理化报告路径 */
	@ApiModelProperty(value = "理化报告路径")
	private String pcReport;
	/** 送检单号 */
	@ApiModelProperty(value = "送检单号")
	private String checkBillCode;
	/** 送检单位 */
	@ApiModelProperty(value = "送检单位")
	private String checkCompany;
	/** 送检单位名称 */
	@ApiModelProperty(value = "送检单位名称")
	private String checkCompanyName;
	/** 质检结果 */
	@ApiModelProperty(value = "质检结果")
	private String checkResult;
	/** 质检日期 */
	@ApiModelProperty(value = "质检日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate;
	/** 有效期 */
	@ApiModelProperty(value = "有效期")
	private Integer checkValidDate;
	/** 质检项目 */
	@ApiModelProperty(value = "质检项目")
	private String checkItems;
	/** 质检制单人ID */
	@ApiModelProperty(value = "质检制单人ID")
	private String checkOrderUserId;
	/** 质检制单人 */
	@ApiModelProperty(value = "质检制单人")
	private String checkOrderUserName;
	/** 质检文件路径 */
	@ApiModelProperty(value = "质检文件路径")
	private String checkFileUrl;
	/** 面料难度评分 */
	@ApiModelProperty(value = "面料难度评分")
	private String fabricDifficultyScore;
	/** 面料难度评分名称 */
	@ApiModelProperty(value = "面料难度评分名称")
	private String fabricDifficultyScoreName;
	/** 面料评价 */
	@ApiModelProperty(value = "面料评价")
	private String fabricEvaluation;
	/** 风险评估 */
	@ApiModelProperty(value = "风险评估")
	private String riskDescription;
	/** 下发状态(0:未下发，1：下发) */
	@ApiModelProperty(value = "下发状态(0:未下发，1：下发)")
	private String distribute;
	/** 物料属性 */
	@ApiModelProperty(value = "物料属性")
	private String materialType;
	/** 物料属性名称 */
	@ApiModelProperty(value = "物料属性名称")
	private String materialTypeName;
	/** 色号和型号 */
	@ApiModelProperty(value = "色号和型号")
	private String colorAndModel;
	/** 直径 */
	@ApiModelProperty(value = "直径")
	private String diameter;
	/** 长度 */
	@ApiModelProperty(value = "长度")
	private String length;
	/** 公斤米数 */
	@ApiModelProperty(value = "公斤米数")
	private BigDecimal kgMNum;
	/** 询价编号 */
	@ApiModelProperty(value = "询价编号")
	private String inquiryNo;
	/** 旧料号 */
	@ApiModelProperty(value = "旧料号")
	private String materialCodeOld;
	/** 形状功能描述 */
	@ApiModelProperty(value = "形状功能描述")
	private String shapeFunctionDescription;
	/** 成分确认 */
	@ApiModelProperty(value = "成分确认")
	private String ingredientConfirm;
	/** 来源单号 */
	@ApiModelProperty(value = "来源单号")
	private String sourceBillCode;
	/** 来源类型 */
	@ApiModelProperty(value = "来源类型")
	private String sourceType;
	/** 经缩 */
	@ApiModelProperty(value = "经缩")
	private BigDecimal longitudeShrink;
	/** 纬缩 */
	@ApiModelProperty(value = "纬缩")
	private BigDecimal latitudeShrink;
	/** 损耗% */
	@ApiModelProperty(value = "损耗%")
	private BigDecimal lossRate;
	/** 工艺要求 */
	@ApiModelProperty(value = "工艺要求")
	private String processRequire;
	/** 厂家成分 */
	@ApiModelProperty(value = "厂家成分")
	private String factoryComposition;
	/** 门幅 */

	@ApiModelProperty(value = "门幅")
	private String translate;
	/** 询价编号 */
	@ApiModelProperty(value = "询价编号")
	private String inquiryNumber;
	/** 货期 */
	@ApiModelProperty(value = "货期")
	private String deliveryName;

	/**
	 * 面料成分下发状态 0未发送,1发送成功，2发送失败,3重新打开
	 */
	@ApiModelProperty(value = "面料成分下发状态 0未发送,1发送成功，2发送失败,3重新打开")
    private String compositionSendStatus;

	/**
	 * 业务类型:material.物料、devApply.面料开发申请、dev.面料开发、fabricLibrary.面料基础库
	 */
	//@ApiModelProperty(value = "业务类型:material.物料、devApply.面料开发申请、dev.面料开发、fabricLibrary.面料基础库")
	private String bizType;

	/**
	 * 审核时间
	 */
	@ApiModelProperty(value = "审核时间")
	private Date confirmDate;
	/**
	 * 审核人id
	 */
	@ApiModelProperty(value = "审核人id")
	private String confirmId;
	/**
	 * 审核人名称
	 */
	@ApiModelProperty(value = "审核人名称")
	private String confirmName;
	/**
	 * 审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过）
	 */
	@ApiModelProperty(value = "审核状态（0：未提交，1：待审核，2：审核通过，3：审核不通过）")
	private String confirmStatus;

	/**
	 * 数据来源：1.新增、2.面料企划、3.其他
	 */
	@ApiModelProperty(value = "数据来源：1.新增、2.面料企划、3.其他")
	private String source;

	/**
	 * 是否战略备料(0是，1否)
	 */
	@ApiModelProperty(value = "是否战略备料(0是，1否)")
	private String isStrategic;

	/**
	 * 附件
	 */
	private String attachment;

	/**
	 * 附件名称
	 */
	private String attachmentName;

	/*****************************
	 * 数据库字段区 不包含父类公共字段(属性) 【end】
	 ***********************************/
}
