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
public class PdmStyleSizeCheckParam {

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "值")
    private String value;

    @ApiModelProperty(notes = "类型 1颜色 2尺寸")
    private Integer type;

}
