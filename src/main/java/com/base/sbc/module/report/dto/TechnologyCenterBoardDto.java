package com.base.sbc.module.report.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 技术中心看板 接收参数
 */
@Data
public class TechnologyCenterBoardDto {
    /**
     * 版房
     */
    private List<String> plateHouseList;
    /**
     * 年 ：year
     * 季 : season
     * 月 : month
     * 周 : week
     */
    private String type;

    /**
     * 起始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;
}
