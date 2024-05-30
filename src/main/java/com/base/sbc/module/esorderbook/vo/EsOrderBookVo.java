/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.esorderbook.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：ES订货本Vo 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.esorderbook.vo.EsOrderBookVo
 * @email your email
 * @date 创建时间：2024-3-28 16:21:15
 */
@Data
@ApiModel("ES订货本 EsOrderBookVo")
public class EsOrderBookVo extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 组名
     */
    @ApiModelProperty(value = "组名")
    private String name;
    /**
     * 搭配图片
     */
    @ApiModelProperty(value = "搭配图片")
    private String img;
    /**
     * 产品季节id
     */
    @ApiModelProperty(value = "产品季节id")
    private String seasonId;
    /**
     * 产品季节名称
     */
    @ApiModelProperty(value = "产品季节名称")
    private String seasonName;
    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channel;
    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    /**
     * 状态:0:未提交,1:待确认,2:已确认,3:已下单,4:已驳回
     */
    @ApiModelProperty(value = "状态:0:未提交,1:待确认,2:已确认,3:已下单,4:已驳回")
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
