package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NeedUpdateVo {

    @ApiModelProperty("状态：0 不需要更新， 1：需要更新，2: 物料无款式使用，")
    private String status;

}
