package com.base.sbc.module.sample.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class VerificationDto {
    /*id*/
    @NotBlank(message = "id必填")
    private String id;
}
