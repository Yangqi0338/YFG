package com.base.sbc.module.purchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("出库单明细")
public class OutBoundOrderDetailVo {
    /** id */
    @ApiModelProperty(value = "id"  )
    private String id;
    /** 出库单id */
    @ApiModelProperty(value = "出库单id"  )
    private String outboundId;
    /** 来源单id */
    @ApiModelProperty(value = "来源单id"  )
    private String sourceId;
    /** 物料SKU */
    @ApiModelProperty(value = "物料SKU"  )
    private String materialSku;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String styleNo;
    /** 制版号 */
    @ApiModelProperty(value = "制版号"  )
    private String plateBillCode;
    /** 库存单位 */
    @ApiModelProperty(value = "库存单位"  )
    private String stockUnit;
    /** 库存单位名称 */
    @ApiModelProperty(value = "库存单位名称"  )
    private String stockUnitName;
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String specifications;
    /** 规格编码 */
    @ApiModelProperty(value = "规格编码"  )
    private String specificationsCode;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
    /** 颜色编码 */
    @ApiModelProperty(value = "颜色编码"  )
    private String colorCode;
    /** 库存单价 */
    @ApiModelProperty(value = "库存单价"  )
    private BigDecimal stockPrice;
    /** 需求数 */
    @ApiModelProperty(value = "需求数"  )
    private BigDecimal needNum;
    /** 出库数 */
    @ApiModelProperty(value = "出库数"  )
    private BigDecimal outNum;
    /** 已出库数 */
    @ApiModelProperty(value = "已出库数"  )
    private BigDecimal outboundNum;
    /** 库位 */
    @ApiModelProperty(value = "库位"  )
    private String position;
    /** 库位编码 */
    @ApiModelProperty(value = "库位编码"  )
    private String positionCode;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 库存数量 */
    @ApiModelProperty(value = "库存数量"  )
    private BigDecimal stockQuantity;
    /** 锁定库存 */
    @ApiModelProperty(value = "锁定库存"  )
    private BigDecimal lockQuantity;
    /** 可用库存 */
    @ApiModelProperty(value = "可用库存"  )
    private BigDecimal availableQuantity;
    /** 已配数 */
    @ApiModelProperty(value = "已配数"  )
    private BigDecimal readyNum;
}
