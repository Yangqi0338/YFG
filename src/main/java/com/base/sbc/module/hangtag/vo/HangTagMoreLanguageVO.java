/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailAuditDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.CONTENT_FORMAT;

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


    @ApiModelProperty(value = "具体数据的编码")
    protected String propertiesCode;

    /**
     * 标准列翻译
     */
    @ApiModelProperty(value = "标准列翻译")
    private String standardColumnContent = "";

    public String findStandardColumnContent() {
        return getCannotFindStandardColumnContent() || StrUtil.isBlank(this.standardColumnContent) ? "" : this.standardColumnContent;
    }

    /**
     * 不能找到标准列翻译
     */
    @ApiModelProperty(value = "不能找到标准列翻译")
    protected Boolean cannotFindStandardColumnContent = true;

    /**
     * 具体数据的翻译
     */
    @ApiModelProperty(value = "具体数据的翻译")
    public String propertiesContent = "";

    /**
     * 具体数据翻译
     */
    @ApiModelProperty(value = "具体数据翻译")
    public String getPropertiesContent() {
        return (getCannotFindPropertiesContent() && !isGroup) || StrUtil.isBlank(this.propertiesContent) ? "" : this.propertiesContent;
    }

    /**
     * 不能找到数据翻译
     */
    @ApiModelProperty(value = "不能找到数据翻译")
    private Boolean cannotFindPropertiesContent = true;

    public Boolean getCannotFindPropertiesContent() {
        // 若类型是文本,则直接为已翻译
        return forceFindContent() && this.cannotFindPropertiesContent;
    }

    @ApiModelProperty(value = "标准列模型")
    protected StandardColumnModel model;

    @JsonIgnore
    public Boolean forceFindContent(){
        return this.model != StandardColumnModel.TEXT;
    }

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
        String title = findStandardColumnContent() + MoreLanguageProperties.fieldValueSeparator;
        String value = getPropertiesContent().trim();
        return MoreLanguageProperties.getMsg(CONTENT_FORMAT,
                title,
                this.isGroup ? MoreLanguageProperties.multiSeparator : "",
                StrUtil.endWith(value, MoreLanguageProperties.showInfoLanguageSeparator) ? value : value + MoreLanguageProperties.showInfoLanguageSeparator
        );
    }

    @JsonIgnore
    protected Date createTime;
    @JsonIgnore
    protected Date updateTime;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    @JsonIgnore
    protected StyleCountryStatusEnum contentAuditStatus = StyleCountryStatusEnum.UNCHECK;

    @JsonIgnore
    protected StyleCountryStatusEnum titleAuditStatus = StyleCountryStatusEnum.UNCHECK;

    public StyleCountryStatusEnum getAuditStatus(){
        return (!forceFindContent() || contentAuditStatus == StyleCountryStatusEnum.CHECK) && titleAuditStatus == StyleCountryStatusEnum.CHECK
                ? StyleCountryStatusEnum.CHECK
                : StyleCountryStatusEnum.UNCHECK;
    }

    public List<MoreLanguageStatusCheckDetailAuditDTO> buildAuditList(String standardColumnCode, String titleCode){
        List<MoreLanguageStatusCheckDetailAuditDTO> auditDtoList = new ArrayList<>();
        if (forceFindContent()) {
            MoreLanguageStatusCheckDetailAuditDTO contentStatus = new MoreLanguageStatusCheckDetailAuditDTO();
            contentStatus.setStandardColumnCode(standardColumnCode);
            contentStatus.setSource(this.getPropertiesCode());
            contentStatus.setContent(this.getPropertiesContent());
            contentStatus.setStatus(YesOrNoEnum.YES.getValueStr());
            auditDtoList.add(contentStatus);
        }

        MoreLanguageStatusCheckDetailAuditDTO titleStatus = new MoreLanguageStatusCheckDetailAuditDTO();
        titleStatus.setStandardColumnCode(titleCode);
        titleStatus.setSource(standardColumnCode);
        titleStatus.setContent(this.getStandardColumnContent());
        titleStatus.setStatus(YesOrNoEnum.YES.getValueStr());
        auditDtoList.add(titleStatus);

        return auditDtoList;
    }

}
