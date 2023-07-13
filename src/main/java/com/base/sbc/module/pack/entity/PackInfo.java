/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：资料包 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackInfo
 * @email your email
 * @date 创建时间：2023-7-13 10:45:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_info")
@ApiModel("资料包 PackInfo")
public class PackInfo extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id(样衣设计id)
     */
    @ApiModelProperty(value = "主数据id(样衣设计id)")
    private String foreignId;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String code;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 波段企划id
     */
    @ApiModelProperty(value = "波段企划id")
    private String planningBandId;
    /**
     * 品类信息id
     */
    @ApiModelProperty(value = "品类信息id")
    private String planningCategoryId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;
    /**
     * 品类名称路径:(大类/品类/中类/小类)
     */
    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)")
    private String categoryName;
    /**
     * 品类id路径:(大类/品类/中类/小类)
     */
    @ApiModelProperty(value = "品类id路径:(大类/品类/中类/小类)")
    private String categoryIds;
    /**
     * 大类id
     */
    @ApiModelProperty(value = "大类id")
    private String prodCategory1st;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private String prodCategory;
    /**
     * 中类id
     */
    @ApiModelProperty(value = "中类id")
    private String prodCategory2nd;
    /**
     * 小类
     */
    @ApiModelProperty(value = "小类")
    private String prodCategory3rd;
    /**
     * 款式配色id
     */
    @ApiModelProperty(value = "款式配色id")
    private String sampleStyleColorId;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;
    /**
     * 生产模式
     */
    @ApiModelProperty(value = "生产模式")
    private String devtType;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    /**
     * 旧设计款号
     */
    @ApiModelProperty(value = "旧设计款号")
    private String hisDesignNo;
    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

