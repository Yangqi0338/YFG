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

import java.util.Date;

/**
 * 类描述：样衣 实体类
 * @version 1.0
 * @address com.base.sbc.module.sample.entity.Sample
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sample")
@ApiModel("样衣 Sample")
public class Sample extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;

    /** 图片 */
    @ApiModelProperty(value = "图片。多图英文‘,’逗号分隔")
    private String images;

    /** 打版管理ID */
    @ApiModelProperty(value = "打版管理ID")
    private String patternMakingId;

    /** 打版编号 */
    @ApiModelProperty(value = "打版编号")
    private String patternMakingCode;

    /** 样衣版 */
    @ApiModelProperty(value = "样衣版")
    private String samplaType;

    /** 纸样师ID */
    @ApiModelProperty(value = "纸样师ID")
    private String patternDesignId;

    /** 纸样师名称 */
    @ApiModelProperty(value = "纸样师名称")
    private String patternDesignName;

    /** 客款号 */
    @ApiModelProperty(value = "客款号")
    private String customerNo;

    /** 样衣设计ID */
    @ApiModelProperty(value = "样衣设计ID")
    private String sampleDesignId;

    /** 款式名称 */
    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /** 设计款号 */
    @ApiModelProperty(value = "设计款号")
    private String designNo;

    /** 款式品类 */
    @ApiModelProperty(value = "款式品类")
    private String categoryName;

    /** 款式品类Ids */
    @ApiModelProperty(value = "款式品类Ids")
    private String categoryIds;

    /** 季节ID */
    @ApiModelProperty(value = "季节ID")
    private String seasonId;

    /** 季节 */
    @ApiModelProperty(value = "季节")
    private String season;

    /** 设计来源类型：1-内部人员，2-供应商 */
    @ApiModelProperty(value = "设计来源类型：1-内部人员，2-供应商")
    private Integer fromType;

    /** 设计来源ID */
    @ApiModelProperty(value = "设计来源ID")
    private String fromId;

    /** 设计来源名称 */
    @ApiModelProperty(value = "设计来源名称")
    private String fromName;

    /** 样衣类型：1-内部研发，2-外采，2-ODM提供 */
    @ApiModelProperty(value = "样衣类型：1-内部研发，2-外采，2-ODM提供")
    private Integer type;

    /** 状态：0-禁用，1-启用，2-删除 */
    @ApiModelProperty(value = "状态：0-禁用，1-启用，2-删除")
    private Integer status;

    /** 库存状态：0-完全借出，1-部分借出，2-全部在库 */
    @ApiModelProperty(value = "库存状态：0-完全借出，1-部分借出，2-全部在库")
    private Integer completeStatus;

    /** 审核人ID */
    @ApiModelProperty(value = "审核人ID")
    private String examineId;

    /** 审核人 */
    @ApiModelProperty(value = "审核人")
    private String examineName;

    /** 审核时间 */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examineDate;

    /** 审核状态：0-草稿，1-待审核、2-审核通过、3-驳回 */
    @ApiModelProperty(value = "审核状态：0-草稿，1-待审核、2-审核通过、3-驳回")
    private Integer examineStatus;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remarks;
}

