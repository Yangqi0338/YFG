/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 面料开发颜色信息保存
 */
@Data
@ApiModel("面料开发颜色信息保存")
public class FabricDevColorInfoSaveDTO {
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String bizId;
    /**
     * 颜色代码
     */
    @ApiModelProperty(value = "颜色代码")
    private String colorCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    /**
     * 供应商色号
     */
    @ApiModelProperty(value = "供应商色号")
    private String supplierColorCode;
}
