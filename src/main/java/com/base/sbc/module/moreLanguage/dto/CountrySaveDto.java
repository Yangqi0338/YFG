package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
public class CountrySaveDto {

    @ApiModelProperty(value = "国家语言id")
    private String countryLanguageId;

    @NotBlank(message = "请填写国家编码")
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    @NotBlank(message = "请填写国家")
    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @NotBlank(message = "请填写语言Code")
    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @NotBlank(message = "请填写语言")
    @ApiModelProperty(value = "语言名称")
    private String languageName;

    @NotEmpty(message = "请至少选择一个标准列")
    @ApiModelProperty(value = "选择的标准列")
    private List<String> standardColumnCodeList;

//    @NotEmpty(message = "请至少选择一个币种")
//    @ApiModelProperty(value = "选择的币种")
//    private String coinCode;

}
