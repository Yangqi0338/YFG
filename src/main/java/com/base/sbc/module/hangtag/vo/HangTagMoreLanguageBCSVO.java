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
        StringJoiner message = new StringJoiner("\n");
        message.setEmptyValue("");
        if (CollectionUtil.isNotEmpty(this.failureList)) {
            this.failureList.stream().collect(Collectors.groupingBy(HangTagMoreLanguageBCSChildrenBaseVO::getBulkStyleNo))
                    .forEach((bulkStyleNo, sameBulkStyleNoList)-> {
                        message.add(bulkStyleNo + ": " +
                                sameBulkStyleNoList.stream().map(HangTagMoreLanguageBCSChildrenBaseVO::getPrinterCheckMessage).distinct().collect(Collectors.joining("\n")));
                    });
        }
        return message.toString();
    }

    public HangTagMoreLanguageBCSVO(List<HangTagMoreLanguageBCSChildrenBaseVO> childrenList, boolean cnCheck) {
        HangTagMoreLanguageBCSChildrenBaseVO groupChildrenVO = childrenList.get(0);
        this.countryCode = groupChildrenVO.getCountryCode();
        this.countryName = groupChildrenVO.getCountryName();
        this.bulkStyleNo = childrenList.stream().map(HangTagMoreLanguageBaseVO::getBulkStyleNo).distinct().collect(Collectors.joining(","));
        childrenList.forEach(it-> it.setCnCheck(cnCheck));
        childrenList.stream().collect(Collectors.groupingBy(it-> StrUtil.isBlank(it.getPrinterCheckMessage()))).forEach((isSuccess,successFailureList)-> {
            if (isSuccess) {
                this.successList = successFailureList;
            }else {
                this.failureList = successFailureList;
            }
        });
    }

    public HangTagMoreLanguageBCSVO(List<HangTagMoreLanguageBCSChildrenBaseVO> childrenList) {
        this(childrenList, MoreLanguageProperties.internalCheck);
    }

    public static class HangTagMoreLanguageBCSChildrenBaseVO extends HangTagMoreLanguageBaseVO {
        /**
         * 打印系统专门检查信息
         */
        @ApiModelProperty(value = "打印系统专门检查信息")
        public String getPrinterCheckMessage(){

            boolean languageEmpty = CollectionUtil.isEmpty(this.getLanguageList());
            if (languageEmpty) {
                if (StrUtil.isBlank(this.getCode())) return "PDM未创建" + Opt.ofNullable(this.getCountryName()).orElse("") + "国家语言翻译";
                if (StrUtil.isBlank(this.getStandardColumnId()) || StrUtil.isBlank(this.getStandardColumnCode())) return "不存在吊牌信息";
                if (!hasLanguage) return Opt.ofNullable(this.getCountryName()).orElse("") +"不存在该语种";
            } else {
                String messageFormat = "%s未翻译";
                CountryLanguageType countryLanguageType = CountryLanguageType.findByStandardColumnType(this.type);
                String typeText = Opt.ofNullable(countryLanguageType).map(CountryLanguageType::getText).orElse("");
                StringJoiner message = new StringJoiner("/",typeText + this.getStandardColumnName(),"").setEmptyValue("");
                this.getLanguageList().forEach(language-> {
                    StringJoiner languageMsg = new StringJoiner("、");
                    // 是否强制判断语言为中文,不进行校验
                    if (!MoreLanguageProperties.internalLanguageCode.equals(language.getLanguageCode()) || cnCheck) {
                        if (language.getCannotFindStandardColumnContent()) languageMsg.add(String.format(messageFormat, "字段"));
                        if (language.getCannotFindPropertiesContent())  languageMsg.add(String.format(messageFormat, "内容"));
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
