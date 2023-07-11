/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：款式管理-订货本与配色中间表 实体类
 * @address com.base.sbc.module.sample.entity.SampleStyleOrderBookColor
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-11 14:49:39
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_style_order_book_color")
@ApiModel("款式管理-订货本与配色中间表 SampleStyleOrderBookColor")
public class SampleStyleOrderBookColor extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 订货本编号 */
    @ApiModelProperty(value = "订货本编号"  )
    private String orderBookCode;
    /** 款式(大货款号) */
    @ApiModelProperty(value = "款式(大货款号)"  )
    private String styleNo;
    /** 套装标记(0单件，1套装) */
    @ApiModelProperty(value = "套装标记(0单件，1套装)"  )
    private String groupFlag;
    /** 搭配编码 */
    @ApiModelProperty(value = "搭配编码"  )
    private String groupCode;

    /** 设计状态 */
	@ApiModelProperty(value = "下发状态")
	private String orderBookStatus;
	/** 设计状态 */
    @ApiModelProperty(value = "设计状态"  )
    private String designStatus;
    /** 4倍价 */
    @ApiModelProperty(value = "4倍价"  )
    private BigDecimal quadruplePrice;
    /** 设计说明 */
    @ApiModelProperty(value = "设计说明"  )
    private String designSay;
    /** 企划状态 */
    @ApiModelProperty(value = "企划状态"  )
    private String planStatus;
    /** 企划人员 */
    @ApiModelProperty(value = "企划人员"  )
    private String planUserId;
    /** 企划人员 */
    @ApiModelProperty(value = "企划人员"  )
    private String planUserName;
    /** 面料吊牌价 */
    @ApiModelProperty(value = "面料吊牌价"  )
    private BigDecimal fabricTagPrice;
    /** 商品企划状态 */
    @ApiModelProperty(value = "商品企划状态"  )
    private String productPlanStatus;
    /** 商企人员 */
    @ApiModelProperty(value = "商企人员"  )
    private String productPlanUserId;
    /** 商企人员 */
    @ApiModelProperty(value = "商企人员"  )
    private String productPlanUserName;
    /** 投产日期 */
    @ApiModelProperty(value = "投产日期"  )
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionDate;
    /** 预计入仓日期 */
    @ApiModelProperty(value = "预计入仓日期"  )
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date warehousDate;
    /** 紧急程度 */
    @ApiModelProperty(value = "紧急程度"  )
    private String urgency;
    /** 倍率 */
    @ApiModelProperty(value = "倍率"  )
    private BigDecimal magnification;
    /** 总投产 */
    @ApiModelProperty(value = "总投产"  )
    private BigDecimal productionTotalNum;
    /** 尺码数量明细 */
    @ApiModelProperty(value = "尺码数量明细"  )
    private String sizeNums;
    /** 合计 */
    @ApiModelProperty(value = "合计"  )
    private BigDecimal totalNum;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
