package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.PreProductionSampleTaskSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 20:07
 */
@Data
@ApiModel("产前样-任务筛选 PreProductionSampleTaskSearchDto")
public class PreProductionSampleTaskSearchDto {

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

}
