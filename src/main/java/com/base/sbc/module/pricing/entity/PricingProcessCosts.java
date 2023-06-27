/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：加工费用 实体类
 * @address com.base.sbc.module.pricing.entity.PricingProcessCosts
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:31
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pricing_process_costs")
@ApiModel("加工费用 PricingProcessCosts")
public class PricingProcessCosts extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 报价单单号 */
    @ApiModelProperty(value = "报价单单号"  )
    private String pricingCode;
    /** 序号 */
    @ApiModelProperty(value = "序号"  )
    private String sort;
    /** 部位 */
    @ApiModelProperty(value = "部位"  )
    private String part;
    /** 工序名称 */
    @ApiModelProperty(value = "工序名称"  )
    private String processName;
    /** 工序等级 */
    @ApiModelProperty(value = "工序等级"  )
    private String processLevel;
    /** 末节点 */
    @ApiModelProperty(value = "末节点"  )
    private String finallyNode;
    /** GST工价 */
    @ApiModelProperty(value = "GST工价"  )
    private BigDecimal gstProcessPrice;
    /** 倍率 */
    @ApiModelProperty(value = "倍率"  )
    private BigDecimal magnification;
    /** 倍数 */
    @ApiModelProperty(value = "倍数"  )
    private BigDecimal multiple;
    /** IE工价 */
    @ApiModelProperty(value = "IE工价"  )
    private BigDecimal ieProcessPrice;
    /** 标准工时(秒) */
    @ApiModelProperty(value = "标准工时(秒)"  )
    private BigDecimal processDate;
    /** 最高标准工价 */
    @ApiModelProperty(value = "最高标准工价"  )
    private BigDecimal maxProcePrice;
    /** 货币 */
    @ApiModelProperty(value = "货币"  )
    private String currency;
    /** 报价单价 */
    @ApiModelProperty(value = "报价单价"  )
    private BigDecimal quotationPrice;
    /** 报价货币 */
    @ApiModelProperty(value = "报价货币"  )
    private String quotationPriceCurrency;
    /** 工价 */
    @ApiModelProperty(value = "工价"  )
    private BigDecimal processPrice;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
