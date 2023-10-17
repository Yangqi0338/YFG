package com.base.sbc.module.planning.vo;

import com.base.sbc.module.planning.entity.PlanningDemandProportionSeat;
import com.base.sbc.module.style.vo.DemandOrderSkcVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 类描述：企划汇总Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.vo.PlanningSummaryVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 09:55
 */
@Data
@ApiModel("企划汇总Vo PlanningSummaryVo")
public class PlanningSummaryVo {

    @ApiModelProperty(value = "x轴(波段)数量统计")
    private Collection<PlanningBandTotalVo> xList;

    @ApiModelProperty(value = "y轴(维度)数量统计")
    private Collection<PlanningDimensionTotalVo> yList;


    @ApiModelProperty(value = "明细数据")
    private Map<String, List<DemandOrderSkcVo>> xyData;
    @ApiModelProperty(value = "维度统计数据")
    private List<List<String>> demandSummary;

    @ApiModelProperty(value = "需求维度坑位id")
    private List<String> seatIds;
    @JsonIgnore
    private List<PlanningDemandProportionSeat> seatList;
}
