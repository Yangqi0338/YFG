package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@ApiModel("样衣归还")
public class SampleReturnDTO {

    @ApiModelProperty("样衣借还id")
    @NotEmpty(message = "样衣借还id不可为空")
    private List<String> sampleCirculateItemIds;

    @ApiModelProperty("备注")
    private String remarks;

}
