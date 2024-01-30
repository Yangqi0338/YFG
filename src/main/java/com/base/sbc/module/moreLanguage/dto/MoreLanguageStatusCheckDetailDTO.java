package com.base.sbc.module.moreLanguage.dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.base.sbc.config.enums.business.StyleCountryStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusCheckDetailDTO {

    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @ApiModelProperty(value = "标准列编码集合")
    private List<String> standardColumnCodeList;

}
