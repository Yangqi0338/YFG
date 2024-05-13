package com.base.sbc.module.report.vo;

import lombok.Data;

/**
 * 技术看板 打版/样衣产能数
 * 月/周/日 ： 默认进来周：默认显示最近5周，天：最近7天，月：最近6个月
 */
@Data
public class TechnologyCenterBoardCapacityNumberVo {
    /**
     * 日期
     */
    private String dateFormat;

    /**
     * 个数
     */
    private Integer count;

    /**
     * 查询开始时间-结束时间
     */
    private String[] betweenDate;
}
