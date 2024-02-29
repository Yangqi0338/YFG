/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：导入导出基础资料-颜色库 dto类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumColourLibrary
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 20:23:02
 */
@Data
@ApiModel("基础资料-颜色库 BasicsdatumColourLibrary")
public class BasicsdatumColourLibraryAgentExcelDto {

    // @Excel(name = "id")
    private String id;
    private String brand;
    @Excel(name = "品牌名称")
    private String brandName;
    /**
     * 颜色系
     */
    @ApiModelProperty(value = "颜色系")
    private String colorType;
    @Excel(name = "颜色系")
    private String colorTypeName;
    /**
     * 颜色规格
     */
    @ApiModelProperty(value = "颜色规格")
    @Excel(name = "颜色规格")
    private String colourSpecification;
    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    @Excel(name = "代码")
    private String colourCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    @Excel(name = "颜色名称")
    private String colourName;
    /**
     * 库
     */
    @ApiModelProperty(value = "库")
    @Excel(name = "库")
    private String library;
    /**
     * 16进制颜色
     */
    @ApiModelProperty(value = "16进制颜色")
    private String color16;
    //    * 可用于款式
    @ApiModelProperty(value = "可用于款式")
    @Excel(name = "可用于款式", replace = {"false_0", "true_1"})
    private String isStyle;
    //    * 可用于材料
    @ApiModelProperty(value = "可用于材料")
    @Excel(name = "可用于材料", replace = {"false_0", "true_1"})
    private String isMaterials;
    /**
     * 潘通
     */
    @ApiModelProperty(value = "潘通")
    @Excel(name = "潘通")
    private String pantone;
    /**
     * RGB三角
     */
    @ApiModelProperty(value = "RGB三角")
    @Excel(name = "RGB三角")
    private String colorRgb;
    /**
     * 色度
     */
    @ApiModelProperty(value = "色度")
    private String chroma;
    @Excel(name = "色度")
    private String chromaName;
    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称")
    @Excel(name = "英文名称")
    private String englishName;
    /**
     * 法文名称
     */
    @ApiModelProperty(value = "法文名称")
    @Excel(name = "法文名称")
    private String frenchName;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    @Excel(name = "图片", type = 2)
    private String picture;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    @Excel(name = "启用", replace = {"true_0", "false_1"})
    private String status;
    @Excel(name = "对应集团颜色代码")
    private String sysColorCode;
    private String colorId;
}
