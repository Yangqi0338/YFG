package com.base.sbc.module.patternmaking.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("打版挂起dto SuspendDto ")
public class SuspendDto {
    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "挂起", example = "(“打胚样”“打净样”“做纸样”)", required = true)
    @NotBlank(message = "挂起状态不能为空")
    private String suspendStatus;

    @ApiModelProperty(value = "挂起备注", example = "")
    @Length(max = 255, message = "超过长度")
    private String suspendRemarks;


}
