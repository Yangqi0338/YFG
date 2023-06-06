package com.base.sbc.module.patternmaking.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class PatternMakingTaskListVo {

    @ApiModelProperty(value = "id")
    private String id;
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
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;
    @ApiModelProperty(value = "版师id")
    private String patternDesignId;
    @ApiModelProperty(value = "节点")
    private String node;
    @ApiModelProperty(value = "状态")
    private String status;
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
    private List<NodeStatusVo> nodeStatusList;
}
