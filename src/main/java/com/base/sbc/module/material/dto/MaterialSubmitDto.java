package com.base.sbc.module.material.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Create : 2024/6/27 11:00
 **/
@Data
public class MaterialSubmitDto {


    @ApiModelProperty("类型（1:提交审核，2提交发布）")
    @NotEmpty(message = "提交类型不能为空")
    private String type;

    private List<String> idList;

}
