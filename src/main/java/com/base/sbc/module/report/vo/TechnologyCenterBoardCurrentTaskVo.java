package com.base.sbc.module.report.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 技术看板当前任务
 */
@Data
public class TechnologyCenterBoardCurrentTaskVo {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 打版类型
     */
    private String sampleTypeName;

    /**
     * 打版类型个数，以及总数
     */
    private LinkedHashMap<String, Integer> plateMakingTypeMap;
    /**
     * 设计款号
     */
    private List<String> designNolist;
    /**
     * 排名
     */
    private Integer count;
}
