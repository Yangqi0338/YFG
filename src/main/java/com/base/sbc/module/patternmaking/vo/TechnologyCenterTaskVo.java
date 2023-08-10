package com.base.sbc.module.patternmaking.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：技术中心看板-任务列表
 * @address com.base.sbc.module.patternmaking.vo.TechnologyCenterTaskVo
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-31 14:14
 * @version 1.0
 */
@Data
@ApiModel("技术中心看板-任务列表Vo TechnologyCenterTaskVo ")
public class TechnologyCenterTaskVo {

    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "款号")
    private String designNo;
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    @ApiModelProperty(value = "款图")
    private String stylePic;
    @ApiModelProperty(value = "大类名称")
    private String prodCategory1stName;
    @ApiModelProperty(value = "品类")
    private String prodCategoryName;
    @ApiModelProperty(value = "中类名称")
    private String prodCategory2ndName;
    @ApiModelProperty(value = "小类名称")
    private String prodCategory3rdName;

    @ApiModelProperty(value = "设计下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designSendDate;

    /**
     * 版房主管下发时间
     */
    @ApiModelProperty(value = "版房主管下发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date prmSendDate;

    /**
     * 版房主管下发状态:(0未下发，1已下发)
     */
    @ApiModelProperty(value = "版房主管下发状态:(0未下发，1已下发)")
    private String prmSendStatus;

    /**
     * 紧急程度
     */
    @ApiModelProperty(value = "紧急程度")
    private String urgency;

    @ApiModelProperty(value = "紧急程度名称")
    private String urgencyName;
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;

    /**
     * 中断样衣(0正常，1中断)
     */
    @ApiModelProperty(value = "中断样衣(0正常，1中断)")
    private String breakOffSample;
    /**
     * 中断打版(0正常，1中断)
     */
    @ApiModelProperty(value = "中断打版(0正常，1中断)")
    private String breakOffPattern;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 年份名称
     */
    @ApiModelProperty(value = "年份名称")
    private String yearName;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 季节名称
     */
    @ApiModelProperty(value = "季节名称")
    private String seasonName;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;
    /**
     * 月份名称
     */
    @ApiModelProperty(value = "月份名称")
    private String monthName;
    /**
     * 波段(编码)
     */
    @ApiModelProperty(value = "波段(编码)")
    private String bandCode;
    /**
     * 波段名称
     */
    @ApiModelProperty(value = "波段名称")
    private String bandName;

    @ApiModelProperty(value = "版师列表")
    private List<PatternDesignVo> pdList;


}
