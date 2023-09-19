/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.entity;
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
 * 类描述：采购申请单-明细 实体类
 * @address com.base.sbc.module.purchase.entity.PurchaseRequestDetail
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-18 16:53:33
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_purchase_request_detail")
@ApiModel("采购申请单-明细 PurchaseRequestDetail")
public class PurchaseRequestDetail extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 采购申请单id */
    @ApiModelProperty(value = "采购申请单id"  )
    private String requestId;
    /** 图片路径 */
    @ApiModelProperty(value = "图片路径"  )
    private String imgUrl;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料分类 */
    @ApiModelProperty(value = "物料分类"  )
    private String materialType;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String component;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColor;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;
    /** 物料颜色编码 */
    @ApiModelProperty(value = "物料颜色编码"  )
    private String materialColorCode;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /** 申请数量 */
    @ApiModelProperty(value = "申请数量"  )
    private BigDecimal requestNum;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位"  )
    private String unit;
    /** 物料规格 */
    @ApiModelProperty(value = "物料规格"  )
    private String materialSpecifications;
    /** 物料规格编码 */
    @ApiModelProperty(value = "物料规格编码"  )
    private String materialSpecificationsCode;
    /** 损耗 */
    @ApiModelProperty(value = "损耗"  )
    private BigDecimal loss;
    /** 需求交期 */
    @ApiModelProperty(value = "需求交期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date needDate;
    /** 使用部位 */
    @ApiModelProperty(value = "使用部位"  )
    private String usePosition;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

