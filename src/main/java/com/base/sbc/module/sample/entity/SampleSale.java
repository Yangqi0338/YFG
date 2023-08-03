/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：样衣销售 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.SampleSale
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_sale")
@ApiModel("样衣销售 SampleSale")
public class SampleSale extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 企业编码 */
    @ApiModelProperty(value = "企业编码")
    private String companyCode;

    /** 编号 */
    @ApiModelProperty(value = "单号")
    private String code;

    /** 销售时间 */
    @ApiModelProperty(value = "销售时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date saleDate;

    /** 业务员ID */
    @ApiModelProperty(value = "业务员ID")
    private String saleId;

    /** 业务员 */
    @ApiModelProperty(value = "业务员")
    private String saleName;

    /** 客户类型：1-内部人员，2-供应商 */
    @ApiModelProperty(value = "客户类型：1-内部人员，2-供应商")
    private Integer custmerType;

    /** 客户ID */
    @ApiModelProperty(value = "客户ID")
    private String custmerId;

    /** 客户 */
    @ApiModelProperty(value = "客户")
    private String custmerName;

    /** 总金额 */
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    /** 总数量 */
    @ApiModelProperty(value = "总数量")
    private Integer totalCount;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

