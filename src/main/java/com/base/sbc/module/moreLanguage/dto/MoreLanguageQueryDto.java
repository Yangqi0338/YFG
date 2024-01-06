package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.config.enums.business.CountryLanguageType;
import com.base.sbc.config.enums.business.StandardColumnType;
import com.base.sbc.module.moreLanguage.service.CountryLanguageService;
import com.base.sbc.module.moreLanguage.service.MoreLanguageService;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageQueryDto extends Page {

    @NotBlank(message = "国家语言码不能为空")
    @ApiModelProperty(value = "查询国家语言条件标签Id")
    private String code;

    @NotNull(message = "国家语言类型不能为空", groups = {MoreLanguageService.class})
    @ApiModelProperty(value = "查询国家语言条件标签Id")
    private CountryLanguageType type;

    @NotBlank(message = "查询源不能为空")
    @ApiModelProperty(value = "是否缓存")
    private String cache;

    @ApiModelProperty(value = "标准列码条件标签Code")
    private String standardColumnCode;

    @ApiModelProperty(value = "语言码")
    private String languageCode;

    @ApiModelProperty(value = "根据编码分组")
    private YesOrNoEnum singleLanguageFlag;

    public boolean isCache() {
        return StrUtil.equals(YesOrNoEnum.YES.getValueStr(), cache);
    }
}
