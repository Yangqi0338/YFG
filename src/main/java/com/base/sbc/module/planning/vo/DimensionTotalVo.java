package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：维度数量统计
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.DimensionTotalVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 10:42
 */
@Data
@ApiModel("维度数量统计Vo DimensionTotalVo")
public class DimensionTotalVo {
    @ApiModelProperty(value = "名称", example = "1A")
    private String name;
    @ApiModelProperty(value = "数量", example = "2")
    private Long total;
    @ApiModelProperty(value = "数量2", example = "2")
    private Long total2;
}
