/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 类描述：新增修改基础资料-颜色库 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumColourLibrary
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:02
 * @version 1.0
 */
@Data
@ApiModel("基础资料-颜色库 BasicsdatumColourLibrary")
public class AddRevampBasicsdatumColourLibraryDto  {

    private String id;

    /** 颜色组id */
    @ApiModelProperty(value = "颜色组id"  )
    private String colorType;
    /** 色系名称 */
    @ApiModelProperty(value = "色系名称"  )
    private String colorTypeName;
    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colourSpecification;
    /** 代码 */
    @ApiModelProperty(value = "代码"  )
    private String colourCode;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String colourName;
    /** 库 */
    @ApiModelProperty(value = "库"  )
    private String library;
/*    *//** 可用于款式 *//*
    @ApiModelProperty(value = "可用于款式"  )
    private String isStyle;
    *//** 可用于材料 *//*
    @ApiModelProperty(value = "可用于材料"  )
    private String isMaterials;*/
    /** 潘通 */
    @ApiModelProperty(value = "潘通"  )
    private String pantone;
    /** 16进制颜色 */
    @ApiModelProperty(value = "16进制颜色"  )
    private String color16;
    @ApiModelProperty(value = "Rgb"  )
    private String colorRgb;
    /** 色度 */
    @ApiModelProperty(value = "色度"  )
    private String chroma;
    /** 色度名称 */
    @ApiModelProperty(value = "色度名称"  )
    private String chromaName;
    /** 英文名称 */
    @ApiModelProperty(value = "英文名称"  )
    private String englishName;
    /** 法文名称 */
    @ApiModelProperty(value = "法文名称"  )
    private String frenchName;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    /** SCM下发状态:0未下发,1已下发 */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发"  )
    private String scmSendFlag;
}
