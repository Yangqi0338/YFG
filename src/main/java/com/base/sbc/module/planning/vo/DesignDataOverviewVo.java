package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：产品季总览-设计数据总览Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.DesignDataOverviewVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-18 10:18
 */
@Data
@ApiModel("产品季总览-设计数据总览Vo DesignDataOverviewVo")
public class DesignDataOverviewVo {
    @ApiModelProperty(value = "企划下发需求数", example = "100")
    private long qhxfxqzs;

    @ApiModelProperty(value = "设计需求数", example = "100")
    private long sjxqs;

    @ApiModelProperty(value = "未开款数", example = "100")
    private long wkks;
    @ApiModelProperty(value = "未开款数", example = "100")
    private long ykks;

    @ApiModelProperty(value = "已下发打版", example = "100")
    private long yxfdbs;
}
