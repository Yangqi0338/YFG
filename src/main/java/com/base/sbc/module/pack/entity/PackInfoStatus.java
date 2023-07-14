/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 类描述：资料包-状态 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackInfoStatus
 * @email your email
 * @date 创建时间：2023-7-14 11:36:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_info_status")
@ApiModel("资料包-状态 PackInfoStatus")
public class PackInfoStatus extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)")
    private String packType;
    /**
     * 状态:1启用,0未启用
     */
    @ApiModelProperty(value = "状态:1启用,0未启用")
    private String enableFlag;
    /**
     * SCM下发状态:0未下发,1已下发
     */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;
    /**
     * bom状态:(0样品,1大货)
     */
    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    private String bomStatus;
    /**
     * 审核状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "审核状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String confirmStatus;
    /**
     * 反审状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "反审状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String reverseConfirmStatus;
    /**
     * 设计转后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "设计转后技术确认:(0未确认,1已确认)")
    private String designTechConfirm;
    /**
     * 设计转后技术确认时间
     */
    @ApiModelProperty(value = "设计转后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designTechConfirmDate;
    /**
     * 大货制单员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货制单员确认:(0未确认,1已确认)")
    private String bulkOrderClerkConfirm;
    /**
     * 大货制单员确认时间
     */
    @ApiModelProperty(value = "大货制单员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkOrderClerkConfirmDate;
    /**
     * 大货工艺员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货工艺员确认:(0未确认,1已确认)")
    private String bulkProdTechConfirm;
    /**
     * 大货工艺员确认时间
     */
    @ApiModelProperty(value = "大货工艺员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkProdTechConfirmDate;
    /**
     * 后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "后技术确认:(0未确认,1已确认)")
    private String postTechConfirm;
    /**
     * 后技术确认时间
     */
    @ApiModelProperty(value = "后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postTechConfirmDate;
    /**
     * 尺寸表锁定flg:(0未锁定,1锁定)
     */
    @ApiModelProperty(value = "尺寸表锁定flg:(0未锁定,1锁定)")
    private String sizeLockFlag;
    /**
     * 工艺说明锁定flg:(0未锁定,1锁定)
     */
    @ApiModelProperty(value = "工艺说明锁定flg:(0未锁定,1锁定)")
    private String techSpecLockFlag;
    /**
     * 工艺说明审批状态:待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "工艺说明审批状态:待审核(1)、审核通过(2)、被驳回(-1)")
    private String techSpecConfirmStatus;
    /**
     * 尺寸表洗后尺寸跳码:(0关闭,1开启)
     */
    @ApiModelProperty(value = "尺寸表洗后尺寸跳码:(0关闭,1开启)")
    private String washSkippingFlag;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

