/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：新增修改样衣-款式配色 dto类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleStyleColor
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Data
@ApiModel("样衣-款式配色 SampleStyleColor")
public class AddRevampSampleStyleColorDto {

    private String id;

    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;
    /**
     * 样衣id
     */
    @ApiModelProperty(value = "样衣id")
    private String sampleDesignId;
    /**
     * 样衣图(主图)
     */
    @ApiModelProperty(value = "样衣图(主图)")
    private String sampleDesignPic;
    /*配色*/
    private String colorName;
    /**
     * 颜色规格
     */
    @ApiModelProperty(value = "颜色规格")
    private String colorSpecification;
    /*颜色库id*/
    private String colourLibraryId;

    private String colorCode;
    /** 大货款号 */
    @ApiModelProperty(value = "大货款号"  )
    private String styleNo;

    /**
     * 轻奢款(0否,1:是)
     */
    @ApiModelProperty(value = "轻奢款(0否,1:是)")
    private String isLuxury;
    /**
     * 细分
     */
    @ApiModelProperty(value = "细分")
    private String subdivide;
    /**
     * 波段
     */
    @ApiModelProperty(value = "波段")
    private String bandCode;
    /**
     * bom
     */
    @ApiModelProperty(value = "bom")
    private String bom;
    /**
     * 销售类型
     */
    @ApiModelProperty(value = "销售类型")
    private String salesType;
    /*品类名称路径:(大类/品类/中类/小类)'*/
    @ApiModelProperty(value = "品类名称路径:(大类/品类/中类/小类)"  )
    private String categoryName;
    /** 是否下发scm(0否,1:是) */
    @ApiModelProperty(value = "是否下发scm(0否,1:是)"  )
    private String  isIssueScm;
    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private String devtType;
    /*设计款号*/
    @ApiModelProperty(value = "设计款号"  )
    private String   designNo;
    /**
     * 是否是内饰款(0否,1:是)
     */
    @ApiModelProperty(value = "是否是内饰款(0否,1:是)")
    private String isTrim;
    /**
     * 主款
     */
    @ApiModelProperty(value = "主款")
    private String principalStyle;
    /**
     * 主款款号
     */
    @ApiModelProperty(value = "主款款号")
    private String principalStyleNo;
    /**
     * 配饰
     */
    @ApiModelProperty(value = "配饰")
    private String accessory;
    /**
     * 配饰款号
     */
    @ApiModelProperty(value = "配饰款号")
    private String accessoryNo;
    /**
     * 吊牌价
     */
    @ApiModelProperty(value = "吊牌价")
    private BigDecimal tagPrice;
    /**
     * 厂家
     */
    @ApiModelProperty(value = "厂家")
    private String manufacturer;
    /**
     * 厂家款号
     */
    @ApiModelProperty(value = "厂家款号")
    private String manufacturerNo;
    /**
     * 厂家颜色
     */
    @ApiModelProperty(value = "厂家颜色")
    private String manufacturerColor;
    /**
     * 是否主推(0否,1:是)
     */
    @ApiModelProperty(value = "是否主推(0否,1:是)")
    private String isMainly;
    /**
     * 次品编号
     */
    @ApiModelProperty(value = "次品编号")
    private String defectiveNo;
    /*波段企划id'*/
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
     * 状态(0正常,1停用)
     */
    @ApiModelProperty(value = "状态(0正常,1停用)")
    private String status;
    //    原大货款号
    private String hisStyleNo;
    //    款式风格
    private String styleFlavour;
    //    厂家简称
    private String manufacturerJane;
    //    是否报次数0否 1是
    private String isDefective;
    //次品名称
    private String defectiveName;
    /*细分名称*/
    private String subdivideName;
    //销售类型名称
    private String salesTypeName;
    //波段名称
    private String bandName;
    //生产类型名称
    private String devtTypeName;
    /*上新时间*/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date newDate;
}
