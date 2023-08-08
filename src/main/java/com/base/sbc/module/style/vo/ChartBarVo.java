package com.base.sbc.module.style.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述： 柱状图
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.ChartBarVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-17 16:22
 */
@Data
@ApiModel("设计档案左侧树 vo ChartBarVo ")
public class ChartBarVo {

    @ApiModelProperty(value = "项目")
    private String product;

    @ApiModelProperty(value = "维度")
    private String dimension;

    @ApiModelProperty(value = "数量")
    private BigDecimal total;
}
