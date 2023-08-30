package com.base.sbc.module.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述： IdDto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.dto.IdDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-03 16:39
 */
@Data
@ApiModel("IdDto")
public class IdDto {
    @ApiModelProperty(value = "id", required = true, example = "1675770402093846529")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "移动标记", required = false, example = "1：下 -1:上")
    private Integer moveFlag;
}
