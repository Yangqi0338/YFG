package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：波段维度统计
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningBandTotalVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-12 20:41
 */
@Data
@ApiModel("波段维度统计 PlanningBandTotalVo")
public class PlanningBandTotalVo {

    @ApiModelProperty(value = "波段名称", example = "1A")
    private String bandName;

    @ApiModelProperty(value = "波段编码", example = "1A")
    private String bandCode;

    @ApiModelProperty(value = "企划数量", example = "2")
    private Long planningTotal = 0L;

    @ApiModelProperty(value = "下单数量", example = "2")
    private Long orderTotal = 0L;

    @ApiModelProperty(value = "总数", example = "2")
    private Long total = 0L;

    public Long totalAdd() {
        if (total == null) {
            total = 1L;
        } else {
            total++;
        }
        return total;
    }

    public Long planningTotalAdd() {
        if (planningTotal == null) {
            planningTotal = 1L;
        } else {
            planningTotal++;
        }
        totalAdd();
        return planningTotal;
    }

    public Long orderTotalAdd() {
        if (orderTotal == null) {
            orderTotal = 1L;
        } else {
            orderTotal++;
        }
        totalAdd();
        return orderTotal;
    }
}
