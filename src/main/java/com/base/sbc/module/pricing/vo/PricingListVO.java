/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.pricing.enums.PricingSourceTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：核价
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:17
 */
@Data
@ApiModel("核价")
public class PricingListVO extends BaseDataEntity<String> {

    private static final long serialVersionUID = -4920262612455280732L;

    /**
     * 明细维度查询时该字段是核价颜色id
     */
    @ApiModelProperty(value = "明细维度查询时该字段是核价颜色id")
    private String id;

    /**
     * 核价id
     */
    @ApiModelProperty(value = "核价id")
    private String pricingId;
    /**
     * 报价日期
     */
    @ApiModelProperty(value = "报价日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date quoteDate;

    /**
     * 审核状态：0：未提交，1：待审核，2：审核通过，3：审核不通过）
     */
    @ApiModelProperty(value = "审核状态：0：未提交，1：待审核，2：审核通过，-1：审核不通过）")
    private String confirmStatus;

    /**
     * 来源类型
     *
     * @see PricingSourceTypeEnum
     */
    @ApiModelProperty(value = "来源类型")
    private String sourceType;
    /**
     * 单号
     */
    @ApiModelProperty(value = "单号")
    private String code;

    /**
     * 设计款号
     */
    @ApiModelProperty(value = "设计款号")
    private String designStyleCode;

    /**
     * 来源id
     */
    @ApiModelProperty(value = "来源id")
    private String sourceId;
    /**
     * 来源单号
     */
    @ApiModelProperty(value = "来源单号")
    private String sourceCode;

    /**
     * 款号
     */
    @ApiModelProperty(value = "款号")
    private String styleCode;
    /**
     * 款式名称
     */
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String stylePic;

    /**
     * 客款号
     */
    @ApiModelProperty(value = "客款号")
    private String customerStyleCode;

    /**
     * 版本号(核价次数)
     */
    @ApiModelProperty(value = "版本号(核价次数)")
    private String version;

    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String category;

    @ApiModelProperty(value = "颜色")
    private String colors;

    /**
     * 物料费用
     */
    @ApiModelProperty(value = "物料费用")
    private BigDecimal materialCost;
    /**
     * 加工费
     */
    @ApiModelProperty(value = "加工费")
    private BigDecimal processCost;
    /**
     * 加工费倍率
     */
    @ApiModelProperty(value = "加工费倍率")
    private BigDecimal processCostRate;
    /**
     * 二次加工费用
     */
    @ApiModelProperty(value = "二次加工费用")
    private BigDecimal secondaryProcessingCost;
    /**
     * 其他费用
     */
    @ApiModelProperty(value = "其他费用")
    private BigDecimal otherCost;

// TODO 管理倍率？
    // todo 利润？
    // todo 纯成本？
    // todo 吊牌价？

    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private BigDecimal orderNum;


    /**
     * 报价人
     */
    @ApiModelProperty(value = "报价人")
    private String quoteUserId;
    /**
     * 报价人名称
     */
    @ApiModelProperty(value = "报价人名称")
    private String quoteUserName;

    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 货币2
     */
    @ApiModelProperty(value = "货币2")
    private String otherCurrency;

    /**
     * 报价说明
     */
    @ApiModelProperty(value = "报价说明")
    private String quoteDesc;

    /**
     * 审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    private String confirmId;
    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String confirmName;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmDate;
    /**
     * 审核原因
     */
    @ApiModelProperty(value = "审核原因")
    private String confirmReason;
    /**
     * 编辑
     */
    @ApiModelProperty(value = "编辑")
    private Integer isEdit = 0;
}

