package com.base.sbc.module.report.vo;

import lombok.Data;

import java.util.Date;

/**
 * 技术中心看板 数据总览统计个数
 */
@Data
public class TechnologyCenterBoardOverviewDataVo {
    /**
     * 待接收数量
     */
    private Integer preAcceptedQuantity;
    /**
     * 打版中数量
     */
    private Integer plateMakingQuantity;
    /**
     * 中断打版数量
     */
    private Integer breakPlateMakingQuantity;
    /**
     * 裁剪开始数量
     */
    private Integer cuttingStartQuantity;
    /**
     * 车缝开始数量
     */
    private Integer sewingStartQuantity;

    /**
     * 打版需求总数
     */
    private Integer plateMakingDemandQuantity;

    /**
     * 打版完成数
     */
    private Integer plateMakingFinishQuantity;

    /**
     * 样衣完成数
     */
    private Integer sampleFinishQuantity;

    /**
     * 查询开始时间-结束时间
     */
    private String[] betweenDate;

}
