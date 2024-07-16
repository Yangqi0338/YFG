/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 类描述：面料基本信息 实体类
 * @address com.base.sbc.module.sample.entity.FabricBasicInformation
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-11-2 15:58:41
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_basic_information")
@ApiModel("面料基本信息 FabricBasicInformation")
public class FabricBasicInformation extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    @TableField(exist = false)
    private String codeName;

    public String getCodeName() {
        String format = String.format("%06d", this.getCode());
        return "mldyd"+format;
    }
    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    private Integer code;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份名称 */
    @ApiModelProperty(value = "年份名称"  )
    private String yearName;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节名称 */
    @ApiModelProperty(value = "季节名称"  )
    private String seasonName;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 品牌名称 */
    @ApiModelProperty(value = "品牌名称"  )
    private String brandName;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;

    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;

    /** 供应商简称 */
    @ApiModelProperty(value = "供应商简称"  )
    private String supplierAbbreviation;
    /** 供应商料号 */
    @ApiModelProperty(value = "供应商料号"  )
    private String supplierMaterialCode;
    /** 厂家色号 */
    @ApiModelProperty(value = "厂家色号"  )
    private String supplierColor;
    /** 是否新面料（1是 0否 */
    @ApiModelProperty(value = "是否新面料（1是 0否"  )
    private String isNewFabric;
    /** 数量（米） */
    @ApiModelProperty(value = "数量（米）"  )
    private Integer quantity;

    /** 是否环保（1是 0否） */
    @ApiModelProperty(value = "是否环保（1是 0否）"  )
    private String isProtection;

    /** 调样设计师 */
    @ApiModelProperty(value = "调样设计师"  )
    private String  atactiformStylist;

    /** 调样设计师id */
    @ApiModelProperty(value = "调样设计师id"  )
    private String  atactiformStylistUserId;

    /** 登记时间 */
    @ApiModelProperty(value = "登记时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerDate;

    /** 发送供应商标记(1是0否 */
    @ApiModelProperty(value = "发送供应商标记(1是0否"  )
    private String sendingSupplierFlag;

    /** 面料是否可用(1是，0否) */
    @ApiModelProperty(value = "面料是否可用(1是，0否)"  )
    private String fabricIsUsable;
    /** 面料价格 */
    @ApiModelProperty(value = "面料价格"  )
    private BigDecimal fabricPrice;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格"  )
    private String specification;
    /** 密度 */
    @ApiModelProperty(value = "密度"  )
    private String density;
    /** 厂家成分 */
    @ApiModelProperty(value = "厂家成分"  )
    private String supplierFactoryIngredient;
    /** 货期 */
    @ApiModelProperty(value = "货期"  )
    private Integer leadtime;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer minimumOrderQuantity;
    /** 厂家有效门幅 */
    @ApiModelProperty(value = "厂家有效门幅"  )
    private String translate;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private BigDecimal gramWeight;
    /** 胚布情况 */
    @ApiModelProperty(value = "胚布情况"  )
    private String germinalCondition;
    /** 调样日期 */
    @ApiModelProperty(value = "调样日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date atactiformDate;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date practicalAtactiformDate;
    /** 留样送检时间 */
    @ApiModelProperty(value = "留样送检时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectDate;
    /** 理化检测结果（1是0否 */
    @ApiModelProperty(value = "理化检测结果（1是0否"  )
    private String physicochemistryDetectionResult;
    /** 样衣试穿洗涤送检时间 */
    @ApiModelProperty(value = "样衣试穿洗涤送检时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sampleWashingInspectionDate;
    /** 洗涤检测结果（1是0否 */
    @ApiModelProperty(value = "洗涤检测结果（1是0否"  )
    private String washDetectionResult;
    /** 图片地址 */
    @ApiModelProperty(value = "图片地址"  )
    private String imageUrl;
    /** 是否草稿（1是：0否 */
    @ApiModelProperty(value = "是否草稿（1是：0否"  )
    private String isDraft;
    /** 理化报告地址 */
    @ApiModelProperty(value = "理化报告地址"  )
    private String reportUrl;
    /** 理化报告名 */
    @ApiModelProperty(value = "理化报告名"  )
    private String reportName;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remark;

    /** 创建部门 */
    @ApiModelProperty(value = "创建部门"  )
    private String createDeptId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

