package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：打版管理任务查询dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingTaskSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 10:03
 */
@Data
@ApiModel("打版管理任务查询dto PatternMakingTaskSearchDto ")
public class PatternMakingTaskSearchDto {

    @ApiModelProperty(value = "关键字筛选", example = "1")
    private String search;
    @ApiModelProperty(value = "年份", example = "2022")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节", example = "S")
    private String season;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份", example = "1")
    private String month;

    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id", example = "1223333122223333333")
    private String patternDesignId;
    @ApiModelProperty(value = "节点", example = "打样管理")
    private String node;

    @ApiModelProperty(value = "黑单(0:排除黑单数据,1只查黑单数据,空：都查)", example = "1")
    private String isBlackList;
}
