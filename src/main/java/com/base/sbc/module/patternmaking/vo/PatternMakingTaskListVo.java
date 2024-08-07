package com.base.sbc.module.patternmaking.vo;


import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.workload.dto.WorkloadRatingDetailDTO;
import com.base.sbc.module.workload.vo.WorkloadRatingConfigVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 打样任务-列表Vo
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.PatternMakingTaskListVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 09:33
 */
@Data
@ApiModel("打样任务-列表Vo PatternMakingTaskListVo ")
public class PatternMakingTaskListVo extends PatternMaking {

    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;

    @ApiModelProperty(value = "产品图片")
    private String stylePic;
    @ApiModelProperty(value = "款式名称")
    private String styleName;
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "紧急程度")
    private String urgency;
    @ApiModelProperty(value = "紧急程度")
    private String urgencyName;
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    @ApiModelProperty(value = "节点")
    private String node;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "样衣组长确认齐套:1齐套")
    private String sglKitting;
    @ApiModelProperty(value = "工艺员确认齐套：1齐套")
    private String technicianKitting;
    @ApiModelProperty(value = "挂起状态:1挂起")
    private String suspend;
    @ApiModelProperty(value = "排序")
    private BigDecimal sort;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date updateDate;
    /**
     * 挂起状态(“打胚样”“打净样”“做纸样”)
     */
    @ApiModelProperty(value = "挂起状态(“打胚样”“打净样”“做纸样”)")
    private String suspendStatus;
    /**
     * 挂起备注
     */
    @ApiModelProperty(value = "挂起备注")
    private String suspendRemarks;

    /**
     * 样衣条码
     */
    @ApiModelProperty(value = "样衣条码")
    private String sampleBarCode;
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
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    private String cutterName;
    /**
     * 裁剪工id
     */
    @ApiModelProperty(value = "裁剪工id")
    private String cutterId;

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

    /**
     * 设计师名称
     */
    @ApiModelProperty(value = "设计师名称")
    private String designer;
    /**
     * 设计师id
     */
    @ApiModelProperty(value = "设计师id")
    private String designerId;

   
    @ApiModelProperty(value = "样衣制作评分")
    private BigDecimal sampleMakingScore;

    @ApiModelProperty(value = "打版质量评分")
    private BigDecimal patternMakingScore;

    private String patSeq;
    /**
     * 放码师id
     */
    @ApiModelProperty(value = "放码师id")
    private String gradingId;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    private String gradingName;

    /**
     * 放码时间
     */
    @ApiModelProperty(value = "放码时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gradingDate;
    /**
     * 打版类型
     */
    @ApiModelProperty(value = "打版类型")
    private String sampleType;

    @ApiModelProperty(value = "分配车缝工备注")
    private String stitcherRemark;

    private List<NodeStatusVo> nodeStatusList;

    private String userId;

    private String name;

    private List<WorkloadRatingConfigVO> ratingConfigList;

    private WorkloadRatingDetailDTO ratingDetailDTO;

    @JsonIgnore
    private Style style;

    private String prodCategory;

    public Map<String, NodeStatusVo> getNodeStatus() {
        return Optional.ofNullable(nodeStatusList).map(ns -> {
            return ns.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> b));
        }).orElse(new HashMap<>(4));
    }

    public String getDesigner() {
        if(StrUtil.contains(designer,StrUtil.COMMA)){
            return designer.split(",")[0];
        }
        return designer;
    }
}
