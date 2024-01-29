package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    @ApiModelProperty(value = "国家语言code")
    private String code;

    @NotBlank(message = "请填写国家编码")
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    @NotBlank(message = "请填写国家")
    @ApiModelProperty(value = "国家名称")
    private String countryName;

}
