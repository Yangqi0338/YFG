package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MoreLanguageQueryDto extends Page {

    @ApiModelProperty(value = "查询条件标签id集合")
    private String[] countryCode;

}
