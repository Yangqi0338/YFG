package com.base.sbc.module.patternmaking.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Data
public class PatternMakingReferSampleDto {

    @NotBlank(message = "id不能为空")
    private String id;
    @Range(min = 0, max = 1, message = "状态只能0和1")
    private Integer Status;
}
