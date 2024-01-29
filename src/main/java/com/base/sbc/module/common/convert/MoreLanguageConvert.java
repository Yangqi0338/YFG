package com.base.sbc.module.common.convert;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBCSVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguagePrinterBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageWebBaseVO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.LanguageSaveDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusCountryDto;
import com.base.sbc.module.moreLanguage.dto.MoreLanguageStatusExcelResultDTO;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.moreLanguage.entity.StyleCountryStatus;
import com.base.sbc.module.standard.dto.StandardColumnSaveDto;
import com.base.sbc.module.standard.entity.StandardColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * {@code 描述：吊牌模块通用MapStruct转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Mapper(uses = {BaseConvert.class}, imports = {BaseSpringConvert.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoreLanguageConvert {
    MoreLanguageConvert INSTANCE = Mappers.getMapper(MoreLanguageConvert.class);

    List<LanguageSaveDto> copyList2Save(List<CountryLanguage> source);

    @Mappings({
            @Mapping(target = "countryLanguageId",source = "id"),
            @Mapping(target = "countryLanguageType",source = "type")
    })
    LanguageSaveDto copy2Save(CountryLanguage source);

    @Mappings({
            @Mapping(target = "model", ignore = true),
    })
    void copy2Entity(StandardColumnSaveDto source, @MappingTarget StandardColumn target);
    BasicsdatumSize copyMyself(BasicsdatumSize source);
    StandardColumnCountryTranslate copyMyself(StandardColumnCountryTranslate source);
    List<MoreLanguageStatusExcelResultDTO> copyList2ResultDTO(List<String> source);

    default MoreLanguageStatusExcelResultDTO copy2ResultDTO(String source) {
        return new MoreLanguageStatusExcelResultDTO(source);
    }

    List<MoreLanguageStatusCountryDto> copyList2CountryDTO(List<StyleCountryStatus> source);

    @Mappings({
            @Mapping(target = "statusCode", source = "status"),
            @Mapping(target = "time", source = "updateDate"),
            @Mapping(target = "person", source = "updateName"),
    })
    MoreLanguageStatusCountryDto copy2CountryDTO(StyleCountryStatus source);

}
