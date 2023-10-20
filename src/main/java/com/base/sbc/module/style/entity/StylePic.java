/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：款式设计-设计款图 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.entity.StylePic
 * @email your email
 * @date 创建时间：2023-10-20 19:30:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_pic")
@ApiModel("款式设计-设计款图 StylePic")
public class StylePic extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 款式设计id
     */
    @ApiModelProperty(value = "款式设计id")
    private String styleId;
    /**
     * 款式设计
     */
    @ApiModelProperty(value = "款式设计")
    private String designNo;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private BigDecimal sort;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String picname;
    /**
     * 是否主图(0否,1是)
     */
    @ApiModelProperty(value = "是否主图(0否,1是)")
    private String mainPicFlag;
    /**
     * 图片类型:0jpg,1png
     */
    @ApiModelProperty(value = "图片类型:0jpg,1png")
    private String pictype;
    /**
     * 是否模特图片:0否,1是
     */
    @ApiModelProperty(value = "是否模特图片:0否,1是")
    private String model;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String quarter;
    /**
     * 调试模式
     */
    @ApiModelProperty(value = "调试模式")
    private String debug;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "返回的文件名称")
    private String fileName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

