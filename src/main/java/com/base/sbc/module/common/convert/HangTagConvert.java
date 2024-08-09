package com.base.sbc.module.common.convert;

import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.entity.HangTag;
import com.base.sbc.module.hangtag.vo.HangTagListVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBCSVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageWebBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagVO;
import com.base.sbc.module.hangtag.vo.MoreLanguageHangTagVO;
import com.base.sbc.module.moreLanguage.dto.CountryLanguageDto;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
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
    HangTagMoreLanguageVO copy2MoreLanguageVO(CountryLanguageDto source);
    @Mappings({
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "standardColumnCode", source = "code"),
            @Mapping(target = "standardColumnName", source = "name"),
            @Mapping(target = "standardColumnId", source = "id"),
    })
    void standardColumn2MoreLanguageBaseVO(StandardColumn source, @MappingTarget HangTagMoreLanguageBaseVO target);
    HangTagMoreLanguageBaseVO copyMyself(HangTagMoreLanguageBaseVO source);
    @Mappings({
            @Mapping(target = "mySelfList", ignore = true),
    })
    MoreLanguageTagPrinting copyMyself(MoreLanguageTagPrinting source);
    HangTagMoreLanguageWebBaseVO copyMyself(HangTagMoreLanguageWebBaseVO source);
    List<HangTagMoreLanguageVO> copyMyself(List<HangTagMoreLanguageVO> languageList);
    HangTagMoreLanguageVO copyMyself(HangTagMoreLanguageVO source);
    CountryQueryDto copy2CountryQuery(HangTagMoreLanguageDTO source);
    HangTagMoreLanguageBaseVO copy2MoreLanguageBaseVO(CountryLanguageDto source);
    @Mappings({
            @Mapping(target = "createTime", source = "createDate"),
            @Mapping(target = "updateTime", source = "updateDate"),
            @Mapping(target = "propertiesCode", ignore = true),
    })
    void countryTranslate2MoreLanguageVO(StandardColumnCountryTranslate source, @MappingTarget HangTagMoreLanguageVO target);

    MoreLanguageHangTagVO copy2MoreLanguage(HangTagVO source);
    HangTag copy2Entity(HangTagListVO source);
    List<HangTag> copyList2Entity(List<HangTagListVO> source);
    List<HangTagListVO> copyList2ListVO(List<HangTag> source);
    List<MoreLanguageHangTagVO> copyList2MoreLanguage(List<HangTagVO> source);
    List<MoreLanguageHangTagVO> copyList2MoreLanguageList(List<HangTagListVO> source);
    MoreLanguageTagPrinting copy2MoreLanguage(TagPrinting source);
    MoreLanguageTagPrinting.Size copy2MoreLanguage(TagPrinting.Size source);
    List<MoreLanguageTagPrinting.Size> copyList2MoreLanguageSize(List<TagPrinting.Size> source);
}
