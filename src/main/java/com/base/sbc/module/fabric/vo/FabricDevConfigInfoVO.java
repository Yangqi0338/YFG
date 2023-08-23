package com.base.sbc.module.fabric.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("开发信息")
public class FabricDevConfigInfoVO {
    private String id;
    @ApiModelProperty("开发名称")
    private String devName;
    @ApiModelProperty("开发编码")
    private String devCode;
    @ApiModelProperty("实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalStartDate;
    @ApiModelProperty("实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date practicalEndDate;
    @ApiModelProperty("预计开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectStartDate;
    @ApiModelProperty("预计结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expectEndDate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("附件")
    private String attachmentUrl;
    @ApiModelProperty("状态(0.未处理,1.通过，2.失败)")
    private String status;
    @ApiModelProperty("分配状态 未分配，进行中，已完成")
    private String devStatus;
}
