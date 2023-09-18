package com.base.sbc.module.style.vo;

import com.base.sbc.module.planning.vo.DimensionTotalVo;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.style.vo.StyleSummaryVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-12 18:53
 */
@Data
@ApiModel("款式汇总Vo StyleSummaryVo")
public class StyleSummaryVo {

    @ApiModelProperty(value = "品类数量统计")
    private List<DimensionTotalVo> xList;

    @ApiModelProperty(value = "波段数量统计")
    private List<DimensionTotalVo> yList;


    @ApiModelProperty(value = "明细数据")
    private Map<String, List<PlanningSummaryDetailVo>> xyData;
}
