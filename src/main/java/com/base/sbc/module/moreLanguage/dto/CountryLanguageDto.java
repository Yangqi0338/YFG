package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CountryLanguageDto extends CountryLanguage {

    public String getTypeName(){
        return this.getType() != null ? this.getType().getText() : null;
    }

    public String getName(){
        return getCountryName() + "-" + getLanguageName();
    }

}
