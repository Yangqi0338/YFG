package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  技术看板DTO
 * @author lizan
 * @date 2023-07-10 16:02
 */
@Data
@ApiModel("技术看板DTO PatternMakingWeekMonthViewDTO ")
@NoArgsConstructor
@AllArgsConstructor
public class PatternMakingWeekMonthViewDto {

    /**
     * 版样类型
     */
    @ApiModelProperty(value = "版样类型")
    private String sampleType;

    @ApiModelProperty(value = "企业编码")
    private String companyCode;
    /**
     * 月 month  周 week
     */
    @ApiModelProperty(value = "月周")
    private String weeklyMonth;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 查询状态列表 （待接收、已接收、打版中、打版完成）
     */
    @ApiModelProperty(value = "查询状态列表")
    private List<String> statusList;

    /**
     * 节点状态
     */
    private String status;

    /**
     * 品类id列表
     */
    @ApiModelProperty(value = "品类id列表")
    private List<String> categoryIds;

    /**
     * 节点
     */
    @ApiModelProperty(value = "节点")
    private String node;

}
