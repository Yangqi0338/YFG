package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
public class CountryAddDto {

    @NotBlank(message = "请选择国家")
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    @NotBlank(message = "请选择语言")
    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @NotEmpty(message = "请至少选择一个标准列")
    @ApiModelProperty(value = "选择的标准列")
    private List<String> standardColumnCodeList;

}
