/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：调样-辅料信息 实体类
 * @address com.base.sbc.module.sample.entity.FabricIngredientsInfo
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-15 13:42:13
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_ingredients_info")
@ApiModel("调样-辅料信息 FabricIngredientsInfo")
public class FabricIngredientsInfo extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @TableField(exist = false)
    private String codeName;
    public String getCodeName() {
        String format = String.format("%06d", this.getCode());
        return "fldyd"+format;
    }

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 品类id */
    @ApiModelProperty(value = "品类id"  )
    private String categoryId;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String categoryName;
//    /** 品牌 */
//    @NotBlank(message = "品牌字段不能为空")
//    @ApiModelProperty(value = "品牌"  )
//    private String brand;
//    /** 品牌名称 */
//    @NotBlank(message = "品牌名称字段不能为空")
//    @ApiModelProperty(value = "品牌名称"  )
//    private String brandName;
    /** 开发类型 */
    @ApiModelProperty(value = "开发类型"  )
    private String devType;
    /** 开发类型名称 */
    @ApiModelProperty(value = "开发类型名称"  )
    private String devTypeName;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String imageUrl;
    /** 厂家寄出时间 */
    @ApiModelProperty(value = "厂家寄出时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendDate;
    /** 开发厂家 */
    @ApiModelProperty(value = "开发厂家"  )
    private String devManufacturer;
    /** 开发情况 */
    @ApiModelProperty(value = "开发情况"  )
    private String devCondition;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")

    @TableField( fill = FieldFill.INSERT_UPDATE)
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField( fill = FieldFill.INSERT_UPDATE)
    private Date practicalAtactiformDate;
    /** 看样1 */
    @ApiModelProperty(value = "看样1"  )
    private String result1;
    /** 看样2 */
    @ApiModelProperty(value = "看样2"  )
    private String result2;
    /** 看样3 */
    @ApiModelProperty(value = "看样3"  )
    private String result3;
    /** 厂家 */
    @ApiModelProperty(value = "厂家"  )
    private String manufacturer;
    /** 厂家编号 */
    @ApiModelProperty(value = "厂家编号"  )
    private String manufacturerNumber;
    /** 首单期货 */
    @ApiModelProperty(value = "首单期货"  )
    private String futures;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer orderedQuantity;

    /** 规格编码 */
    @ApiModelProperty(value = "规格编码"  )
    private String specificationCode;
    /** 设计到样时间 */
    @ApiModelProperty(value = "设计到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designAtactiformDate;
    /** 调样设计师 */
    @ApiModelProperty(value = "调样设计师"  )
    private String atactiformStylist;
    /** 调样设计师 */
    @ApiModelProperty(value = "调样设计师"  )
    private String atactiformStylistUserId;
    /** 调样使用人 */
    @ApiModelProperty(value = "调样使用人"  )
    private String sampleUser;
    /** 调样使用人id */
    @ApiModelProperty(value = "调样使用人id"  )
    private String sampleUserId;
    /** 到样检测时间 */
    @ApiModelProperty(value = "到样检测时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date testingDate;
    /** 检查结果 0不可用 1可用 */
    @ApiModelProperty(value = "检查结果 0不可用 1可用"  )
    private String testingResult;
    /** 是否下单 0否 1是 */
    @ApiModelProperty(value = "是否下单 0否 1是"  )
    private String isBuy;
    /** 意见 */
    @ApiModelProperty(value = "意见"  )
    private String opinion;
    /** 使用款号 */
    @ApiModelProperty(value = "使用款号"  )
    private String styleNo;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    private Integer code;

    /**
     * 完成状态
     */
    @ApiModelProperty(value = "完成状态"  )
    private String completionStatus;

    /** 创建部门 */
    @ApiModelProperty(value = "创建部门"  )
    private String createDeptId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

