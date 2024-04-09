/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.HangTagStatusEnum;
import com.base.sbc.config.enums.business.StandardColumnModel;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCheckDetailAuditDTO;
import com.base.sbc.open.dto.MoreLanguageTagPrinting;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class HangTagMoreLanguageSupportVO {

    /**
     * 标准列Id
     */
    @JsonIgnore
    private String standardColumnId;

    /**
     * 标准列模型
     */
    @JsonIgnore
    @ApiModelProperty(value = "标准列模型")
    protected StandardColumnModel model;

    /**
     * 标准列模型
     */
    @JsonIgnore
    @ApiModelProperty(value = "标准列模型")
    protected StandardColumnType type;

    /**
     * 国家类型
     */
    @JsonEnumDefaultValue
    @ApiModelProperty(value = "国家类型")
    protected CountryLanguageType countryLanguageType;

    /**
     * 查数据库的编码
     */
    @JsonIgnore
    @ApiModelProperty(value = "查数据库的编码")
    protected String tableCode;

    /**
     * 仅model为radio时使用
     */
    @JsonIgnore
    @ApiModelProperty(value = "仅model为radio时使用")
    protected String tableName;

    /**
     * 具体数据的编码
     */
    @JsonIgnore
    @ApiModelProperty(value = "具体数据的编码")
    protected String propertiesCode;

    /**
     * 是组合的
     */
    @JsonIgnore
    @ApiModelProperty(value = "是组合的")
    protected Boolean isGroup = false;

    /**
     * 中文检查
     */
    @JsonIgnore
    @ApiModelProperty(value = "中文检查")
    protected Boolean cnCheck = true;

    /**
     * 吊牌状态
     */
    @JsonIgnore
    @ApiModelProperty(value = "吊牌状态")
    private HangTagStatusEnum status;

    /**
     * 分类列表
     */
    @ApiModelProperty(value = "分类列表")
    private List<HangTagMoreLanguageVO> languageList = new ArrayList<>();

    /**
     * 有语言的
     */
    @JsonIgnore
    @ApiModelProperty(value = "有语言的")
    protected Boolean hasLanguage = true;

}
