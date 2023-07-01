/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：样衣-款式配色 实体类
 * @address com.base.sbc.module.sample.entity.SampleStyleColor
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_style_color")
@ApiModel("样衣-款式配色 SampleStyleColor")
public class SampleStyleColor extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 产品季节id */
    @ApiModelProperty(value = "产品季节id"  )
    private String planningSeasonId;
    /** 样衣id */
    @ApiModelProperty(value = "样衣id"  )
    private String sampleDesignId;
    /** 样衣图(主图) */
    @ApiModelProperty(value = "样衣图(主图)"  )
    private String sampleDesignPic;
    /*配色*/
    private String  colorName;
    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colorSpecification;
    /*颜色库id*/
    private String colourLibraryId;
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
    /** 轻奢款(0否,1:是) */
    @ApiModelProperty(value = "轻奢款(0否,1:是)"  )
    private String isLuxury;
    /** 细分 */
    @ApiModelProperty(value = "细分"  )
    private String subdivide;
    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandCode;
    /** bom */
    @ApiModelProperty(value = "bom"  )
    private String bom;
    /** 销售类型 */
    @ApiModelProperty(value = "销售类型"  )
    private String salesType;
    /** 是否是内饰款(0否,1:是) */
    @ApiModelProperty(value = "是否是内饰款(0否,1:是)"  )
    private String isTrim;
    /** 主款 */
    @ApiModelProperty(value = "主款"  )
    private String principalStyle;
    /** 主款款号 */
    @ApiModelProperty(value = "主款款号"  )
    private String principalStyleNo;
    /** 配饰 */
    @ApiModelProperty(value = "配饰"  )
    private String accessory;
    /** 配饰款号 */
    @ApiModelProperty(value = "配饰款号"  )
    private String accessoryNo;
    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
    private BigDecimal tagPrice;
    /** 厂家 */
    @ApiModelProperty(value = "厂家"  )
    private String manufacturer;
    /** 厂家款号 */
    @ApiModelProperty(value = "厂家款号"  )
    private String manufacturerNo;
    /** 厂家颜色 */
    @ApiModelProperty(value = "厂家颜色"  )
    private String manufacturerColor;
    /** 是否主推(0否,1:是) */
    @ApiModelProperty(value = "是否主推(0否,1:是)"  )
    private String isMainly;
    /** 次品编号 */
    @ApiModelProperty(value = "次品编号"  )
    private String defectiveNo;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
