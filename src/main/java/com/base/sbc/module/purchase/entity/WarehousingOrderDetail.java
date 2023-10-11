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
 * 类描述：入库单-明细 实体类
 * @address com.base.sbc.module.purchase.entity.WarehousingOrderDetail
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-10-11 11:10:39
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_warehousing_order_detail")
@ApiModel("入库单-明细 WarehousingOrderDetail")
public class WarehousingOrderDetail extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 入库单id */
    @ApiModelProperty(value = "入库单id"  )
    private String warehouseOrderId;
    /** 送货通知单id */
    @ApiModelProperty(value = "送货通知单id"  )
    private String noticeId;
    /** 采购单号 */
    @ApiModelProperty(value = "采购单号"  )
    private String purchaseCode;
    /** 采购单明细id */
    @ApiModelProperty(value = "采购单明细id"  )
    private String purchaseOrderDetailId;
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
    /** 物料规格编码 */
    @ApiModelProperty(value = "物料规格编码"  )
    private String materialSpecificationsCode;
    /** 款式名称 */
    @ApiModelProperty(value = "款式名称"  )
    private String styleName;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;
    /** 物料颜色编码 */
    @ApiModelProperty(value = "物料颜色编码"  )
    private String materialColorCode;
    /** 库位 */
    @ApiModelProperty(value = "库位"  )
    private String position;
    /** 库位编码 */
    @ApiModelProperty(value = "库位编码"  )
    private String positionCode;
    /** 预计采购数量 */
    @ApiModelProperty(value = "预计采购数量"  )
    private BigDecimal expectedPurchaseNum;
    /** 采购单价 */
    @ApiModelProperty(value = "采购单价"  )
    private BigDecimal price;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位"  )
    private String purchaseUnit;
    /** 采购单位编码 */
    @ApiModelProperty(value = "采购单位编码"  )
    private String purchaseUnitCode;
    /** 采购送货数量 */
    @ApiModelProperty(value = "采购送货数量"  )
    private BigDecimal deliveryQuantity;
    /** 库存单位 */
    @ApiModelProperty(value = "库存单位"  )
    private String stockUnit;
    /** 库存单位编码 */
    @ApiModelProperty(value = "库存单位编码"  )
    private String stockUnitCode;
    /** 采购收货数量 */
    @ApiModelProperty(value = "采购收货数量"  )
    private BigDecimal receivedQuantity;
    /** 金额 */
    @ApiModelProperty(value = "金额"  )
    private BigDecimal money;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColor;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 入库数量 */
    @ApiModelProperty(value = "入库数量"  )
    private BigDecimal warehouseNum;
    /** 实际金额 */
    @ApiModelProperty(value = "实际金额"  )
    private BigDecimal actualAmount;
    /** 送货金额 */
    @ApiModelProperty(value = "送货金额"  )
    private BigDecimal deliveryAmount;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

