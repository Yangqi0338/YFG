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
 * 类描述：物料库存-明细 实体类
 * @address com.base.sbc.module.purchase.entity.MaterialStockLog
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-13 15:44:18
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_material_stock_log")
@ApiModel("物料库存-明细 MaterialStockLog")
public class MaterialStockLog extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    public MaterialStockLog(){

    }

    public MaterialStockLog(WarehousingOrder order, WarehousingOrderDetail orderDetail, BasicsdatumMaterial material,
                            BigDecimal beforeQuality, BigDecimal quality, BigDecimal stockQuality){
        this.materialCode = material.getMaterialCode();
        this.relationCode = order.getCode();
        this.warehouseId  = order.getWarehouseId();
        this.warehouseName = order.getWarehouseName();
        this.agent = order.getAgentName();
        this.beforeQuality = beforeQuality;
        this.quality = quality;
        this.stockQuality = stockQuality;
    }

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 单据单号 */
    @ApiModelProperty(value = "单据单号"  )
    private String relationCode;
    /** 仓库id */
    @ApiModelProperty(value = "仓库id"  )
    private String warehouseId;
    /** 仓库名称 */
    @ApiModelProperty(value = "仓库名称"  )
    private String warehouseName;
    /** 出入库类型 */
    @ApiModelProperty(value = "出入库类型(0 入库, 1 出库)"  )
    private String type;
    /** 变动前数量 */
    @ApiModelProperty(value = "变动前数量"  )
    private BigDecimal beforeQuality;
    /** 变动数量 */
    @ApiModelProperty(value = "变动数量"  )
    private BigDecimal quality;
    /** 在库库存 */
    @ApiModelProperty(value = "在库库存"  )
    private BigDecimal stockQuality;
    /** 经办人 */
    @ApiModelProperty(value = "经办人"  )
    private String agent;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
