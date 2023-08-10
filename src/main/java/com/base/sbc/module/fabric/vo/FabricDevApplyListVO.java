package com.base.sbc.module.fabric.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("开发申请列表")
public class FabricDevApplyListVO {
    private String id;
    @ApiModelProperty(value = "开发申请单号")
    private String devApplyCode;
    @ApiModelProperty(value = "图片")
    private String image;
    /**
     * 简码
     */
    @ApiModelProperty(value = "简码")
    private String shortCode;
    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;
    /**
     * 来源1.新增、2.其他
     */
    @ApiModelProperty(value = "来源1.新增、2.其他")
    private String source;
    /**
     * 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；
     */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；")
    private String fabricLabel;

    /**
     * 面料分类
     */
    @ApiModelProperty(value = "面料分类")
    private String fabricClassif;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 材料类型
     */
    @ApiModelProperty(value = "材料类型")
    private String materialType;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 询价
     */
    @ApiModelProperty(value = "询价")
    private BigDecimal inquiry;
    /**
     * 开发状态:1.待分配、2.进行中、3.已完成
     */
    @ApiModelProperty(value = "开发状态:1.待分配、2.进行中、3.已完成")
    private String allocationStatus;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String category;
    /**
     * 标准生产周期（天）
     */
    @ApiModelProperty(value = "标准生产周期（天）")
    private Integer prodCycle;
    /**
     * 起订量（米）
     */
    @ApiModelProperty(value = "起订量（米）")
    private Integer moq;

    /**
     * 是否物料档案接受 0.否、1.是
     */
    @ApiModelProperty(value = "是否物料档案接受 0.否、1.是")
    private String materialAcceptFlag;
    /**
     * 是否转至物料档案 0.否、1.是
     */
    @ApiModelProperty(value = "是否转至物料档案 0.否、1.是")
    private String toMaterialFlag;
    /** 要求到料日期 */
    @ApiModelProperty(value = "要求到料日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requiredArrivalDate;
    /**
     * 预计开始时间
     */
    @ApiModelProperty(value = "预计开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectStartDate;
    /**
     * 预计结束时间
     */
    @ApiModelProperty(value = "预计结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectEndDate;
    /**
     * 实际开始时间
     */
    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalStartDate;
    /**
     * 实际开始时间
     */
    @ApiModelProperty(value = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalEndDate;
    /**
     * 物料类别
     */
    @ApiModelProperty(value = "物料类别")
    private String materialCategory;
    /**
     * 物料属性
     */
    @ApiModelProperty(value = "物料属性")
    private String materialAttribute;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brand;
    /**
     * 旧料号
     */
    @ApiModelProperty(value = "旧料号")
    private String oldMaterialCode;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 形状功能描述
     */
    @ApiModelProperty(value = "形状功能描述")
    private String shapeFunctionDescription;
    /**
     * 色号和型号
     */
    @ApiModelProperty(value = "色号和型号")
    private String colorAndModel;
    /**
     * 厂家成分
     */
    @ApiModelProperty(value = "厂家成分")
    private String factoryIngredient;
    /**
     * 面料成分
     */
    @ApiModelProperty(value = "面料成分")
    private String fabricIngredient;
    /**
     * 面料成分说明
     */
    @ApiModelProperty(value = "面料成分说明")
    private String fabricIngredientSay;
    /**
     * 克重
     */
    @ApiModelProperty(value = "克重")
    private BigDecimal gramWeight;
    /**
     * 工艺要求
     */
    @ApiModelProperty(value = "工艺要求")
    private String technicsRequirements;
    /**
     * 辅料材质
     */
    @ApiModelProperty(value = "辅料材质")
    private String auxiliaryMaterial;
    /**
     * 经缩
     */
    @ApiModelProperty(value = "经缩")
    private BigDecimal longitudeShrink;
    /**
     * 纬缩
     */
    @ApiModelProperty(value = "纬缩")
    private BigDecimal latitudeShrink;
    /**
     * 损耗%
     */
    @ApiModelProperty(value = "损耗%")
    private BigDecimal lossRate;
    /**
     * 采购单位
     */
    @ApiModelProperty(value = "采购单位")
    private String purchasingUnit;
    /**
     * 库存单位
     */
    @ApiModelProperty(value = "库存单位")
    private String stockUnit;
    /**
     * 采购转库存
     */
    @ApiModelProperty(value = "采购转库存")
    private String purchaseToStock;
    /**
     * 默认供应商
     */
    @ApiModelProperty(value = "默认供应商")
    private String defaultSupplier;
    /**
     * 成分确认
     */
    @ApiModelProperty(value = "成分确认")
    private String ingredientConfirm;
    /**
     * 送检单位
     */
    @ApiModelProperty(value = "送检单位")
    private String checkUnit;
    /**
     * 送检结果
     */
    @ApiModelProperty(value = "送检结果")
    private String checkResult;
    /**
     * 质检日期
     */
    @ApiModelProperty(value = "质检日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qualityCheckDate;
    /**
     * 公斤米数
     */
    @ApiModelProperty(value = "公斤米数")
    private Integer kgMNum;
    /**
     * 长度
     */
    @ApiModelProperty(value = "长度")
    private String length;
    /**
     * 物料来源
     */
    @ApiModelProperty(value = "物料来源")
    private String materialSource;
    /**
     * 直径
     */
    @ApiModelProperty(value = "直径")
    private String diameter;
    /**
     * 询价编号
     */
    @ApiModelProperty(value = "询价编号")
    private String inquiryCode;
    /**
     * 开发员
     */
    @ApiModelProperty(value = "开发员")
    private String developer;

    /**
     * 采购组
     */
    @ApiModelProperty(value = "采购组")
    private String purchaseGroup;

    @ApiModelProperty(value = "采购员")
    private String purchaseName;

    /**
     * 纱支规格
     */
    @ApiModelProperty(value = "纱支规格")
    private String yarnCountSpecification;
    /**
     * 密度
     */
    @ApiModelProperty(value = "密度")
    private String density;
    /**
     * 库存可用量
     */
    @ApiModelProperty(value = "库存可用量")
    private Integer stockAvailability;
    /**
     * 门幅
     */
    @ApiModelProperty(value = "门幅")
    private BigDecimal width;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
}
