package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.SetKittingDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-05 14:39
 */
@Data
@ApiModel("设置齐套dto  SetKittingDto")
public class SetKittingDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "kitting", required = true, example = "1")
    @NotBlank(message = "是否齐套不能为空")
    private String kitting;
}
