/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：样衣明细 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.Sample
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample_item")
@ApiModel("样衣明细 SampleItem")
public class SampleItem extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 样衣主键ID */
    @ApiModelProperty(value = "样衣主键ID")
    private String sampleId;

    /** 颜色ID */
    @ApiModelProperty(value = "颜色ID")
    private String colorId;

    /** 颜色 */
    @ApiModelProperty(value = "颜色")
    private String color;

    /** 尺码ID */
    @ApiModelProperty(value = "尺码ID")
    private String sizeId;

    /** 尺码 */
    @ApiModelProperty(value = "尺码")
    private String size;

    /** 数量 */
    @ApiModelProperty(value = "数量")
    private Integer count;

    /** 借出数量 */
    @ApiModelProperty(value = "借出数量")
    private Integer borrowCount;

    /** 价格 */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /** 编码 */
    @ApiModelProperty(value = "编码")
    private String code;

    /** 位置ID */
    @ApiModelProperty(value = "位置ID")
    private String positionId;

    /** 位置 */
    @ApiModelProperty(value = "位置")
    private String position;

    /** 来源：1-新增，2-导入，3-外部 */
    @ApiModelProperty(value = "来源：1-新增，2-导入，3-外部")
    private String fromType;

    /** 状态：0-未入库，1-在库，2-借出，3-删除 */
    @ApiModelProperty(value = "状态：0-未入库，1-在库，2-借出，3-删除，4-售出, 5-盘点中")
    private String status;

    /** 入仓时间 */
    @ApiModelProperty(value = "入仓时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date storeDate;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /** 是否启用：0.否，1.是 */
    @ApiModelProperty(value = "是否启用：0.否，1.是")
    private String enableFlag;
}

