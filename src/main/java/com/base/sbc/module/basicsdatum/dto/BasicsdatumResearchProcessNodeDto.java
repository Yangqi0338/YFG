package com.base.sbc.module.basicsdatum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class BasicsdatumResearchProcessNodeDto {
    /** 研发总进度模板id */
    private String templateId;
    /** 节点编码 */
    private String code;
    /** 节点名称 */
    private String name;
    /** 日期类型(0:工作日，1：自然日) */
    private Integer dateType;
    /** 天数 */
    @NotNull(message = "常量周期")
    private Integer numberDay;
    /** 排序 */
    private Integer sort;
}
