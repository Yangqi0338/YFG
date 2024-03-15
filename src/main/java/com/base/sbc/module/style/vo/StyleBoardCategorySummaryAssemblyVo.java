package com.base.sbc.module.style.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：品类数据汇总 返回vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.StyleBoardCategorySummaryVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-29 10:54
 */

@Data
@ApiModel("品类数据汇总整体返回 VO")
public class StyleBoardCategorySummaryAssemblyVo {

    /**
     * skc品类汇总数据
     */
    @ApiModelProperty(value = "skc品类汇总数据")
    private List<StyleBoardCategorySummaryVo> skcList;
    /**
     * 线上企划需求品类汇总数据
     */
    @ApiModelProperty(value = "线上企划需求品类汇总数据")
    private List<StyleBoardCategorySummaryVo> onlinePlanningList;
    /**
     * 线下企划需求品类汇总数据
     */
    @ApiModelProperty(value = "线下企划需求品类汇总数据")
    private List<StyleBoardCategorySummaryVo> offlinePlanningList;

}
