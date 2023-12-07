package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Slf4j
@Component
public class
DataVerifyResultVO {

    /** 可能为空字符串,表示中国 */
    @NotNull(message = "国家语言不能为空")
    @ApiModelProperty(value = "查询国家语言条件标签Id")
    private String countryLanguageId;


    @ApiModelProperty(value = "标准列码条件标签Code")
    private String standardColumnCode;

}

