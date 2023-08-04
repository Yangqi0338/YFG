package com.base.sbc.module.purchase.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("采购单列表")
public class PurchasePageDTO extends Page {
    @ApiModelProperty(value = "单据状态（0正常 1作废）")
    private String orderStatus;

    @ApiModelProperty(value = "审核状态（0待审核 1审核通过 2驳回）")
    private String status;

    @ApiModelProperty(value = "下发状态（0未下发 1已下发）"  )
    private String distributeStatus;
}
