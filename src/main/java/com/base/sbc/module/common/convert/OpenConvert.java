package com.base.sbc.module.common.convert;

import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageCheckDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageDTO;
import com.base.sbc.module.hangtag.dto.HangTagMoreLanguageSystemDTO;
import com.base.sbc.module.moreLanguage.dto.CountryQueryDto;
import com.base.sbc.module.moreLanguage.dto.LanguageSaveDto;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
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
public interface OpenConvert {
    OpenConvert INSTANCE = Mappers.getMapper(OpenConvert.class);

    HangTagMoreLanguageCheckDTO copy2Check(HangTagMoreLanguageSystemDTO source);
    HangTagMoreLanguageDTO copy2MoreLanguageDTO(HangTagMoreLanguageSystemDTO source);
    CountryQueryDto copy2CountryQuery(HangTagMoreLanguageSystemDTO source);

    default CountryLanguageType getTypeByCsvIndex(Integer index) {
        if (index == 1) {
            return CountryLanguageType.WASHING;
        }else if (index == 2) {
            return CountryLanguageType.TAG;
        }else {
            throw new OtherException("非法类型");
        }
    }

}
