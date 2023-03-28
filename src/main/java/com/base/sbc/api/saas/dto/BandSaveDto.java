package com.base.sbc.api.saas.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("波段保存修改 BandSaveDto")
public class BandSaveDto {

    @ApiModelProperty(name = "编号id", value = "编号:为空时新增、不为空时修改",  required = false, example = "")
    private String id;

    @ApiModelProperty(value = "年份",  required = true, example = "2023")
    @NotBlank(message = "年份必填")
    private String particularYear;

    @ApiModelProperty(value = "月份", required = false, example = "01")
    private String month;

    @ApiModelProperty(value = "季节",  required = true, example = "A")
    @NotBlank(message = "季节必填")
    private String season;

    @ApiModelProperty(value = "编码",  required = true, example = "a")
    @NotBlank(message = "编码必填")
    private String code;

    @ApiModelProperty(value = "波段名称",  required = true, example = "波段名称")
    @NotBlank(message = "波段名称必填")
    private String bandName;

    @ApiModelProperty(value = "排序",  required = false, example = "1")
    private Integer sort;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String   status;


}
