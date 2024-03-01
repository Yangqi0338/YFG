package com.base.sbc.module.planning.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/11/28 10:52:23
 * @mail 247967116@qq.com
 */
@Data
public class CheckMutexDto {
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String prodCategory;

    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道id")
    private String planningChannelId;

    /**
     * 中类code
     */
    @ApiModelProperty(value = "中类code")
    private String prodCategory2nd;

    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "系数模板id")
    private String coefficientTemplateId;

}
