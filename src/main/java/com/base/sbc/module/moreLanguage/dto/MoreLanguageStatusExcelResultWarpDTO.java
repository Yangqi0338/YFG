package com.base.sbc.module.moreLanguage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoreLanguageStatusExcelResultWarpDTO {

    @ApiModelProperty(value = "款号")
    private List<MoreLanguageStatusExcelResultDTO> result = new ArrayList<>();

    @ApiModelProperty(value = "Redis 查询唯一标识")
    private String uniqueValue;

}
