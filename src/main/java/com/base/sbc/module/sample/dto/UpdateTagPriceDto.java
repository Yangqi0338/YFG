package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/*更新吊牌价*/
@Data
public class UpdateTagPriceDto {
    @ApiModelProperty(value = "款式id", example = "")
    @NotBlank(message = "编号不能为空")
    private String id;
    @ApiModelProperty(value = "吊牌价", example = "10")
    @NotBlank(message = "吊牌价")
    private String tagPrice;
}
