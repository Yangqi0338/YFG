/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：资料包-业务意见 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackBusinessOpinion
 * @email your email
 * @date 创建时间：2023-7-13 20:34:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_business_opinion")
@ApiModel("资料包-业务意见 PackBusinessOpinion")
public class PackBusinessOpinion extends BaseDataEntity<String> {

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
     * 资料包类型
     */
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    /**
     * 业务环节
     */
    @ApiModelProperty(value = "业务环节")
    private String step;
    /**
     * 提出人
     */
    @ApiModelProperty(value = "提出人")
    private String proposer;
    /**
     * 提出人id
     */
    @ApiModelProperty(value = "提出人id")
    private String proposerId;
    /**
     * 业务意见
     */
    @ApiModelProperty(value = "业务意见")
    private String content;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

