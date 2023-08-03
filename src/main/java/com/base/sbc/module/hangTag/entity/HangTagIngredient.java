/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：吊牌成分表 实体类
 * @address com.base.sbc.module.hangTag.entity.HangTagIngredient
 * @author xhj
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_hang_tag_ingredient")
@ApiModel("吊牌成分表 HangTagIngredient")
public class HangTagIngredient extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 吊牌id */
    @ApiModelProperty(value = "吊牌id"  )
    private String hangTagId;
    /** 类型编码 */
    @ApiModelProperty(value = "类型编码"  )
    private String typeCode;
    /** 类型 */
    @ApiModelProperty(value = "类型"  )
    private String type;
    /** 百分比 */
    @ApiModelProperty(value = "百分比"  )
    private BigDecimal percentage;
    /**
     * 成分说明
     *
     */
    @ApiModelProperty(value = "成分说明")
    private String ingredientDescription;
    /**
     * 成分说明编码
     *
     */
    @ApiModelProperty(value = "成分说明编码")
    private String ingredientDescriptionCode;
    /**
     * 成分备注
     *
     */
    @ApiModelProperty(value = "成分备注")
    private String descriptionRemarks;
    /**
     * 成分备注编码
     *
     */
    @ApiModelProperty(value = "成分备注编码")
    private String descriptionRemarksCode;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

