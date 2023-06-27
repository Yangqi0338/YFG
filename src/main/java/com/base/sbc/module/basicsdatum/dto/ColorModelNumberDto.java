package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/6/26 10:21
 * @mail 247967116@qq.com
 */
@Data
public class ColorModelNumberDto extends BaseDto {
    @ApiModelProperty(value = "文件名称（分类）")
    private String fileName;
    @ApiModelProperty(value = "依赖名称")
    private String mat2ndCategoryName;
}
