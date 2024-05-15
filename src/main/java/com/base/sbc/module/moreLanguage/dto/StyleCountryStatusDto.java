package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.constant.MoreLanguageProperties;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StyleCountryStatusDto extends CountryDTO {

    private String bulkStyleNo;

    private List<TypeLanguageDto> typeLanguageDtoList;

    @JsonIgnore
    private Map<CountryLanguageType, StyleCountryStatusEnum> statusMap = new HashMap<>();

    public String getTagStatus() {
        return statusMap.getOrDefault(CountryLanguageType.TAG, StyleCountryStatusEnum.UNCHECK).getText();
    }
    public String getWashingStatus() {
        return statusMap.getOrDefault(CountryLanguageType.WASHING, StyleCountryStatusEnum.UNCHECK).getText();
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

