package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusCheckDetailOldDTO {

    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @ApiModelProperty(value = "国家类型")
    private String type;

    @ApiModelProperty(value = "标准列编码集合")
    private List<String> standardColumnCodeList;

}
