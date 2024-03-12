/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.vo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.Constants.COMMA;
import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.*;

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
public class HangTagMoreLanguageBCSVO {

    /**
     * 大货款号
     */
    @ApiModelProperty(value = "大货款号")
    private String bulkStyleNo;

    /**
     * 国家语言Id
     */
    @ApiModelProperty(value = "国家语言Id")
    private String countryCode;

    /**
     * 国家语言名字
     */
    @ApiModelProperty(value = "国家语言名字")
    private String countryName;

    /**
     * 成功列表
     */
    @ApiModelProperty(value = "成功列表")
    @JsonIgnore
    private List<HangTagMoreLanguageBCSChildrenBaseVO> successList;

    /**
     * 失败列表
     */
    @ApiModelProperty(value = "失败列表")
    @JsonIgnore
    private List<HangTagMoreLanguageBCSChildrenBaseVO> failureList;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    public String getFailureMessage() {
        StringJoiner message = new StringJoiner(MoreLanguageProperties.multiSeparator);
        message.setEmptyValue("");
        // 仅获取错误列表
        if (CollectionUtil.isNotEmpty(this.failureList)) {
            // 先根据款号分组
            this.failureList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageBCSChildrenBaseVO::getBulkStyleNo))
                    .forEach((bulkStyleNo, sameBulkStyleNoList)-> {
                        // 再拼装所有的错误信息
                        message.add(bulkStyleNo + MoreLanguageProperties.fieldValueSeparator +
                                sameBulkStyleNoList.stream().map(HangTagMoreLanguageBCSChildrenBaseVO::getPrinterCheckMessage)
                                        .distinct().collect(Collectors.joining(MoreLanguageProperties.multiSeparator)));
                    });
        }
        return message.toString();
    }

    public HangTagMoreLanguageBCSVO(List<HangTagMoreLanguageBCSChildrenBaseVO> childrenList, boolean cnCheck) {
        HangTagMoreLanguageBCSChildrenBaseVO groupChildrenVO = childrenList.get(0);
        this.countryCode = groupChildrenVO.getCountryCode();
        this.countryName = groupChildrenVO.getCountryName();
        this.bulkStyleNo = childrenList.stream().map(HangTagMoreLanguageBaseVO::getBulkStyleNo).distinct().collect(Collectors.joining(COMMA));
        // 是否中文语言检查
        childrenList.forEach(it-> it.setCnCheck(cnCheck));
        // 看是否存在错误信息,将其分到正确或错误列表
        childrenList.stream().collect(Collectors.groupingBy(it-> StrUtil.isBlank(it.getPrinterCheckMessage()))).forEach((isSuccess,successFailureList)-> {
            if (isSuccess) {
                this.successList = successFailureList;
            }else {
                this.failureList = successFailureList;
            }
        });
    }

    public HangTagMoreLanguageBCSVO(List<HangTagMoreLanguageBCSChildrenBaseVO> childrenList) {
        // 默认不检查中文
        this(childrenList, MoreLanguageProperties.internalCheck);
    }

    public static class HangTagMoreLanguageBCSChildrenBaseVO extends HangTagMoreLanguageBaseVO {
        /**
         * 打印系统专门检查信息
         */
        @ApiModelProperty(value = "打印系统专门检查信息")
        public String getPrinterCheckMessage(){
            if (MoreLanguageProperties.notCheckStandardColumnCode.contains(this.getStandardColumnCode())) return "";
            boolean languageEmpty = CollectionUtil.isEmpty(this.getLanguageList());
            // 如果连语言都没
            if (languageEmpty) {
                // 检查是否创建了国家语言
                if (StrUtil.isBlank(this.getCode())) return MoreLanguageProperties.getMsg(NOT_INSERT, Opt.ofNullable(this.getCountryName()).orElse(""));
                // 检查是否有标准列, 款号肯定有, 所以只能判断下级设置是否成功
                if (StrUtil.isBlank(this.getStandardColumnId()) || StrUtil.isBlank(this.getStandardColumnCode())) return MoreLanguageProperties.getMsg(HAVEN_T_TAG, this.getBulkStyleNo());
                // 检查是否存在语种
                if (!hasLanguage) return MoreLanguageProperties.getMsg(HAVEN_T_LANGUAGE,Opt.ofNullable(this.getCountryName()).orElse(""));
            } else {
                CountryLanguageType countryLanguageType = CountryLanguageType.findByStandardColumnType(this.type);
                String typeText = Opt.ofNullable(countryLanguageType).map(CountryLanguageType::getText).orElse("");
                StringJoiner message = new StringJoiner(MoreLanguageProperties.checkItemSeparator,typeText + this.getStandardColumnName(),"").setEmptyValue("");
                this.getLanguageList().forEach(language-> {
                    StringJoiner languageMsg = new StringJoiner(MoreLanguageProperties.checkMsgItemSeparator);
                    // 是否强制判断语言为中文,不进行校验
                    if (!MoreLanguageProperties.internalLanguageCode.equals(language.getLanguageCode()) || cnCheck) {
                        if (language.getCannotFindStandardColumnContent()) languageMsg.add(MoreLanguageProperties.getMsg(HAVEN_T_CONTENT, FIELD));
                        if (language.getCannotFindPropertiesContent())  languageMsg.add(MoreLanguageProperties.getMsg(HAVEN_T_CONTENT, CONTENT));
                        if (languageMsg.length() > 0) {
                            message.add(language.getLanguageName() + languageMsg);
                        }
                    }
                });
                return message.toString();
            }
            return "";
        }
    }

}
