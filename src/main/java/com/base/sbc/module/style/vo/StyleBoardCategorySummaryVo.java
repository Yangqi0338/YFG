package com.base.sbc.module.style.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel("品类数据汇总vo StyleBoardCategorySummaryVo")
public class StyleBoardCategorySummaryVo {

    /**
     * 大类id
     */
    @ApiModelProperty(value = "大类", example = "外套")
    private String prodCategory1st;
    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类", example = "上衣")
    private String prodCategory;
    /**
     * 中类id
     */
    @ApiModelProperty(value = "中类", example = "小外套")
    private String prodCategory2nd;

    /**
     * 企划数量
     */
    @ApiModelProperty(value = "企划数量", example = "10")
    private Long planningNum;

    /**
     * 开款数量
     */
    @ApiModelProperty(value = "开款数量", example = "10")
    private Long scriptedNum;

    /**
     * 中类数量
     */
    @ApiModelProperty(value = "中类数量", example = "10")
    private Long skc;

    /**
     * 大类数量
     */
    @ApiModelProperty(value = "大类数量", example = "10")
    private Long total;

    /**
     * 品类汇总数据类型（1-SKC 2-线上企划需求数 3-线下企划需求数）
     */
    @ApiModelProperty(value = "品类汇总数据类型（1-SKC 2-线上企划需求数 3-线下企划需求数）", example = "1")
    private Integer type;
}
