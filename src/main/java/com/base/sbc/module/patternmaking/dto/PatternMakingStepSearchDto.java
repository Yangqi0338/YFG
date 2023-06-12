package com.base.sbc.module.patternmaking.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：打版-样衣进度列表搜索条件Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.PatternMakingStepSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-12 11:26
 */
@Data
@ApiModel("打版-样衣进度列表搜索条件Dto PatternMakingStepSearchDto ")
public class PatternMakingStepSearchDto extends Page {
    @ApiModelProperty(value = "关键字筛选", example = "1")
    private String search;
    @ApiModelProperty(value = "年份", example = "2022")
    private String year;

    @ApiModelProperty(value = "季节", example = "S")
    private String season;

    @ApiModelProperty(value = "月份", example = "1")
    private String month;
    @ApiModelProperty(value = "波段", example = "1")
    private String bandCode;

    @ApiModelProperty(value = "设计师id", example = "1223333122223333333")
    private String designerId;
}
