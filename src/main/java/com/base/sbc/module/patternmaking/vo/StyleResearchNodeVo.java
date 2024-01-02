package com.base.sbc.module.patternmaking.vo;

import lombok.Data;

import java.util.Date;

@Data
public class StyleResearchNodeVo {
    /**
     * 节点编码
     */
    private String nodeCode;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 计划时间
     */
    private Date planTime;
    /**
     * 完成时间
     */
    private Date finishTime;
    /**
     * 节点状态
     */
    private Integer nodeStatus;
    /**
     * 天数
     */
    private Integer numberDay;
}
