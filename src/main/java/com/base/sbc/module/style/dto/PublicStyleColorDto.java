package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PublicStyleColorDto {
    /*id*/
    @NotBlank(message = "id必填")
    private String id;

    /*号型类型编码*/
//    @NotBlank(message = "号型类型编码")
    private String sizeRange;

    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String scmSendFlag;

    @ApiModelProperty(value = "次品编号")
    private String defectiveNo;

    @ApiModelProperty(value = "次品名称")
    private String defectiveName;

    @ApiModelProperty(value = "颜色库code")
    private String colorCode;

    @ApiModelProperty(value = "下单标记（0否 1是）")
    private String  orderFlag;

    private boolean checkScmSendFlag;
}
