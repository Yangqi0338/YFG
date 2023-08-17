/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.entity;
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
 * 类描述：采购-采购单 实体类
 * @address com.base.sbc.module.review.entity.PurchaseOrder
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-17 15:31:32
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_purchase_order")
@ApiModel("采购-采购单 PurchaseOrder")
public class PurchaseOrder extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 采购单号 */
    @ApiModelProperty(value = "采购单号"  )
    private String code;
    /** 单据状态（0 正常 1作废） */
    @ApiModelProperty(value = "单据状态（0 正常 1作废）"  )
    private String orderStatus;
    /** 状态（0草稿 1待审核 2审核通过 -1驳回） */
    @ApiModelProperty(value = "状态（0草稿 1待审核 2审核通过 -1驳回）"  )
    private String status;
    /** 下发状态（0未下发 1已下发） */
    @ApiModelProperty(value = "下发状态（0未下发 1已下发）"  )
    private String distributeStatus;
    /** 入库状态（0未入库 1入库中 2已入库） */
    @ApiModelProperty(value = "入库状态（0未入库 1入库中 2已入库）"  )
    private String warehouseStatus;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 供应商联系人 */
    @ApiModelProperty(value = "供应商联系人"  )
    private String supplierContacts;
    /** 供应商电话 */
    @ApiModelProperty(value = "供应商电话"  )
    private String supplierPhone;
    /** 供应商地址 */
    @ApiModelProperty(value = "供应商地址"  )
    private String supplierAddress;
    /** 采购员id */
    @ApiModelProperty(value = "采购员id"  )
    private String purchaserId;
    /** 采购员名称 */
    @ApiModelProperty(value = "采购员名称"  )
    private String purchaserName;
    /** 入库仓库id */
    @ApiModelProperty(value = "入库仓库id"  )
    private String warehouseId;
    /** 入库仓库 */
    @ApiModelProperty(value = "入库仓库"  )
    private String warehouseName;
    /** 仓库联系人 */
    @ApiModelProperty(value = "仓库联系人"  )
    private String warehouseContacts;
    /** 仓库联系人电话 */
    @ApiModelProperty(value = "仓库联系人电话"  )
    private String warehousePhone;
    /** 仓库地址 */
    @ApiModelProperty(value = "仓库地址"  )
    private String warehouseAddress;
    /** 采购日期 */
    @ApiModelProperty(value = "采购日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date purchaseDate;
    /** 交货日期 */
    @ApiModelProperty(value = "交货日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryDate;
    /** 总数量 */
    @ApiModelProperty(value = "总数量"  )
    private BigDecimal total;
    /** 总金额 */
    @ApiModelProperty(value = "总金额"  )
    private BigDecimal totalAmount;
    /** 审核人 */
    @ApiModelProperty(value = "审核人"  )
    private String reviewer;
    /** 审核时间 */
    @ApiModelProperty(value = "审核时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewDate;
    /** 驳回理由 */
    @ApiModelProperty(value = "驳回理由"  )
    private String rejectReason;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
