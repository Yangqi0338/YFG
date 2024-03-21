package com.base.sbc.module.report.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 设计下单进度明细报表
 */
@NoArgsConstructor
@Data
public class DesignOrderScheduleDetailsReportVo {
    /**
     * 配色id
     */
    private String id;
    /**
     * 生产类型
     */
    private String devtTypeName;
    /**
     * 月份
     */
    private String month;
    /**
     * 品牌
     */
    private String brandName;
    /**
     * 年份季节
     */
    private String yearSeason;
    /**
     * 年份
     */
    private String year;
    /**
     * 年份
     */
    private String seasonName;
    /**
     * 年份
     */
    private String season;
    /**
     * 设计师
     */
    private String designerName;
    /**
     * 波段
     */
    private String bandName;
    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 大货款号
     */
    private String styleNo;
    /**
     * 图片
     */
    private String styleColorPic;
    /**
     * 投产时间
     */
    private Date commissioningDate;
    /**
     * 设计下面料详单时间
     */
    private Date sendMainFabricDate;
    /**
     * 设计下面料详单逾期原因
     */
    private String sendMainFabricOverdueReason;
    /**
     * 设计下面料详单逾期天数
     */
    private String sendMainFabricDay;
    /**
     * 设计下明细单时间
     */
    private Date designDetailDate;
    /**
     * 设计下明细单逾期原因
     */
    private String designDetailOverdueReason;
    /**
     * 设计下明细单逾期天数
     */
    private String designDetailDay;
    /**
     * 设计下面正确样时间
     */
    private Date designCorrectDate;
    /**
     * 设计下面正确样时间逾期原因
     */
    private String designCorrectOverdueReason;
    /**
     * 设计下面正确样时间逾期天数
     */
    private String designCorrectDay;

    /**
     * 列头筛选数量
     */
    private Integer groupCount;
}
