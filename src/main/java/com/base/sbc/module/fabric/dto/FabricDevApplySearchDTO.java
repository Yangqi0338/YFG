package com.base.sbc.module.fabric.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("面料开发申请查询列表")
public class FabricDevApplySearchDTO extends Page {
    @ApiModelProperty("来源1.新增、2.其他")
    private String source;
    @ApiModelProperty("面料分类")
    private String fabricClassifCode;
    @ApiModelProperty("通过情况：1.待分配、2.进行中、3.已完成")
    private String allocationStatus;
}
