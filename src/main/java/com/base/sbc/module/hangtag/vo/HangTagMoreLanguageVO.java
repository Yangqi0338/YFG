/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import com.base.sbc.config.enums.business.StandardColumnModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * 标准列Id
     */
    private String standardColumnId;

    /**
     * 标准列码
     */
    @ApiModelProperty(value = "标准列码")
    private String standardColumnCode;

    /**
     * 标准列名
     */
    @ApiModelProperty(value = "标准列名")
    private String standardColumnName;

    /**
     * 标准列翻译
     */
    @ApiModelProperty(value = "标准列翻译")
    private String standardColumnContent;

    /**
     * 不能找到标准列翻译
     */
    @ApiModelProperty(value = "不能找到标准列翻译")
    private Boolean cannotFindStandardColumnContent = true;

    /**
     * 标准列模型
     */
    @ApiModelProperty(value = "标准列模型")
    private StandardColumnModel model;

    /**
     * 查数据库的编码
     */
    @ApiModelProperty(value = "查数据库的编码")
    private String tableCode;

    /**
     * 仅model为radio时使用
     */
    @ApiModelProperty(value = "仅model为radio时使用")
    private String tableName;

    /**
     * 国家编码
     */
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    /**
     * 国家名
     */
    @ApiModelProperty(value = "国家名")
    private String countryName;

    /**
     * 语言码
     */
    @ApiModelProperty(value = "语言码")
    private String languageCode;

    /**
     * 语言名
     */
    @ApiModelProperty(value = "语言名")
    private String languageName;

    /**
     * 具体数据的编码
     */
    @ApiModelProperty(value = "具体数据的编码")
    private String propertiesCode;

    /**
     * 具体数据
     */
    @ApiModelProperty(value = "具体数据")
    private String propertiesName;

    /**
     * 具体数据的翻译
     */
    @ApiModelProperty(value = "具体数据的翻译")
    private String propertiesContent;

    /**
     * 是否是温馨提示
     */
    @ApiModelProperty(value = "是否是温馨提示")
    private Boolean isWarnTips = false;

    /**
     * 不能找到数据翻译
     */
    @ApiModelProperty(value = "不能找到数据翻译")
    private Boolean cannotFindPropertiesContent = true;

    /**
     * 是组合的
     */
    @ApiModelProperty(value = "是组合的")
    @JsonIgnore
    private Boolean isGroup = false;

    /**
     * 全量数据
     */
    @ApiModelProperty(value = "全量数据")
    public String getSourceContent() {
        return String.format("%s:%s %s", standardColumnName, isGroup ? "\n" : "", propertiesName);
    }

    /**
     * 全量数据翻译
     */
    @ApiModelProperty(value = "全量数据翻译")
    public String getContent() {
        return String.format("%s:%s %s", standardColumnContent, isGroup ? "\n" : "", propertiesContent);
    }



    private String createId;
    private String createName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String updateId;
    private String updateName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
