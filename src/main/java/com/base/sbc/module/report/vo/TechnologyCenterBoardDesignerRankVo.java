package com.base.sbc.module.report.vo;

import lombok.Data;

@Data
public class TechnologyCenterBoardDesignerRankVo {
    /**
     * 设计师名字
     */
    private String designer;
    /**
     * 个数
     */
    private Integer count;

    /**
     * 查询开始时间-结束时间
     */
    private String[] betweenDate;
}
