/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：采购申请单 实体类
 * @address com.base.sbc.module.purchase.entity.PurchaseRequest
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-18 16:53:27
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_purchase_request")
@ApiModel("采购申请单 PurchaseRequest")
public class PurchaseRequest extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @TableField(exist = false)
    private List<PurchaseRequestDetail> detailList;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 采购申请单号 */
    @ApiModelProperty(value = "采购申请单号"  )
    private String code;
    /** 单据状态（0 正常 1作废） */
    @ApiModelProperty(value = "单据状态（0 正常 1作废）"  )
    private String orderStatus;
    /** 状态（0草稿 1待审核 2审核通过 -1驳回） */
    @ApiModelProperty(value = "状态（0草稿 1待审核 2审核通过 -1驳回）"  )
    private String status;
    /** 采购申请单名称 */
    @ApiModelProperty(value = "采购申请单名称"  )
    private String orderName;
    /** 需求交期 */
    @ApiModelProperty(value = "需求交期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date needDate;
    /** 总金额 */
    @ApiModelProperty(value = "总金额"  )
    private BigDecimal totalAmount;
    /** 审核人 */
    @ApiModelProperty(value = "审核人"  )
    private String reviewer;
    /** 审核时间 */
    @ApiModelProperty(value = "审核时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewDate;
    /** 驳回理由 */
    @ApiModelProperty(value = "驳回理由"  )
    private String rejectReason;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

