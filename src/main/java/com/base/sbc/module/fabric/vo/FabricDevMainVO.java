/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.vo;

import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 面料开发主信息
 */
@Data
@ApiModel("面料开发主信息")
public class FabricDevMainVO {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;
    @ApiModelProperty(value = "修改人")
    private String updateName;
    @ApiModelProperty(value = "创建人")
    private String createName;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 开发申请单号
     */
    @ApiModelProperty(value = "开发申请单号")
    private String devApplyCode;
    /**
     * 开发单号
     */
    @ApiModelProperty(value = "开发单号")
    private String devCode;

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
    @ApiModelProperty(value = "实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalEndDate;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String suppler;
    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String supplerId;
    /**
     * 状态(0.未处理,1.通过，2.失败)
     */
    @ApiModelProperty(value = "状态(0.未处理,1.通过，2.失败)")
    private String status;


    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private String materialId;
    /**
     * 基本信息
     */
    @ApiModelProperty(value = "基本信息")
    private FabricDevBasicInfoVO fabricDevBasicInfo;

    /**
     * 物料信息
     */
    @ApiModelProperty(value = "物料信息")
    private BasicsdatumMaterialVo basicsdatumMaterial;


}
