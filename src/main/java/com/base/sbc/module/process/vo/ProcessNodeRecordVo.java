package com.base.sbc.module.process.vo;

import com.base.sbc.module.process.entity.ProcessNodeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProcessNodeRecordVo {
    /** 业务实例id */
    @ApiModelProperty(value = "业务实例id"  )
    private String businessInstanceId;
    /** 节点名称 */
    @ApiModelProperty(value = "节点名称"  )
    private String nodeName;
    /** 节点id */
    private String nodeId;
    /** 开始时间 */
    @ApiModelProperty(value = "开始时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    /** 结束时间 */
    @ApiModelProperty(value = "结束时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
    /** 当前状态 */
    @ApiModelProperty(value = "当前状态"  )
    private String atPresentStatus;
    /** 是否完成(0未完成，1已完成) */
    @ApiModelProperty(value = "是否完成(0未完成，1已完成)"  )
    private String isComplete;

    private Integer sort;

    List<ProcessNodeStatus> processNodeStatusList;
}
