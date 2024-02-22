/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：代理货品资料 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.style.entity.StyleColorAgent
 * @email your email
 * @date 创建时间：2024-2-20 14:45:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_color_agent")
@ApiModel("代理货品资料 StyleColorAgent")
public class StyleColorAgent extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 配色id
     */
    @ApiModelProperty(value = "配色id")
    private String styleColorId;
    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String styleColorNo;
    /**
     * 尺码id
     */
    @ApiModelProperty(value = "尺码id")
    private String sizeId;
    /**
     * 尺码code
     */
    @ApiModelProperty(value = "尺码code")
    private String sizeCode;
    /**
     * 外部颜色code
     */
    @ApiModelProperty(value = "外部颜色code")
    private String outsideColorCode;
    /**
     * 外部颜色名称
     */
    @ApiModelProperty(value = "外部颜色名称")
    private String outsideColorName;
    /**
     * 外部尺码code
     */
    @ApiModelProperty(value = "外部尺码code")
    private String outsideSizeCode;
    /**
     * 合作方条码
     */
    @ApiModelProperty(value = "合作方条码")
    private String outsideBarcode;
    /**
     * 状态（0：未发送，1：已发送，2：重新打开，-1：停用）冗余暂时不用
     */
    @ApiModelProperty(value = "状态（0：未发送，1：已发送，2：重新打开，-1：停用）冗余暂时不用")
    private String status;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
