package com.base.sbc.module.patternmaking.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date planTime;
    /**
     * 完成时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
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
