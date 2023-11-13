package com.base.sbc.client.flowable.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>流程任务<p>
 *
 * @author Tony
 * @date 2021-04-03
 */
@ApiModel("工作流任务相关--请求参数")
@Data
public class FlowQueryVo {

    @ApiModelProperty("流程名称")
    private String procDefName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("当前页码")
    private Integer pageNum;

    @ApiModelProperty("每页条数")
    private Integer pageSize;

    private String category;

    private String  taskName;

    private String startUserId;

    private String  createTime;

    private String contentApproval;

    private String isAdmin;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
