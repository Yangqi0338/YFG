package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Alias("scc")
    @JsonProperty("scc")
    @ApiModelProperty(value = "语言编码")
    private String standardColumnCode;

    @Alias("sc")
    @JsonProperty("sc")
    @ApiModelProperty(value = "单据原值")
    private String source;

    @Alias("c")
    @JsonProperty("c")
    @ApiModelProperty(value = "单据翻译")
    private String content;

    @Alias("s")
    @JsonProperty("s")
    @ApiModelProperty(value = "状态")
    private String status;

}
