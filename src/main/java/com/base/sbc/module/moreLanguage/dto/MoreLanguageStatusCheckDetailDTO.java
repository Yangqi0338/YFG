package com.base.sbc.module.moreLanguage.dto;

import cn.hutool.core.annotation.Alias;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageBaseVO;
import com.base.sbc.module.hangtag.vo.HangTagMoreLanguageVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusCheckDetailDTO {

    @Alias("l")
    @ApiModelProperty(value = "语言编码")
    private String languageCode;

    @Alias("a")
    @ApiModelProperty(value = "标准列编码集合")
    private List<MoreLanguageStatusCheckDetailAuditDTO> auditList;
}
