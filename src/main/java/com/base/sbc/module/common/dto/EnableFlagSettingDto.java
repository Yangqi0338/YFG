package com.base.sbc.module.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 类描述：类描述：启用停用dto EnableFlagSettingDto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.common.dto.EnableFlagSettingDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-18 16:57
 */
@Data
@ApiModel("启用停用dto EnableFlagSettingDto")
public class EnableFlagSettingDto {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "状态:1启用,0未启用")
    @Pattern(regexp = "[01]", message = "值不对,1启用,0未启用")
    private String enableFlag;

    private String code;

}
