package com.base.sbc.module.purchase.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("采购需求列表")
public class DemandPageDTO extends Page {
    @ApiModelProperty(value = "单据状态（0正常 1作废）")
    private String orderStatus;

    @ApiModelProperty(value = "审核状态（0待审核 1审核通过 2驳回）")
    private String status;

    @ApiModelProperty(value = "是否齐料")
    private String isKitting;

    @ApiModelProperty(value = "仓库id")
    private String warehouseId;
}
