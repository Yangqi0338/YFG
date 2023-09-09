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
 * 类描述：送货通知单 实体类
 * @address com.base.sbc.module.purchase.entity.DeliveryNotice
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-9 9:15:56
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_delivery_notice")
@ApiModel("送货通知单 DeliveryNotice")
public class DeliveryNotice extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /** 款式名称 */
    @TableField(exist = false)
    private String styleName;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 状态（0正常 1作废） */
    @ApiModelProperty(value = "状态（0正常 1作废）"  )
    private String status;
    /** 采购单id */
    @ApiModelProperty(value = "采购单id"  )
    private String purcahseId;
    /** 采购单号 */
    @ApiModelProperty(value = "采购单号"  )
    private String purchaseCode;
    /** 采购单明细id */
    @ApiModelProperty(value = "采购单明细id"  )
    private String purchaseOrderDetailId;
    /** 外部单号 */
    @ApiModelProperty(value = "外部单号"  )
    private String externalCode;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 采购员id */
    @ApiModelProperty(value = "采购员id"  )
    private String purchaserId;
    /** 采购员名称 */
    @ApiModelProperty(value = "采购员名称"  )
    private String purchaserName;
    /** 采购日期 */
    @ApiModelProperty(value = "采购日期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date purchaseDate;
    /** 物料交期 */
    @ApiModelProperty(value = "物料交期"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryDate;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designStyleCode;
    /** 制版号 */
    @ApiModelProperty(value = "制版号"  )
    private String plateBillCode;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 物料规格 */
    @ApiModelProperty(value = "物料规格"  )
    private String materialSpecifications;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;
    /** 预计采购数量 */
    @ApiModelProperty(value = "预计采购数量"  )
    private BigDecimal expectedPurchaseNum;
    /** 采购单价 */
    @ApiModelProperty(value = "采购单价"  )
    private BigDecimal price;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位"  )
    private String purchaseUnit;
    /** 采购送货数量 */
    @ApiModelProperty(value = "采购送货数量"  )
    private BigDecimal deliveryQuantity;
    /** 采购收货数量 */
    @ApiModelProperty(value = "采购收货数量"  )
    private BigDecimal receivedQuantity;
    /** 金额 */
    @ApiModelProperty(value = "金额"  )
    private BigDecimal money;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColor;
    /** 采购转库存 */
    @ApiModelProperty(value = "采购转库存"  )
    private BigDecimal convertUnitRatio;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

