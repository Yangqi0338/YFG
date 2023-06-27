package com.base.sbc.module.planning.vo;

import com.base.sbc.config.common.annotation.UserAvatar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：PlanningSummaryDetailVo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningSummaryDetailVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 10:45
 */
@Data
@ApiModel("企划汇总明细数据 PlanningSummaryDetailVo")
public class PlanningSummaryDetailVo {
    @ApiModelProperty(value = "款号", example = "5CA232731")
    private String designNo;

    @ApiModelProperty(value = "波段", example = "2C")
    private String bandCode;

    @ApiModelProperty(value = "价格带", example = "100-200")
    private String price;

    @ApiModelProperty(value = "设计师名称")
    private String designer;

    @ApiModelProperty(value = "设计师id")
    private String designerId;

    @ApiModelProperty(value = "设计师头像")
    @UserAvatar("designerId")
    private String designerAvatar;

}
