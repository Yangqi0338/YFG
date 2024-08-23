/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.smp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 类描述：BI 物料投产
 *
 * @author KC
 * @version 1.0
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@TableName(value = "DM_STOCK_SIZE", autoResultMap = true)
@ApiModel("BI 物料投产 SaleFac")
public class StockSize implements Serializable {

    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** id */
    @ApiModelProperty(value = "id")
    @TableId(value = "GUID", type = IdType.ASSIGN_UUID)
    private String id;

    /** 大货款 */
    @ApiModelProperty(value = "大货款")
    @TableField("GOODSNO")
    private String bulkStyleNo;

    /** 尺码名 */
    @ApiModelProperty(value = "尺码名")
    @TableField("SIZES")
    private String sizeName;

    /** 入库数量 */
    @ApiModelProperty(value = "入库数量")
    @TableField("STO_QTY")
    private Integer qty;

    /** 入库时间 */
    @ApiModelProperty(value = "入库时间")
    @TableField("STOCK_DATE")
    private LocalDate date;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
