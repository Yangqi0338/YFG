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

import java.util.Date;

/**
 * 类描述：资料包 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackInfo
 * @email your email
 * @date 创建时间：2023-9-9 9:26:58
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
     * 主数据id:款式主数据id(t_style_master_data)
     */
    @ApiModelProperty(value = "主数据id:款式主数据id(t_style_master_data)")
    private String foreignId;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 坑位信息id
     */
    @ApiModelProperty(value = "坑位信息id")
    private String planningCategoryItemId;

    /**
     * 款式设计id:款式设计id(t_style)
     */
    @ApiModelProperty(value = "款式设计id:款式设计id(t_style)")
    private String styleId;
    /**
     * 大类code
     */
    @ApiModelProperty(value = "大类code")
    private String prodCategory1st;
    /**
     * 大类名称
     */
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    /**
     * 品类code
     */
    @ApiModelProperty(value = "品类code")
    private String prodCategory;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;
    /**
     * 中类名称
     */
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    /**
     * 小类code
     */
    @ApiModelProperty(value = "小类code")
    private String prodCategory3rd;
    /**
     * 小类名称
     */
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;
    /**
     * 打版指令id
     */
    @ApiModelProperty(value = "打版指令id")
    private String patternMakingId;
    /**
     * 样板号
     */
    @ApiModelProperty(value = "样板号")
    private String patternNo;
    /**
     * 款式配色id
     */
    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
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
     * 生产模式名称
     */
    @ApiModelProperty(value = "生产模式名称")
    private String devtTypeName;
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

    /**
     * 是否是迁移历史数据
     */
    @ApiModelProperty(value = "是否是迁移历史数据")
    private String historicalData;

    /**
     * 工艺接收时间
     */
    @ApiModelProperty(value = "工艺接收时间")
    private Date techReceiveDate;

    /**
     * 下单员部门
     */
    @ApiModelProperty(value = "下单员部门")
    private String orderDept;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

