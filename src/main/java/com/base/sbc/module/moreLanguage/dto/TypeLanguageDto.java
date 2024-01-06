package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.enums.business.CountryLanguageType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class TypeLanguageDto {


    @ApiModelProperty(value = "国家语言类型")
    private CountryLanguageType type;
    public String getTypeName(){
        return type != null ? type.getText() : "";
    };

    private List<LanguageSaveDto> languageList;

}
