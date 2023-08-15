package com.base.sbc.module.planning.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("产品季拉下选择")
public class ProductSeasonSelectVO {
    @ApiModelProperty(value = "企划id")
    private String id;
    /**
     * 企划名称
     */
    @ApiModelProperty(value = "企划名称")
    private String name;
}
