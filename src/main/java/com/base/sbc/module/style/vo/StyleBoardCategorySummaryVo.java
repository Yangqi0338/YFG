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

    @ApiModelProperty(value = "中类数量", example = "10")
    private Long skc;

    @ApiModelProperty(value = "大类数量", example = "10")
    private Long total;

}
