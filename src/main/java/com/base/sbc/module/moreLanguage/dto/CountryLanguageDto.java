package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.lang.Opt;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.module.moreLanguage.entity.CountryLanguage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CountryLanguageDto extends CountryLanguage {


    private String languageName;


    private String modelLanguageName;

    public String getTypeName(){
        return this.getType() != null ? this.getType().getText() : null;
    }

    public void buildLanguageName(List<BasicBaseDict> dictList){
        Opt.ofBlankAble(this.getLanguageCode()).map(languageCode->
                dictList.stream().filter(it-> it.getValue().equals(languageCode)).findFirst().orElse(new BasicBaseDict())
        ).ifPresent(dict-> {
            this.languageName = dict.getName();
        });
        Opt.ofBlankAble(this.getModelLanguageCode()).map(languageCode->
                dictList.stream().filter(it-> it.getValue().equals(languageCode)).findFirst().orElse(new BasicBaseDict())
        ).ifPresent(dict-> {
            this.modelLanguageName = dict.getName();
        });

    }

}
