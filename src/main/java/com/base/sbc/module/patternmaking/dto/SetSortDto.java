package com.base.sbc.module.patternmaking.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 类描述：设置排序
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.SetSortDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 11:11
 */
@Data
@ApiModel("打版管理 设置排序dto SetSortDto ")
public class SetSortDto {
    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "sort", required = true)
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
