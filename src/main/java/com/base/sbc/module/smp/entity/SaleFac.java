/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.smp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.enums.business.smp.SaleFacResultType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;

/**
 * 类描述：BI 物料投产
 *
 * @author KC
 * @version 1.0
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@TableName(value = "DM_SALE_FAC", autoResultMap = true)
@ApiModel("BI 物料投产 SaleFac")
public class SaleFac extends HashMap<String, BigDecimal> {

    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** id */
    @ApiModelProperty(value = "id")
    @TableId(value = "GUID", type = IdType.ASSIGN_UUID)
    private String id;

    /** 结果类型 */
    @ApiModelProperty(value = "结果类型")
    @TableField("RESULTTYPE")
    private SaleFacResultType resultType;

    /** 大货款 */
    @ApiModelProperty(value = "大货款")
    @TableField("PROD_CODE")
    private String bulkStyleNo;

    /** 线下销售标签 */
    @ApiModelProperty(value = "线下销售标签")
    @TableField("SALE_TYPE")
    private String offlineSaleFlag;

    /** 投产类型 */
    @ApiModelProperty(value = "投产类型")
    @TableField("ORDER_TYPE")
    private String productionType;

    /** 渠道类型 */
    @ApiModelProperty(value = "渠道类型")
    @TableField("CHANNEL_TYPE")
    private String channel;

    /** 品牌名 */
    @ApiModelProperty(value = "品牌名")
    @TableField("BRAND_NAME")
    private String brandName;

    /** 来源 */
    @ApiModelProperty(value = "来源")
    @TableField("SOURCE")
    private String source;

    /** 年份 */
    @ApiModelProperty(value = "年份")
    @TableField("YEARS")
    private Integer year;

    /** 月份 */
    @ApiModelProperty(value = "月份")
    @TableField("PERIOD_NAME")
    private YearMonth month;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
