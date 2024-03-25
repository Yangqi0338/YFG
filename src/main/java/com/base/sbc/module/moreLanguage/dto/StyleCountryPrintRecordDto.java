package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StyleCountryPrintRecordDto extends CountryDTO {

    private String bulkStyleNo;

    private List<TypeLanguageDto> typeLanguageDtoList;

    @JsonIgnore
    private StyleCountryStatusEnum statusCode;

    public String getStatus() {
        return statusCode.getText();
    }

    public String getShowTypeInfo(){
        return typeLanguageDtoList.stream().map(typeLanguageDto->
                typeLanguageDto.getTypeName() + MoreLanguageProperties.fieldValueSeparator +
                        typeLanguageDto.getLanguageList().stream().map(LanguageSaveDto::getLanguageName)
                                .collect(Collectors.joining(MoreLanguageProperties.showInfoLanguageSeparator))
                + MoreLanguageProperties.multiSeparator +
                typeLanguageDto.getPrintTime()
        ).collect(Collectors.joining( MoreLanguageProperties.multiSeparator));
    }

}

