package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.common.base.Page;
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

    @NotBlank(message = "国家不能为空")
    @ApiModelProperty(value = "查询条件标签id")
    private String countryCode;

    @NotBlank(message = "语言不能为空")
    @ApiModelProperty(value = "查询条件标签id集合")
    private String languageCode;

}
