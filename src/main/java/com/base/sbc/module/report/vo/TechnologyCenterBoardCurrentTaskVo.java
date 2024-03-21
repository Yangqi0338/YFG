package com.base.sbc.module.report.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 技术看板当前任务
 */
@Data
public class TechnologyCenterBoardCurrentTaskVo {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 打版类型个数，以及总数
     */
    private Map<String, Integer> plateMakingTypeMap;
    /**
     * 设计款号
     */
    private List<String> designNolist;
    /**
     * 排名
     */
    private Integer sort;
}
