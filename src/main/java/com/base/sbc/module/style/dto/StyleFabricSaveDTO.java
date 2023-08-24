/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：款式面料保存
 */
@Data
@ApiModel("款式面料保存")
public class StyleFabricSaveDTO {
    private String id;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 面料企划明细id
     */
    @ApiModelProperty(value = "面料企划明细id")
    private String fabricPlanningItemId;
    /**
     * 来源：1.新增，2.基础面料库、3.面料企划、4.其他
     */
    @ApiModelProperty(value = "来源：1.新增，2.基础面料库、3.面料企划、4.其他")
    private String source;
    /**
     * 基础面料id
     */
    @ApiModelProperty(value = "基础面料id")
    private String fabricLibraryId;
    /**
     * 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；
     */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；")
    private String fabricLabel;
    /**
     * 款式id
     */
    @ApiModelProperty(value = "款式id")
    private String styleId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
