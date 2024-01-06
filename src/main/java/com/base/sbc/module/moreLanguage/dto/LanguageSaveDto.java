package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class LanguageSaveDto {

    @NotBlank(message = "请填写语言Code")
    @ApiModelProperty(value = "语言编码")
    private String countryLanguageId;

    @NotBlank(message = "请填写语言Code")
    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @NotBlank(message = "请填写语言")
    @ApiModelProperty(value = "语言名称")
    private String languageName;

    @NotBlank(message = "请填写语言")
    @ApiModelProperty(value = "语言名称")
    private CountryLanguageType countryLanguageType;

}
