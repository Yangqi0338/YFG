/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.business.StandardColumnModel;
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
     * 国家语言Id
     */
    @ApiModelProperty(value = "国家语言Id")
    protected String countryLanguageId;

    /**
     * 语言码
     */
    @ApiModelProperty(value = "语言码")
    protected String languageCode;

    /**
     * 语言名
     */
    @ApiModelProperty(value = "语言名")
    private String languageName;

    /**
     * 标准列翻译
     */
    @ApiModelProperty(value = "标准列翻译")
    private String standardColumnContent = "";

    /**
     * 不能找到标准列翻译
     */
    @ApiModelProperty(value = "不能找到标准列翻译")
    protected Boolean cannotFindStandardColumnContent = true;

    public Boolean getCannotFindStandardColumnContent() {
        return this.cannotFindStandardColumnContent && !isGroup;
    }

    /**
     * 具体数据的翻译
     */
    @ApiModelProperty(value = "具体数据的翻译")
    private String propertiesContent = "";

    /**
     * 不能找到数据翻译
     */
    @ApiModelProperty(value = "不能找到数据翻译")
    private Boolean cannotFindPropertiesContent = false;

    public Boolean getCannotFindPropertiesContent() {
        return this.cannotFindPropertiesContent && this.model != StandardColumnModel.TEXT;
    }

    /**
     * 标准列模型
     */
    @JsonIgnore
    @ApiModelProperty(value = "标准列模型")
    protected StandardColumnModel model;

    /**
     * 是组合的
     */
    @JsonIgnore
    @ApiModelProperty(value = "是组合的")
    protected Boolean isGroup = false;

    /**
     * 全量数据翻译
     */
    @ApiModelProperty(value = "全量数据翻译")
    public String getContent() {
        return String.format("%s%s %s",
                StrUtil.isNotBlank(this.standardColumnContent) ? this.standardColumnContent + ":": this.standardColumnContent,
                this.isGroup ? "\n" : "", this.propertiesContent);
    }

    @JsonIgnore
    protected Date createTime;
    @JsonIgnore
    protected Date updateTime;

}
