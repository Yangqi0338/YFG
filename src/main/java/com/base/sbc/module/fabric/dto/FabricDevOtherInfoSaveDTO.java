/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 面料开发其他信息保存
 */
@Data
@ApiModel("面料开发其他信息保存")
public class FabricDevOtherInfoSaveDTO {

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String bizId;
    /**
     * 采购单位
     */
    @ApiModelProperty(value = "采购单位")
    private String purchasingUnit;
    /**
     * 采购单位编码
     */
    @ApiModelProperty(value = "采购单位编码")
    private String purchasingUnitCode;
    /**
     * 库存单位
     */
    @ApiModelProperty(value = "库存单位")
    private String stockUnit;
    /**
     * 库存单位编码
     */
    @ApiModelProperty(value = "库存单位编码")
    private String stockUnitCode;
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
     * 默认供应商id
     */
    @ApiModelProperty(value = "默认供应商id")
    private String defaultSupplierId;
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
     * 送检单位编码
     */
    @ApiModelProperty(value = "送检单位编码")
    private String checkUnitCode;
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
     * 物料来源编码
     */
    @ApiModelProperty(value = "物料来源编码")
    private String materialSourceCode;
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
     * 开发员id
     */
    @ApiModelProperty(value = "开发员id")
    private String developerId;
    /**
     * 采购组
     */
    @ApiModelProperty(value = "采购组")
    private String purchaseGroup;
    /**
     * 采购组id
     */
    @ApiModelProperty(value = "采购组id")
    private String purchaseGroupId;
    /**
     * 采购员
     */
    @ApiModelProperty(value = "采购员")
    private String purchaseName;
    /**
     * 采购员id
     */
    @ApiModelProperty(value = "采购员id")
    private String purchaseNameId;
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
}
