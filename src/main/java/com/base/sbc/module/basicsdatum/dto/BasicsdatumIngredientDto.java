package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/8 11:05:31
 * @mail 247967116@qq.com
 */
@Data
public class BasicsdatumIngredientDto extends QueryDto{
    private String material;
    private String ingredient;
    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "排序")
    private String order;
}
