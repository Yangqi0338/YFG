package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.base.sbc.config.enums.business.CountryLanguageType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StyleCountryPrintRecordDto {

    private String bulkStyleNo;

    private String code;

    private String countryCode;

    private String countryName;

    private List<TypeLanguageDto> typeLanguageDtoList;

    public String getShowTypeInfo(){
        return typeLanguageDtoList.stream().map(typeLanguageDto->
                typeLanguageDto.getTypeName() + "：" +
                        typeLanguageDto.getLanguageList().stream().map(LanguageSaveDto::getLanguageName).collect(Collectors.joining("；"))
                +"\n" +
                typeLanguageDto.getPrintTime()
        ).collect(Collectors.joining("\n"));
    }

}

