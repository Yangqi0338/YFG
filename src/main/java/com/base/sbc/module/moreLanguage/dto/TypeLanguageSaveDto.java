package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.enums.business.CountryLanguageType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class TypeLanguageSaveDto {

    @NotNull(message = "请至少选择一个类型")
    @ApiModelProperty(value = "国家语言类型")
    private CountryLanguageType type;
    private String getTypeName(){
        return type != null ? type.getText() : "";
    };

    @ApiModelProperty(value = "号型语言编码")
    private String modelLanguageCode;

    @NotEmpty(message = "请至少选择一个标准列")
    @ApiModelProperty(value = "选择的标准列")
    private List<String> standardColumnCodeList;

    @NotEmpty(message = "请至少选择一个语言")
    private List<String> languageCodeList;

}
