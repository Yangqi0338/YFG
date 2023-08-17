package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.CreatePackInfoDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-17 09:44
 */

@Data
@ApiModel("资料包-创建商品企划 CreatePackInfoDto")
public class CreatePackInfoByStyleDto {

    @ApiModelProperty(value = "款式id", example = "12333")
    @NotBlank(message = "款式id不能为空")
    private String id;
    @ApiModelProperty(value = "BOM名称", example = "123")
    private String name;

    @ApiModelProperty(value = "样板号", example = "123")
    private String patternNo;
}
