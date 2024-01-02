package com.base.sbc.module.patternmaking.vo;

import lombok.Data;

import java.util.List;

@Data
public class StyleResearchProcessVo {
    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 大货款号
     */
    private String bulkStyleNo;
    /**
     * 设计师名称
     */
    private String designerName;
    /**
     * 版师名称
     */
    private String sampleName;
    /**
     * 图片
     */
    private String picture;

    /**
     * 当前节点名称
     */
    private String presentNodeName;

    /**
     * 当前节点状态
     */
    private String presentNodeStatus;

    /**
     * 当前节点状态
     */
    private String presentNodeStatusName;

    /**
     * 节点集合
     */
    private List<StyleResearchNodeVo> nodeList;
}
