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
public class MoreLanguageExcelQueryDto {

    @NotBlank(message = "缺少国家语言")
    @ApiModelProperty(value = "国家语言")
    private String countryLanguageId;

    @ApiModelProperty(value = "选择的标准列")
    private List<String> standardColumnCodeList;

//    @NotEmpty(message = "请至少选择一个币种")
//    @ApiModelProperty(value = "选择的币种")
//    private String coinCode;

}
