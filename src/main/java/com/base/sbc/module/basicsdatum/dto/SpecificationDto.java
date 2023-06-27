package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/27 11:12
 * @mail 247967116@qq.com
 */
@Data
public class SpecificationDto extends BaseDto {

    @ApiModelProperty(value = "类型")
    private String type;
}
