package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CountryQueryDto extends Page {

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "编码")
    private CountryLanguageType type;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "语种编码")
    private String languageCode;

    @ApiModelProperty(value = "停用标识")
    private YesOrNoEnum enableFlag;

    @ApiModelProperty(value = "根据编码分组")
    private String codeGroup;

    @ApiModelProperty(value = "根据编码分组")
    private YesOrNoEnum singleLanguageFlag;

    @ApiModelProperty(value = "是否缓存")
    private String cache;

    @ApiModelProperty(value = "是否缓存")
    private boolean decorateLanguageName;

    public boolean isCache() {
        return StrUtil.equals(YesOrNoEnum.YES.getValueStr(), cache);
    }
    public boolean isSingleLanguage() {
        return singleLanguageFlag == YesOrNoEnum.YES;
    }

}
