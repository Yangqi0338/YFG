package com.base.sbc.module.basicsdatum.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 物料导出数据
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月30日
 */
@Data
@ApiModel("物料档案导出数据")
public class BasicsdatumMaterialExcelVo {

	@Excel(name = "物料编号")
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	@Excel(name = "物料名称")
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	@Excel(name = "状态(0正常,1停用)")
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	@Excel(name = "材料")
	@ApiModelProperty(value = "材料")
	private String materialCodeName;
	@Excel(name = "品牌名称")
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	@Excel(name = "大类名称")
	@ApiModelProperty(value = "大类名称")
	private String category1Name;
	@Excel(name = "中类名称")
	@ApiModelProperty(value = "中类名称")
	private String category2Name;
	@Excel(name = "小类名称")
	@ApiModelProperty(value = "小类名称")
	private String category3Name;
	@Excel(name = "类别名称第4级名称")
	@ApiModelProperty(value = "类别名称")
	private String categoryName;
	@Excel(name = "图片地址")
	@ApiModelProperty(value = "图片地址")
	private String imageUrl;
	@Excel(name = "年份名称")
	@ApiModelProperty(value = "年份")
	private String yearName;
	@Excel(name = "季节名称")
	@ApiModelProperty(value = "季节")
	private String seasonName;
	@Excel(name = "材料类型名称")
	@ApiModelProperty(value = "材料类型")
	private String materialCategoryName;
	@Excel(name = "门幅/规格组名称")
	@ApiModelProperty(value = "门幅/规格组")
	private String widthGroupName;

	@Excel(name = "面料成分")
	@ApiModelProperty(value = "面料成分")
	private String ingredient;
	@Excel(name = "采购模式名称")
	@ApiModelProperty(value = "采购模式")
	private String procModeName;
	@Excel(name = "领料方式名称")
	@ApiModelProperty(value = "领料方式")
	private String pickingMethodName;
	@Excel(name = "物料来源名称")
	@ApiModelProperty(value = "物料来源")
	private String materialSourceName;
	@Excel(name = "开发员")
	@ApiModelProperty(value = "开发员")
	private String devName;
	@Excel(name = "采购组名称")
	@ApiModelProperty(value = "采购组")
	private String purchaseDeptName;
	@Excel(name = "采购员")
	@ApiModelProperty(value = "采购员")
	private String purchaseName;
	@Excel(name = "总库存")
	@ApiModelProperty(value = "总库存")
	private BigDecimal totalInventory;
	@Excel(name = "自由库存")
	@ApiModelProperty(value = "自由库存")
	private BigDecimal inventoryAvailability;
	@Excel(name = "面料难度名称")
	@ApiModelProperty(value = "面料难度")
	private String fabricDifficultyName;
	@Excel(name = "备注")
	@ApiModelProperty(value = "备注")
	private String remarks;
	@Excel(name = "采购单位名称")
	@ApiModelProperty(value = "采购单位名称")
	private String purchaseUnitName;
	@Excel(name = "库存单位名称")
	@ApiModelProperty(value = "库存单位名称")
	private String stockUnitName;
	@Excel(name = "采购转库存")
	@ApiModelProperty(value = "采购转库存")
	private String purchaseConvertStock;
	@Excel(name = "默认供应商名称")
	@ApiModelProperty(value = "默认供应商名称")
	private String supplierName;
	@Excel(name = "默认供应商id")
	@ApiModelProperty(value = "默认供应商编码")
	private String supplierId;
	@Excel(name = "供应商物料编号")
	@ApiModelProperty(value = "供应商物料编号")
	private String supplierFabricCode;
	@Excel(name = "供应商报价")
	@ApiModelProperty(value = "供应商报价")
	private BigDecimal supplierQuotationPrice;
	@Excel(name = "供应商色号")
	@ApiModelProperty(value = "供应商色号")
	private String supplierColorNo;
	@Excel(name = "供应商颜色描述")
	@ApiModelProperty(value = "供应商颜色描述")
	private String supplierColorSay;
//	@Excel(name = "供应商面料成分")
//	@ApiModelProperty(value = "供应商面料成分")
//	private String supplierIngredient;
//	@Excel(name = "供应商厂家成分")
//	@ApiModelProperty(value = "供应商厂家成分")
//	private String supplierFactoryIngredient;
	@Excel(name = "面料成分说明")
	@ApiModelProperty(value = "面料成分说明")
	private String ingredientSay;
	@Excel(name = "面料卖点")
	@ApiModelProperty(value = "面料卖点")
	private String fabricSalePoint;
	@Excel(name = "有胚周期(天)")
	@ApiModelProperty(value = "有胚周期(天)")
	private BigDecimal embryonicCycle;
	@Excel(name = "无胚周期(天)")
	@ApiModelProperty(value = "无胚周期(天)")
	private BigDecimal embryonicFreeCycle;
	@Excel(name = "补单生产周期")
	@ApiModelProperty(value = "补单生产周期")
	private BigDecimal replenishmentProductionCycle;
	@Excel(name = "纱支规格")
	@ApiModelProperty(value = "纱支规格")
	private String specification;
	@Excel(name = "密度")
	@ApiModelProperty(value = "密度")
	private String density;
	@Excel(name = "克重")
	@ApiModelProperty(value = "克重")
	private String gramWeight;
	@Excel(name = "织造类型名称")
	@ApiModelProperty(value = "织造类型")
	private String weaveTypeName;
	@Excel(name = "胚布类型名称")
	@ApiModelProperty(value = "胚布类型")
	private String embryoTypeName;
	@Excel(name = "面料属性分类名称")
	@ApiModelProperty(value = "面料属性分类")
	private String fabricPropertyTypeName;
	@Excel(name = "辅料材质")
	@ApiModelProperty(value = "辅料材质")
	private String auxiliaryMaterial;
	@Excel(name = "理化报告路径")
	@ApiModelProperty(value = "理化报告路径")
	private String pcReport;
	@Excel(name = "送检单号")
	@ApiModelProperty(value = "送检单号")
	private String checkBillCode;
	@Excel(name = "送检单位名称")
	@ApiModelProperty(value = "送检单位")
	private String checkCompanyName;
	@Excel(name = "质检结果")
	@ApiModelProperty(value = "质检结果")
	private String checkResult;
	@Excel(name = "质检日期")
	@ApiModelProperty(value = "质检日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate;
	@Excel(name = "有效期")
	@ApiModelProperty(value = "有效期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkValidDate;
	/** 质检项目 */
	@Excel(name = "质检项目")
	private String checkItems;
	/** 质检制单人 */
	@Excel(name = "质检制单人")
	private String checkOrderUserName;
	/** 质检文件路径 */
	@Excel(name = "质检文件路径")
	private String checkFileUrl;
	@Excel(name = "面料难度评分")
	@ApiModelProperty(value = "面料难度评分")
	private String fabricDifficultyScoreName;
	@Excel(name = "面料评价")
	@ApiModelProperty(value = "面料评价")
	private String fabricEvaluation;
	@Excel(name = "风险评估")
	@ApiModelProperty(value = "风险评估")
	private String riskDescription;
}
