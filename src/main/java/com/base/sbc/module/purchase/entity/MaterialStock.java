/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：物料库存 实体类
 * @address com.base.sbc.module.purchase.entity.MaterialStock
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-13 15:44:13
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_material_stock")
@ApiModel("物料库存 MaterialStock")
public class MaterialStock extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    public MaterialStock(){}

    public MaterialStock(WarehousingOrder order, WarehousingOrderDetail orderDetail, BasicsdatumMaterial material){
        this.imgUrl = material.getImageUrl();
        this.materialCode = material.getMaterialCode();
        this.materialName = material.getMaterialName();
        this.materialSku = material.getMaterialCode() + orderDetail.getMaterialColorCode() + orderDetail.getMaterialSpecificationsCode();
        this.unit = orderDetail.getPurchaseUnit();
        this.materialSpecifications = orderDetail.getMaterialSpecifications();
        this.materialSpecificationsCode = orderDetail.getMaterialSpecificationsCode();
        this.materialColor = orderDetail.getMaterialColor();
        this.materialColorCode = orderDetail.getMaterialColorCode();
        this.defaultSupplier = orderDetail.getSupplierName();
        this.defaultSupplierId = orderDetail.getSupplierId();
        this.position = orderDetail.getPosition();
        this.positionCode = orderDetail.getPositionCode();
        this.warehouseId = order.getWarehouseId();
        this.warehouseName = order.getWarehouseName();
    }
	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 图片路径 */
    @ApiModelProperty(value = "图片路径"  )
    private String imgUrl;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 物料SKU */
    @ApiModelProperty(value = "物料SKU"  )
    private String materialSku;
    /** 单位 */
    @ApiModelProperty(value = "单位"  )
    private String unit;
    /** 物料规格 */
    @ApiModelProperty(value = "物料规格"  )
    private String materialSpecifications;
    /** 物料规格编码 */
    @ApiModelProperty(value = "物料规格编码"  )
    private String materialSpecificationsCode;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;
    /** 物料颜色编码 */
    @ApiModelProperty(value = "物料颜色编码"  )
    private String materialColorCode;
    /** 库存数量 */
    @ApiModelProperty(value = "库存数量"  )
    private BigDecimal stockQuantity;
    /** 默认供应商 */
    @ApiModelProperty(value = "默认供应商"  )
    private String defaultSupplier;
    /** 默认供应商id */
    @ApiModelProperty(value = "默认供应商id"  )
    private String defaultSupplierId;
    /** 库位 */
    @ApiModelProperty(value = "库位"  )
    private String position;
    /** 库位编码 */
    @ApiModelProperty(value = "库位编码"  )
    private String positionCode;
    /** 仓库id */
    @ApiModelProperty(value = "仓库id"  )
    private String warehouseId;
    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称"  )
    private String warehouseName;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
