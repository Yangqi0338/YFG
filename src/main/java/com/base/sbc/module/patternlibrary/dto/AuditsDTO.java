package com.base.sbc.module.patternlibrary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 审批入参
 *
 * @author xhte
 * @create 2024-03-22
 */
@Data
@ApiModel(value = "AuditsDTO对象", description = "审批入参")
public class AuditsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审批主表 ID 集合
     */
    @ApiModelProperty("审批主表 ID 集合")
    private List<String> patternLibraryIdList;

    /**
     * 审批提交内容
     */
    @ApiModelProperty("审批提交内容")
    private String comment;

    /**
     * 审批类型（1-通过 2-驳回）
     */
    @ApiModelProperty("审批类型（1-通过 2-驳回）")
    private Integer type;

}
