package com.base.sbc.module.patternmaking.vo;


import com.base.sbc.module.nodestatus.entity.NodeStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类描述：打版进度
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.vo.StyleStepVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-12 10:53
 */
@Data
@ApiModel("打版-样衣进度列表vo StyleStepVo ")
public class StyleStepVo {

    @ApiModelProperty(value = "款式设计id")
    private String id;

    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "款式设计id")
    private String planningSeasonId;

    @ApiModelProperty(value = "款式图片")
    private String stylePic;
    @ApiModelProperty(value = "设计工艺员名称")
    private String technicianName;

    @ApiModelProperty(value = "设计师名称")
    private String designer;

    @ApiModelProperty(value = "打版类型")
    public List<String> getSampleTypes() {
        return Optional.ofNullable(patternMakingSteps)
                .map(pms -> pms.stream().map(PatternMakingStepVo::getSampleType).collect(Collectors.toList()))
                .orElse(null);
    }

    /** 生产模式 */
    @ApiModelProperty(value = "生产模式"  )
    private String devtType;
    /** 生产模式名称 */
    @ApiModelProperty(value = "生产模式名称"  )
    private String devtTypeName;

    @ApiModelProperty(value = "打版指令进度列表")
    private List<PatternMakingStepVo> patternMakingSteps;

    @Data
    @ApiModel("打版进度-打版指令进度vo PatternMakingStepVo")
    public class PatternMakingStepVo {
        @ApiModelProperty(value = "款式设计id")
        private String id;
        @ApiModelProperty(value = "款式设计id")
        private String styleId;
        @ApiModelProperty(value = "打版单号")
        private String code;
        @ApiModelProperty(value = "版师名称")
        private String patternDesignName;

        private String patternNo;

        @ApiModelProperty(value = "打版类型")
        private String sampleType;
        /** 中断样衣(0正常，1中断) */
        private String breakOffSample;
        /** 中断打版(0正常，1中断) */
        private String breakOffPattern;

        @ApiModelProperty(value = "供应商Id")
        private String supplierId;
        @ApiModelProperty(value = "供应商名称")
        private String supplierName;
        private String patternMakingDevtType;

        @ApiModelProperty(value = "节点信息")
        private Map<String, NodeStatus> nodeStatus;
    }
}
