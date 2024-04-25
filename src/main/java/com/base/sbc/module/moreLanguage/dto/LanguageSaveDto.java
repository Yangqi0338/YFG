package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class LanguageSaveDto {

    @NotBlank(message = "请填写语言Code")
    @ApiModelProperty(value = "语言编码")
    private String countryLanguageId;

    @NotBlank(message = "请填写语言Code")
    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @ApiModelProperty(value = "名称")
    @JsonIgnore
    private String name;

    @NotBlank(message = "请填写语言")
    @ApiModelProperty(value = "语言名称")
    private String languageName;

    public String getLanguageName(){
        if (StrUtil.isNotBlank(languageName)) return languageName;
        return Opt.ofBlankAble(name).map(it-> name.split("-")[0]).orElse("");
    };

    @NotBlank(message = "请填写语言")
    @ApiModelProperty(value = "语言名称")
    private CountryLanguageType countryLanguageType;

    @DateTimeFormat("yyyy/MM/dd hh:mm:ss")
    private Date printTime;

}
