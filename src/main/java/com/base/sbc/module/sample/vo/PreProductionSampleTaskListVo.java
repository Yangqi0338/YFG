package com.base.sbc.module.sample.vo;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.patternmaking.vo.NodeStatusVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：产前样任务列表Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.PreProductionSampleTaskListVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 20:10
 */
@Data
@ApiModel("产前样任务-列表Vo PatternMakingTaskListVo ")
public class PreProductionSampleTaskListVo {
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
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "紧急程度")
    private String urgency;
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    @ApiModelProperty(value = "节点")
    private String node;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "齐套:1齐套")
    private String kitting;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date updateDate;

    /**
     * 工艺师名称
     */
    @ApiModelProperty(value = "工艺师名称")
    @NotBlank(message = "工艺师不能为空")
    private String technologistName;
    /**
     * 工艺师id
     */
    @ApiModelProperty(value = "工艺师id")
    @NotBlank(message = "工艺师不能为空")
    private String technologistId;
    /**
     * 放码师id
     */
    @ApiModelProperty(value = "放码师id")
    @NotBlank(message = "放码师不能为空")
    private String gradingId;
    /**
     * 放码师名称
     */
    @ApiModelProperty(value = "放码师名称")
    @NotBlank(message = "放码师不能为空")
    private String gradingName;
    /**
     * 裁剪工
     */
    @ApiModelProperty(value = "裁剪工")
    @NotBlank(message = "裁剪工不能为空")
    private String cutterName;
    /**
     * 裁剪工id
     */
    @ApiModelProperty(value = "裁剪工id")
    @NotBlank(message = "裁剪工不能为空")
    private String cutterId;
    /**
     * 车缝工名称
     */
    @ApiModelProperty(value = "车缝工名称")
    @NotBlank(message = "车缝工不能为空")
    private String stitcher;
    /**
     * 车缝工id
     */
    @ApiModelProperty(value = "车缝工id")
    @NotBlank(message = "车缝工不能为空")
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

    /**
     * 中断样衣(0正常，1中断)
     */
    @ApiModelProperty(value = "中断样衣(0正常，1中断)")
    private String breakOffSample;


    /**
     * 打版类型
     */
    @ApiModelProperty(value = "打版类型")
    private String sampleType;
    private List<NodeStatusVo> nodeStatusList;

    public Map<String, NodeStatusVo> getNodeStatus() {
        return Optional.ofNullable(nodeStatusList).map(ns -> {
            return ns.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> b));
        }).orElse(new HashMap<>(4));
    }

    public String getDesigner() {
        if (StrUtil.contains(designer, StrUtil.COMMA)) {
            return designer.split(",")[0];
        }
        return designer;
    }
}
