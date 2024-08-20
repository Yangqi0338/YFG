package com.base.sbc.module.patternmaking.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

/**
 * 设计收到时间
 */
@Data
public class PatternMakingDesignReceiptDto {

    @NotBlank(message = "id不能为空")
    private String id;

    private Date designReceiptDate;
}
