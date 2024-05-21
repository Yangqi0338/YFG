package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.enums.business.CountryLanguageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    @ApiModelProperty(value = "国家语言code")
    private String code;

//    @NotBlank(message = "请填写国家编码")
//    @ApiModelProperty(value = "国家编码")
//    private String countryCode;

    @ApiModelProperty(value = "名称")
    private String name;

    @JsonIgnore
    private Map<CountryLanguageType, List<String>> languageCodeTypeMap;

}
