package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "品类数量统计")
    private List<DimensionTotalVo> xList;

    @ApiModelProperty(value = "波段数量统计")
    private List<DimensionTotalVo> yList;


    @ApiModelProperty(value = "明细数据")
    private Map<String, List<PlanningSummaryDetailVo>> xyData;
    @ApiModelProperty(value = "维度统计数据")
    private List<List<String>> demandSummary;
}
