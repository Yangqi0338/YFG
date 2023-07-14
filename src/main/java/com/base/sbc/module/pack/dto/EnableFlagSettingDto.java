package com.base.sbc.module.pack.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.EnableFlagSettingDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-14 11:01
 */
@Data
@ApiModel("资料包-启用停用dto EnableFlagSettingDto")
public class EnableFlagSettingDto extends PackCommonSearchDto {

    @ApiModelProperty(value = "状态:1启用,0未启用")
    @Pattern(regexp = "[01]", message = "值不对,1启用,0未启用")
    private String enableFlag;

}
