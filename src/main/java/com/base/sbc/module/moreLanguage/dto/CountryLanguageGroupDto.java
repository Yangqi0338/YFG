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
public class CountryLanguageGroupDto extends CountryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 国家编码 */
    @ApiModelProperty(value = "语言编码"  )
    private String languageCode;
    /** 国家名称 */
    @ApiModelProperty(value = "语言名称"  )
    private String languageName;

    public String getName(){
        return StrUtil.isBlank(getLanguageName()) ? getCountryName() : getLanguageName();
    }

    private List<CountryLanguageGroupDto> languageList;

}
