package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageExcelQueryDto {

    @ApiModelProperty(value = "国家语言")
    private String code;

    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @ApiModelProperty(value = "是否单语言")
    private YesOrNoEnum singleLanguageFlag;

    @ApiModelProperty(value = "吊牌类型")
    private CountryLanguageType type;

    @ApiModelProperty(value = "选择的标准列")
    private List<String> standardColumnCodeList;

    @ApiModelProperty(value = "是否修正缺失")
    private YesOrNoEnum showLack;

//    @NotEmpty(message = "请至少选择一个币种")
//    @ApiModelProperty(value = "选择的币种")
//    private String coinCode;

}
