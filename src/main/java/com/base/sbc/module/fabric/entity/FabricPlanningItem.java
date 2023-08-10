/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料企划明细 实体类
 * @address com.base.sbc.module.fabric.entity.FabricPlanningItem
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-7 11:02:03
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_planning_item")
@ApiModel("面料企划明细 FabricPlanningItem")
public class FabricPlanningItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 面料企划 */
    @ApiModelProperty(value = "面料企划"  )
    private String fabricPlanningCode;
    /** 面料企划id */
    @ApiModelProperty(value = "面料企划id"  )
    private String fabricPlanningId;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String image;
    /** 简码 */
    @ApiModelProperty(value = "简码"  )
    private String shortCode;
    /** 顺序 */
    @ApiModelProperty(value = "顺序"  )
    private Integer sort;
    /** 来源：1.新增，2.供应商面料，3.SCM */
    @ApiModelProperty(value = "来源：1.新增，2.供应商面料，3.SCM"  )
    private String source;
    /** 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料； */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；"  )
    private String fabricLabel;
    /** 面料分类 */
    @ApiModelProperty(value = "面料分类"  )
    private String fabricClassif;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 材料类型 */
    @ApiModelProperty(value = "材料类型"  )
    private String materialType;
    /** 材料类型编码 */
    @ApiModelProperty(value = "材料类型编码"  )
    private String materialTypeCode;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /** 标准生产周期（天） */
    @ApiModelProperty(value = "标准生产周期（天）"  )
    private Integer prodCycle;
    /** 询价 */
    @ApiModelProperty(value = "询价"  )
    private BigDecimal inquiry;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String category;
    /** 品类编码 */
    @ApiModelProperty(value = "品类编码"  )
    private String categoryCode;
    /** 起订量（米） */
    @ApiModelProperty(value = "起订量（米）"  )
    private Integer moq;
    /** 大类编码 */
    @ApiModelProperty(value = "大类编码"  )
    private String category1Code;
    /** 大类名称 */
    @ApiModelProperty(value = "大类名称"  )
    private String category1Name;
    /** 中类编码 */
    @ApiModelProperty(value = "中类编码"  )
    private String category2Code;
    /** 中类名称 */
    @ApiModelProperty(value = "中类名称"  )
    private String category2Name;
    /** 小类编码 */
    @ApiModelProperty(value = "小类编码"  )
    private String category3Code;
    /** 小类名称 */
    @ApiModelProperty(value = "小类名称"  )
    private String category3Name;
    /** 物料属性 */
    @ApiModelProperty(value = "物料属性"  )
    private String materialAttribute;
    /** 物料属性编码 */
    @ApiModelProperty(value = "物料属性编码"  )
    private String materialAttributeCode;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brand;
    /** 品牌编码 */
    @ApiModelProperty(value = "品牌编码"  )
    private String brandCode;
    /** 旧料号 */
    @ApiModelProperty(value = "旧料号"  )
    private String oldMaterialCode;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份编码 */
    @ApiModelProperty(value = "年份编码"  )
    private String yearCode;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节编码 */
    @ApiModelProperty(value = "季节编码"  )
    private String seasonCode;
    /** 形状功能描述 */
    @ApiModelProperty(value = "形状功能描述"  )
    private String shapeFunctionDescription;
    /** 色号和型号 */
    @ApiModelProperty(value = "色号和型号"  )
    private String colorAndModel;
    /** 色号和型号编码 */
    @ApiModelProperty(value = "色号和型号编码"  )
    private String colorAndModelCode;
    /** 厂家成分 */
    @ApiModelProperty(value = "厂家成分"  )
    private String factoryIngredient;
    /** 面料成分 */
    @ApiModelProperty(value = "面料成分"  )
    private String fabricIngredient;
    /** 面料成分说明 */
    @ApiModelProperty(value = "面料成分说明"  )
    private String fabricIngredientSay;
    /** 门幅 */
    @ApiModelProperty(value = "门幅"  )
    private BigDecimal width;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private BigDecimal gramWeight;
    /** 工艺要求 */
    @ApiModelProperty(value = "工艺要求"  )
    private String technicsRequirements;
    /** 辅料材质 */
    @ApiModelProperty(value = "辅料材质"  )
    private String auxiliaryMaterial;
    /** 经缩 */
    @ApiModelProperty(value = "经缩"  )
    private BigDecimal longitudeShrink;
    /** 纬缩 */
    @ApiModelProperty(value = "纬缩"  )
    private BigDecimal latitudeShrink;
    /** 损耗% */
    @ApiModelProperty(value = "损耗%"  )
    private BigDecimal lossRate;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位"  )
    private String purchasingUnit;
    /** 采购单位编码 */
    @ApiModelProperty(value = "采购单位编码"  )
    private String purchasingUnitCode;
    /** 库存单位 */
    @ApiModelProperty(value = "库存单位"  )
    private String stockUnit;
    /** 库存单位编码 */
    @ApiModelProperty(value = "库存单位编码"  )
    private String stockUnitCode;
    /** 采购转库存 */
    @ApiModelProperty(value = "采购转库存"  )
    private String purchaseToStock;
    /** 默认供应商 */
    @ApiModelProperty(value = "默认供应商"  )
    private String defaultSupplier;
    /** 默认供应商id */
    @ApiModelProperty(value = "默认供应商id"  )
    private String defaultSupplierId;
    /** 成分确认 */
    @ApiModelProperty(value = "成分确认"  )
    private String ingredientConfirm;
    /** 送检单位 */
    @ApiModelProperty(value = "送检单位"  )
    private String checkUnit;
    /** 送检单位编码 */
    @ApiModelProperty(value = "送检单位编码"  )
    private String checkUnitCode;
    /** 送检结果 */
    @ApiModelProperty(value = "送检结果"  )
    private String checkResult;
    /** 质检日期 */
    @ApiModelProperty(value = "质检日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qualityCheckDate;
    /** 公斤米数 */
    @ApiModelProperty(value = "公斤米数"  )
    private Integer kgMNum;
    /** 长度 */
    @ApiModelProperty(value = "长度"  )
    private String length;
    /** 物料来源 */
    @ApiModelProperty(value = "物料来源"  )
    private String materialSource;
    /** 物料来源编码 */
    @ApiModelProperty(value = "物料来源编码"  )
    private String materialSourceCode;
    /** 直径 */
    @ApiModelProperty(value = "直径"  )
    private String diameter;
    /** 询价编号 */
    @ApiModelProperty(value = "询价编号"  )
    private String inquiryCode;
    /** 开发员 */
    @ApiModelProperty(value = "开发员"  )
    private String developer;
    /** 开发员id */
    @ApiModelProperty(value = "开发员id"  )
    private String developerId;
    /** 采购组 */
    @ApiModelProperty(value = "采购组"  )
    private String purchaseGroup;
    /** 采购组id */
    @ApiModelProperty(value = "采购组id"  )
    private String purchaseGroupId;
    /** 采购员 */
    @ApiModelProperty(value = "采购员"  )
    private String purchaseName;
    /** 采购员id */
    @ApiModelProperty(value = "采购员id"  )
    private String purchaseNameId;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格"  )
    private String yarnCountSpecification;
    /** 密度 */
    @ApiModelProperty(value = "密度"  )
    private String density;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
