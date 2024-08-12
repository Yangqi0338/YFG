package com.base.sbc.open.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StyleColorRiskAssessmentDto {


    @ApiModelProperty("大货款号 ")
    @NotEmpty(message = "大货款号不能为空")
    private String styleNo;


    @ApiModelProperty("款式难度 ")
    private String styleDifficulty;


    @ApiModelProperty("面料难度 ")
    private String fabricDifficulty;


    @ApiModelProperty("工艺难度 ")
    private String processDifficulty;

}
