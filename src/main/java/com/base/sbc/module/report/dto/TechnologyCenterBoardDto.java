package com.base.sbc.module.report.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 技术中心看板 接收参数
 */
@NoArgsConstructor
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
     * 天 : day
     */
    private String type;

    /***
     * 0：打版，1：样衣
     */
    private String dataType;

    /**
     *  是否是历史数据 0:否，1：是
     */
    private Integer historicalData;

    /**
     *  节点名称
     */
    private String node;

    /**
     * 是否打版中断
     */
    private Integer breakOffPattern;
    /**
     * 节点状态集合
     */
    private List<String> nodeStatusList;

    /**
     * sqlSelect 查询返回值
     */
    private String sqlSelect;

    /**
     * 查询开始时间-结束时间
     */
    private String[] betweenDate;

    public TechnologyCenterBoardDto(Integer historicalData, String node, Integer breakOffPattern, List<String> nodeStatusList, String sqlSelect) {
        this.historicalData = historicalData;
        this.node = node;
        this.breakOffPattern = breakOffPattern;
        this.nodeStatusList = nodeStatusList;
        this.sqlSelect = sqlSelect;
    }
}
