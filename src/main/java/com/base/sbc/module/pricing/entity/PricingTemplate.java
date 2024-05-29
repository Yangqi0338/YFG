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
/**
 * 类描述：核价模板 实体类
 * @address com.base.sbc.module.pricing.entity.PricingTemplate
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:21:Pricing
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pricing_template")
@ApiModel("核价模板 PricingTemplate")
public class PricingTemplate extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 模板编码 */
    @ApiModelProperty(value = "模板编码"  )
    private String templateCode;
    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    private String templateName;
    /**
     * 状态(0)停用,(1)启用
     */
    @ApiModelProperty(value = "状态(0)停用,(1)启用")
    private String status;

    /** 模板名称 */
    @ApiModelProperty(value = "模板名称"  )
    private String devtType;

    /** 生产类型名称 */
    @ApiModelProperty(value = "生产类型名称"  )
    private String devtTypeName;

    /** 是否默认0.否,1.是 */
    @ApiModelProperty(value = "是否默认0.否,1.是"  )
    private String defaultFlag;

    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;

    /** 品牌名 */
    @ApiModelProperty(value = "品牌名"  )
    private String brandName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

