/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@Data
public class HangTagMoreLanguageVO {

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String standardColumnCode;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String standardColumnName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String standardColumnTranslateContent;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String model;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String countryCode;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String countryName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String languageCode;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String languageName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String propertiesCode;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String propertiesName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String tableCode;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String tableName;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String propertiesContent;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private String content;

    private String createid;
    private String createName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String updateid;
    private String updateName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
