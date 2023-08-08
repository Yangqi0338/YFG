package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("查询明细 QueryDetailFabricDto")
public class QueryDetailFabricDto {
    /*id*/
    @NotBlank(message = "id必填")
    private String id;

    private String type;

}
