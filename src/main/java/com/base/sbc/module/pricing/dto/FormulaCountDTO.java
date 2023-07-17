package com.base.sbc.module.pricing.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

@Data
public class FormulaCountDTO {

    /**
     * 核价模板id
     */
    @NotBlank(message = "核价模板id不可为空")
    private String id;

    /**
     * 字段计算值
     */
    private Map<String, Object> map;
}
