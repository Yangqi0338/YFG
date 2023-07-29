package com.base.sbc.module.smp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * PDM款号下发尺码修改验证 PlmStyleSizeParam
 *
 * @author wubin
 * @date 2021-06-25
 */
@Data
public class PdmStyleCheckParam {
    @ApiModelProperty(notes = "大货款号")
    private String styleNo;

    @ApiModelProperty(notes = "值")
    private String code;

}
