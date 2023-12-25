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
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    @ApiModelProperty(value = "国家名")
    private String countryName;

    @NotBlank(message = "语言不能为空")
    @ApiModelProperty(value = "语种编码")
    private String languageCode;

    @ApiModelProperty(value = "语种名")
    private String languageName;

}
