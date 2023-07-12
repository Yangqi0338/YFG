package com.base.sbc.module.patternmaking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  技术看板VO
 * @author lizan
 * @date 2023-07-10 16:02
 */
@Data
@ApiModel("技术看板VO PatternMakingWeekMonthViewVo ")
public class PatternMakingWeekMonthViewVo {

    /**
     * 版样类型
     */
    @ApiModelProperty(value = "版样类型")
    private String sampleType;
    /**
     * 年月周
     */
    @ApiModelProperty(value = "年月周")
    private String yearWeek;
    /**
     * 最初时间
     */
    @ApiModelProperty(value = "最初时间")
    private String minDate;
    /**
     * 统计数量
     */
    @ApiModelProperty(value = "统计数量")
    private String num;
    /**
     * 需求数求和
     */
    @ApiModelProperty(value = "需求数求和")
    private String requirementNumSum;
    /**
     * 节点状态
     */
    @ApiModelProperty(value = "节点状态")
    private String status;

}
