package com.base.sbc.module.patternmaking.vo;

import lombok.Data;

/**
 *  版类对比VO
 * @author lizan
 * @date 2023-07-10 16:02
 */
@Data
public class PatternMakingWeekMonthViewVo {

    /**
     * 版样类型
     */
    private String sampleType;
    /**
     * 年月周
     */
    private String yearWeek;
    /**
     * 最初时间
     */
    private String minDate;
    /**
     * 统计数量
     */
    private String num;

}
