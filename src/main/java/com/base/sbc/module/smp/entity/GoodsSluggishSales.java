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
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.enums.business.smp.SluggishSaleWeekendsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：BI 物料评分
 *
 * @author KC
 * @version 1.0
 * @email kchange0915@gmail.com
 * @date 创建时间：2024-6-13 15:15:25
 */
@Data
@TableName(value = "DM_GOODS_SLUGGISH_SALES")
@ApiModel("BI 物料评分 GoodsSluggishSales")
public class GoodsSluggishSales implements Serializable {

    private static final long serialVersionUID = 1L;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** id */
    @ApiModelProperty(value = "id")
    @TableField("GUID")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 货号 */
    @ApiModelProperty(value = "货号")
    @TableField("GOODS_NO")
    private String bulkStyleNo;

    /** 年份 */
    @ApiModelProperty(value = "年份")
    @TableField("YEAR_ID")
    private Integer year;

    /** 周数 */
    @ApiModelProperty(value = "周数")
    @TableField("SLUGGISH_SALES_TYPE")
    private SluggishSaleWeekendsType weekends;

    /** 店均件 */
    @ApiModelProperty(value = "店均件")
    @TableField("SLUGGISH_SALES_VALUE")
    private BigDecimal avg;

    /** 等级 */
    @ApiModelProperty(value = "等级")
    @TableField("SLUGGISH_SALES_DESC")
    private String level;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    /** 删除标记 */
    @ApiModelProperty(value = "删除标记")
    @TableField("DEL_FLAG")
    @TableLogic(value = "0", delval = "1")
    private String delFlag;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
