package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class StyleColorOverdueReasonDto {

    /**
     * 配色id不能为空
     */
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 类型0 ：设计下明面料详单逾期，1：设计下明细单逾期，2：设计下正确样逾期
     */
    @NotBlank(message = "逾期类型不能为空")
    private String type;

    /**
     * 逾期内容
     */
    private String remark;
}
