/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.dto;

import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：新增修改调样-辅料信息 dto类
 * @address com.base.sbc.module.sample.dto.FabricIngredientsInfo
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-15 13:42:13
 * @version 1.0
 */
@Data
@ApiModel("调样-辅料信息 FabricIngredientsInfo")
public class AddRevampFabricIngredientsInfoDto  extends FabricIngredientsInfo {

    private String id;

    /** 品类id */
    @ApiModelProperty(value = "品类id"  )
    private String categoryId;
    /** 品类名称 */
    @ApiModelProperty(value = "品类名称"  )
    private String categoryName;
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    /** 大货含税价 */
    @ApiModelProperty(value = "大货含税价"  )
    private BigDecimal containPrice;
    /** 厂家编号 */
    @ApiModelProperty(value = "厂家编号"  )
    private String manufacturerNumber;
    /** 首单期货 */
    @ApiModelProperty(value = "首单期货"  )
    private String futures;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer orderedQuantity;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String specification;
    /** 数量 */
    @ApiModelProperty(value = "数量"  )
    private Integer quantity;
    /** 规格编码 */
    @ApiModelProperty(value = "规格编码"  )
    private String specificationCode;
    /** 设计到样时间 */
    @ApiModelProperty(value = "设计到样时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    /** 提交标记(1设计师,0辅料专员) */
    @ApiModelProperty(value = "提交标记(1设计师,0辅料专员)"  )
    private String submitFlag;
}
