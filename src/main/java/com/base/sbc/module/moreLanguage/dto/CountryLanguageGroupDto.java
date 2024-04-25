package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CountryLanguageGroupDto extends CountryDTO {
    private List<CountryLanguageGroupDto> languageList;
}
