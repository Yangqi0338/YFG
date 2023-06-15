package com.base.sbc.module.patternmaking.vo;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 类描述：样衣看板
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.SampleBoardVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-15 10:12
 */
@Data
@ApiModel("样衣看板vo SampleBoardVo ")
public class SampleBoardVo {
    @ApiModelProperty(value = "数据id")
    private String id;
    @ApiModelProperty(value = "打版id")
    private String patternMakingId;
    @ApiModelProperty(value = "样衣设计id")
    private String sampleDesignId;

    @ApiModelProperty(value = "款图")
    private String stylePic;
    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "年份")
    private String year;
    /**
     * 季节
     */
    @ApiModelProperty(value = "季节")
    private String season;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "款式风格")
    private String style;

    @ApiModelProperty(value = "设计款号")
    private String designNo;

    @ApiModelProperty(value = "款式(大货款号)")
    private String styleNo;


    @ApiModelProperty(value = "打版类型")
    private String sampleType;

    @ApiModelProperty(value = "打版难度")
    private String patDiff;

    @ApiModelProperty(value = "打样顺序")
    private String patSeq;

    @ApiModelProperty(value = "工艺员确认齐套")
    private String technicianKitting;
    /**
     * 工艺员确认齐套时间
     */
    @ApiModelProperty(value = "工艺员确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date technicianKittingDate;
    /**
     * 样衣组长确认齐套
     */
    @ApiModelProperty(value = "样衣组长确认齐套")
    private String sglKitting;
    /**
     * 样衣组长确认齐套时间
     */
    @ApiModelProperty(value = "样衣组长确认齐套时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sglKittingDate;

    /**
     * 版师名称
     */
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    /**
     * 版师id
     */
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;

    @ApiModelProperty(value = "需求数")
    private BigDecimal requirementNum;

    @ApiModelProperty(value = "纸样完成数量")
    private BigDecimal patternFinishNum;

    @ApiModelProperty(value = "裁剪工")
    private String cutterName;
    @ApiModelProperty(value = "裁剪工id")
    private String cutterId;
    @ApiModelProperty(value = "裁剪完成数量")
    private BigDecimal cutterFinishNum;

    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    private String stitcher;
    /**
     * 车缝工id
     */
    @ApiModelProperty(value = "车缝工id")
    private String stitcherId;


    @ApiModelProperty(value = "打版质量评分")
    private BigDecimal patternMakingScore;

    /**
     * 改版原因
     */
    @ApiModelProperty(value = "改版原因")
    private String revisionReason;
    /**
     * 改版意见
     */
    @ApiModelProperty(value = "改版意见")
    private String revisionComments;

    /**
     * 二次加工(0否，1是)
     */
    @ApiModelProperty(value = "二次加工(0否，1是)")
    private String secondProcessing;

    @ApiModelProperty(value = "节点状态")
    private Map<String, NodeStatusVo> nodeStatus;

    public String getId() {
        return IdUtil.fastSimpleUUID();
    }
}
