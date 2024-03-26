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
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

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
@EqualsAndHashCode(callSuper = true)
@Data
public class HangTagMoreLanguageBaseVO extends HangTagMoreLanguageSupportVO {

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /**
     * 国家语言编码
     */
    @ApiModelProperty(value = "国家语言编码")
    protected String code;

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
     * 是否展示
     */
    @ApiModelProperty(value = "是否展示")
    private YesOrNoEnum showFlag;

    /**
     * 国家名
     */
    @ApiModelProperty(value = "国家名")
    private String countryName;

    /**
     * 具体数据
     */
    @ApiModelProperty(value = "具体数据")
    private String propertiesName;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    public StyleCountryStatusEnum getAuditStatus(){
        // 内部有一个未审核,那就是未审核
        return getLanguageList().stream().anyMatch(it-> it.getAuditStatus() == StyleCountryStatusEnum.UNCHECK)
                 ? StyleCountryStatusEnum.UNCHECK : StyleCountryStatusEnum.CHECK;
    };

    protected String findStandardColumnName() {
        return (StrUtil.isNotBlank(this.standardColumnName) ? this.standardColumnName + MoreLanguageProperties.fieldValueSeparator: "");
    }

    /**
     * 全量数据
     */
    @ApiModelProperty(value = "全量数据")
    public String getSourceContent() {
        // 名字
        return MoreLanguageProperties.getMsg(CONTENT_FORMAT,
                findStandardColumnName(),
                this.isGroup ? MoreLanguageProperties.multiSeparator : "",
                Opt.ofNullable(this.propertiesName).orElse("")
        );
    }

}
