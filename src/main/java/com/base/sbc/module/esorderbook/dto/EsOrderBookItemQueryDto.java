/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：ES订货本明细QueryDto 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.dto.EsOrderBookItemQueryDto
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@Data
public class EsOrderBookItemQueryDto extends Page {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 头表id
     */
    @ApiModelProperty(value = "头表id")
    private String headId;
    /**
     * 款式配色id
     */
    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    /**
     * 是否锁定（0：否，1：是）
     */
    @ApiModelProperty(value = "是否锁定（0：否，1：是）")
    private String isLock;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
