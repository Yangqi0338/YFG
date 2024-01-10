package com.base.sbc.module.patternmaking.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StyleResearchProcessVo {

    /**
     * 设计款号id
     */
    private String styleId;
    /**
     * 配色id
     */
    private String styleColorId;
    /**
     * 设计款号
     */
    private String designNo;
    /**
     * 大货款号
     */
    private String bulkStyleNo;
    /**
     * 设计款号图
     */
    private String stylePic;
    /**
     * 大货款号图
     */
    private String styleColorPic;
    /**
     * 设计师名称
     */
    private String designerName;
    /**
     * 版师名称
     */
    private String patternDesignName;
    /**
     * 生产类型code
     */
    private String devtType;
    /**
     * 生产类型
     */
    private String devtTypeName;
    /**
     * 品牌code
     */
    private String brandCode;
    /**
     * 品牌
     */
    private String brandName;
    /**
     * 图片
     */
    private String picture;
    /**
     * 研发总进度模板id
     */
    private String templateId;
    /**
     * 当前节点名称
     */
    private String presentNodeName;
    /**
     * 当前节点状态
     */
    private String presentNodeStatus;
    /**
     * 当前节点状态名称
     */
    private String presentNodeStatusName;
    /**
     * 当前节点状态名称
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date noNextDraft;

    /**
     * 节点集合
     */
    private List<StyleResearchNodeVo> nodeList;
}
