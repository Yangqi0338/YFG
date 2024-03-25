/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：基础资料-颜色库 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibraryAgent
 * @email your email
 * @date 创建时间：2024-2-28 16:13:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_colour_library_agent")
@ApiModel("基础资料-颜色库 BasicsdatumColourLibraryAgent")
public class BasicsdatumColourLibraryAgent extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    private String colourCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String colourName;
    /**
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    /**
     * 色系
     */
    @ApiModelProperty(value = "色系")
    private String colorType;
    /**
     * 色系名称
     */
    @ApiModelProperty(value = "色系名称")
    private String colorTypeName;
    /**
     * 颜色规格
     */
    @ApiModelProperty(value = "颜色规格")
    private String colourSpecification;
    /**
     * 库
     */
    @ApiModelProperty(value = "库")
    private String library;
    /**
     * 可用于款式
     */
    @ApiModelProperty(value = "可用于款式")
    private String isStyle;
    /**
     * 可用于材料
     */
    @ApiModelProperty(value = "可用于材料")
    private String isMaterials;
    /**
     * 潘通
     */
    @ApiModelProperty(value = "潘通")
    private String pantone;
    /**
     * RGB三角
     */
    @ApiModelProperty(value = "RGB三角")
    private String colorRgb;
    /**
     * 16进制颜色
     */
    @ApiModelProperty(value = "16进制颜色")
    private String color16;
    /**
     * 色度名称
     */
    @ApiModelProperty(value = "色度名称")
    private String chromaName;
    /**
     * 色度
     */
    @ApiModelProperty(value = "色度")
    private String chroma;
    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称")
    private String englishName;
    /**
     * 法文名称
     */
    @ApiModelProperty(value = "法文名称")
    private String frenchName;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String picture;
    /**
     * SCM下发状态:0未下发,1已下发
     */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;
    /**
     * 品牌,多选
     */
    @ApiModelProperty(value = "品牌,多选")
    private String brand;
    /**
     * 品牌名称,多选
     */
    @ApiModelProperty(value = "品牌名称,多选")
    private String brandName;
    /**
     * 关联集团颜色
     */
    @ApiModelProperty(value = "关联集团颜色")
    private String colorId;

    @TableField(exist = false)
    private String sysColorCode;
    @TableField(exist = false)
    private String sysColorName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
