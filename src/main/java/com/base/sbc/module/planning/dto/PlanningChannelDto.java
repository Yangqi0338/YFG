package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningChannel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.dto.PlanningChannelDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-21 16:06
 */
@Data
@ApiModel("企划-渠道 PlanningChannelDto")
public class PlanningChannelDto extends PlanningChannel {

    @ApiModelProperty(value = "产品季节id")
    @NotBlank(message = "产品季信息为空")
    private String planningSeasonId;


    @ApiModelProperty(value = "渠道")
    @NotBlank(message = "渠道为空")
    private String channel;

    @ApiModelProperty(value = "渠道名称")
    @NotBlank(message = "渠道为空")
    private String channelName;

    @ApiModelProperty(value = "性别")
    @NotBlank(message = "性别为空")
    private String sex;

    @ApiModelProperty(value = "性别名称")
    @NotBlank(message = "性别为空")
    private String sexName;
}
