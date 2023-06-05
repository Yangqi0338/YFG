package com.base.sbc.module.patternmaking.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("节点状态vo NodeStatusVo")
public class NodeStatusVo {

    @ApiModelProperty(value = "数据id")
    private String dataId;
    @ApiModelProperty(value = "节点")
    private String node;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date startDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "M月d日HH:mm", timezone = "GMT+8")
    private Date endDate;
}
