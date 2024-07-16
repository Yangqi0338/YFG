package com.base.sbc.module.basicsdatum.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasicsdatumMaterialUpdateVo {
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "材料名称")
    private String materialCodeName;
}
