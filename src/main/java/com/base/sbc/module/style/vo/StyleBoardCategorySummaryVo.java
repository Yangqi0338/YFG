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
     * 线上企划是否合并（0-否 1-是）
     */
    @ApiModelProperty(value = "线上企划是否合并（0-否 1-是）", example = "0")
    private Integer onlinePlanningIsMerge;

    /**
     * 线上企划数量
     */
    @ApiModelProperty(value = "线上企划数量", example = "10")
    private Long onlinePlanningNum;

    /**
     * 线下企划是否合并（0-否 1-是）
     */
    @ApiModelProperty(value = "线下企划是否合并（0-否 1-是）", example = "0")
    private Integer offlinePlanningIsMerge;

    /**
     * 线下企划数量
     */
    @ApiModelProperty(value = "线下企划数量", example = "10")
    private Long offlinePlanningNum;

    /**
     * 开款数量
     */
    @ApiModelProperty(value = "开款数量", example = "10")
    private Long scriptedNum;

    /**
     * 缺口总数
     */
    @ApiModelProperty(value = "缺口总数", example = "10")
    private Long gapsNum;

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

}
