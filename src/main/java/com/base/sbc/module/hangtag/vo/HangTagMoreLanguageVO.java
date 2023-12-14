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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 类描述：吊牌表 实体类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.entity.HangTag
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HangTagMoreLanguageVO extends HangTagMoreLanguageSupportVO {

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

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
    protected Boolean cannotFindStandardColumnContent = true;

    public Boolean getCannotFindStandardColumnContent() {
        return this.cannotFindStandardColumnContent && !isGroup;
    }

    /**
     * 国家名
     */
    @ApiModelProperty(value = "国家名")
    private String countryName;

    /**
     * 语言名
     */
    @ApiModelProperty(value = "语言名")
    private String languageName;

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
     * 不能找到数据翻译
     */
    @ApiModelProperty(value = "不能找到数据翻译")
    private Boolean cannotFindPropertiesContent = false;

    public Boolean getCannotFindPropertiesContent() {
        return this.cannotFindPropertiesContent && this.model != StandardColumnModel.TEXT;
    }

    /**
     * 全量数据
     */
    @ApiModelProperty(value = "全量数据")
    public String getSourceContent() {
        return String.format("%s:%s %s", this.standardColumnName, this.isGroup ? "\n" : "", this.propertiesName);
    }

    /**
     * 全量数据翻译
     */
    @ApiModelProperty(value = "全量数据翻译")
    public String getContent() {
        return String.format("%s:%s %s", this.standardColumnContent, this.isGroup ? "\n" : "", this.propertiesContent);
    }

}
