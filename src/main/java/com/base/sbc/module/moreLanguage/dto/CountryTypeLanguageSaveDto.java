package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class CountryTypeLanguageSaveDto extends CountryDTO {

    @NotEmpty(message = "至少选择一个类型")
    @ApiModelProperty(value = "语言编码")
    private List<TypeLanguageSaveDto> typeLanguage;


//    @NotEmpty(message = "请至少选择一个币种")
//    @ApiModelProperty(value = "选择的币种")
//    private String coinCode;

}
