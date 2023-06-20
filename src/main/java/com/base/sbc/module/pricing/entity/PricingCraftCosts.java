/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.entity;
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
 * 类描述：核价工艺费用 实体类
 * @address com.base.sbc.module.pricing.entity.PricingCraftCosts
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:22
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pricing_craft_costs")
@ApiModel("核价工艺费用 PricingCraftCosts")
public class PricingCraftCosts extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 报价单编码 */
    @ApiModelProperty(value = "报价单编码"  )
    private String pricingCode;
    /** 工艺名称 */
    @ApiModelProperty(value = "工艺名称"  )
    private String craftName;
    /** 工艺id */
    @ApiModelProperty(value = "工艺id"  )
    private String craftId;
    /** 是否委外0.否, 1.是 */
    @ApiModelProperty(value = "是否委外0.否, 1.是"  )
    private String outsource;
    /** 加工商id */
    @ApiModelProperty(value = "加工商id"  )
    private String processorId;
    /** 加工商名称 */
    @ApiModelProperty(value = "加工商名称"  )
    private String processorName;
    /** 币种 */
    @ApiModelProperty(value = "币种"  )
    private String currency;
    /** 报价货币 */
    @ApiModelProperty(value = "报价货币"  )
    private String quotationPriceCurrency;
    /** 汇率 */
    @ApiModelProperty(value = "汇率"  )
    private BigDecimal exchangeRate;
    /** 报价单价 */
    @ApiModelProperty(value = "报价单价"  )
    private BigDecimal quotationPrice;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
