package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import com.base.sbc.config.constant.Constants;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.base.sbc.config.constant.MoreLanguageProperties.MoreLanguageMsgEnum.TIME;


@Data
public class TypeLanguageDto {


    @ApiModelProperty(value = "国家语言类型")
    private CountryLanguageType type;
    public String getTypeName(){
        return type != null ? type.getText() : "";
    };

    private List<LanguageSaveDto> languageList;

    public String getPrintTime(){
        return languageList.stream().map(languageDto->
                getTypeName() + MoreLanguageProperties.getMsg(TIME) + MoreLanguageProperties.fieldValueSeparator
                        + Opt.ofNullable(DateUtil.format(languageDto.getPrintTime(), Constants.TIME_FORMAT)).orElse("")
        ).distinct().collect(Collectors.joining(MoreLanguageProperties.multiSeparator));
    }
}
