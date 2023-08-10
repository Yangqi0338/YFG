package com.base.sbc.module.style.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
/*修改配色颜色*/
public class UpdateColorDto {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "颜色库id")
    @NotBlank(message = "颜色库不能为空")
    private String colourLibraryId;
}
