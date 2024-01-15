package com.base.sbc.module.common.convert;

import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBCSVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguagePrinterBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageWebBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.base.sbc.module.moreLanguage.entity.StandardColumnCountryTranslate;
import com.base.sbc.module.smp.entity.TagPrinting;
import com.base.sbc.module.standard.entity.StandardColumn;
import com.base.sbc.open.dto.MoreLanguageTagPrinting;
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
public interface HangTagConvert {
    HangTagConvert INSTANCE = Mappers.getMapper(HangTagConvert.class);

    List<HangTagMoreLanguageWebBaseVO> copyList2Web(List<HangTagMoreLanguageBaseVO> source);
    List<HangTagMoreLanguageBCSVO.HangTagMoreLanguageBCSChildrenBaseVO> copyList2Bcs(List<HangTagMoreLanguageBaseVO> source);
    List<HangTagMoreLanguagePrinterBaseVO> copyList2Print(List<HangTagMoreLanguageBaseVO> source);
    List<HangTagMoreLanguageVO> copyList2MoreLanguageVO(List<CountryLanguage> source);
    HangTagMoreLanguageVO copy2MoreLanguageVO(CountryLanguage source);
    @Mappings({
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "standardColumnCode", source = "code"),
            @Mapping(target = "standardColumnName", source = "name"),
            @Mapping(target = "standardColumnId", source = "id"),
    })
    void standardColumn2MoreLanguageBaseVO(StandardColumn source, @MappingTarget HangTagMoreLanguageBaseVO target);
    HangTagMoreLanguageBaseVO copyMyself(HangTagMoreLanguageBaseVO source);
    CountryQueryDto copy2CountryQuery(HangTagMoreLanguageDTO source);
    HangTagMoreLanguageBaseVO copy2MoreLanguageBaseVO(CountryLanguage source);
    void countryTranslate2MoreLanguageVO(StandardColumnCountryTranslate source, @MappingTarget HangTagMoreLanguageVO target);

    MoreLanguageHangTagVO copy2MoreLanguage(HangTagVO source);
    List<MoreLanguageHangTagVO> copyList2MoreLanguage(List<HangTagVO> source);
    MoreLanguageTagPrinting copy2MoreLanguage(TagPrinting source);
}
