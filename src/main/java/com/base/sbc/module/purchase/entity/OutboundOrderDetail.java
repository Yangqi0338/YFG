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
 * 类描述：出库单-明细 实体类
 * @address com.base.sbc.module.purchase.entity.OutboundOrderDetail
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-18 15:21:51
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_outbound_order_detail")
@ApiModel("出库单-明细 OutboundOrderDetail")
public class OutboundOrderDetail extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 出库单id */
    @ApiModelProperty(value = "出库单id"  )
    private String outboundId;
    /** 来源单id */
    @ApiModelProperty(value = "来源单id"  )
    private String sourceId;
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
    /** 规格 */
    @ApiModelProperty(value = "规格"  )
    private String specifications;
    /** 颜色 */
    @ApiModelProperty(value = "颜色"  )
    private String color;
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
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

