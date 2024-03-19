package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusCheckDetailAuditDTO {

    @ApiModelProperty(value = "语言编码")
    private String standardColumnCode;

    @ApiModelProperty(value = "单据翻译")
    private String content;

    @ApiModelProperty(value = "审核状态")
    private StyleCountryStatusEnum status;

}
