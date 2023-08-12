package com.base.sbc.module.style.dto;

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

}
