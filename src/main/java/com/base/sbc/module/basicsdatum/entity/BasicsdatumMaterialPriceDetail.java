/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-物料档案-供应商报价- 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialPriceDetail
 * @author your name
 * @email your email
 * @date 创建时间：2023-7-27 17:53:40
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_material_price_detail")
@ApiModel("基础资料-物料档案-供应商报价- BasicsdatumMaterialPriceDetail")
public class BasicsdatumMaterialPriceDetail extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /** 实体主键 */
    @TableId(type = IdType.ASSIGN_ID)
    protected String id;

    /** 公司编码 */
    @TableField(fill = FieldFill.INSERT)
    protected String companyCode;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 报价id */
    @ApiModelProperty(value = "报价id"  )
    private String priceId;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String width;
    /** 采购报价 */
    @ApiModelProperty(value = "采购报价"  )
    private BigDecimal quotationPrice;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String colorName;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String widthName;
    /** 供应商料号 */
    @ApiModelProperty(value = "供应商料号"  )
    private String supplierMaterialCode;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

