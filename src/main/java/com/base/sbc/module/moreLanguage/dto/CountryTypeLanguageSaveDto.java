package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
public class CountryTypeLanguageSaveDto {

    @ApiModelProperty(value = "国家语言code")
    private String code;

    @NotBlank(message = "请填写国家编码")
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    @NotBlank(message = "请填写国家")
    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @NotEmpty(message = "至少选择一个类型")
    @ApiModelProperty(value = "语言编码")
    private List<TypeLanguageSaveDto> typeLanguage;


//    @NotEmpty(message = "请至少选择一个币种")
//    @ApiModelProperty(value = "选择的币种")
//    private String coinCode;

}
