/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.dto;

import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialSaveDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 基础面料库更新
 */
@Data
@ApiModel("基础面料库更新")
public class BasicFabricLibrarySaveDTO {
    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private String materialId;
    /**
     * 基本信息
     */
    @ApiModelProperty(value = "基本信息")
    @NotNull(message = "基本信息不可为空")
    private FabricDevBasicInfoSaveDTO fabricDevBasicInfo;

    /**
     * 物料信息
     */
    @ApiModelProperty(value = "物料信息")
    @NotNull(message = "物料信息不可为空")
    private BasicsdatumMaterialSaveDto basicsdatumMaterial;

}
