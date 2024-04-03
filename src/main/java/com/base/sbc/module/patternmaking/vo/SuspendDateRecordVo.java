package com.base.sbc.module.patternmaking.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@ApiModel("挂起时间记录")
public class SuspendDateRecordVo {
    @ApiModelProperty("当前挂起的标识")
    private String currentFlag;

    @ApiModelProperty("挂起时间统计")
    private Map<String, SuspendDate> suspendDateMap;

    @Data
    public static class SuspendDate{
        @ApiModelProperty("挂起开始时间")
        private Date startTime;
        @ApiModelProperty("挂起结束时间")
        private Date endTime;
    }
}
