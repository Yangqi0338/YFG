package com.base.sbc.module.planning.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.PlanningChannelSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-21 16:36
 */
@Data
@ApiModel("企划-渠道 PlanningChannelSearchDto")
public class PlanningChannelSearchDto extends Page {

    @ApiModelProperty(value = "产品季节id")
    private String planningSeasonId;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "季节")
    private String season;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "性别")
    private String sex;

    private String businessType;
}
