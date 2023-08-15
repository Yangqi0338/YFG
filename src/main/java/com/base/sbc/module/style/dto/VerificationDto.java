package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class VerificationDto {
    /*id*/
    @NotBlank(message = "id必填")
    private String id;

    /*号型类型编码*/
//    @NotBlank(message = "号型类型编码")
    private String sizeRange;

    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String scmSendFlag;
}
