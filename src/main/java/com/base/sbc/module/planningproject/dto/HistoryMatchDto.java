package com.base.sbc.module.planningproject.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/30 17:22:13
 * @mail 247967116@qq.com
 */
@Data
public class HistoryMatchDto {
    /**
     * 旧号集合
     */
    private String oldDesignNos;

    @ApiModelProperty(value = "企划规划看板id")
    private String planningProjectId;
}
