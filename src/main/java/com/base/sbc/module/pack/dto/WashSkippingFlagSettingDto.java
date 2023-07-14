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
 * @address com.base.sbc.module.pack.dto.WashSkippingFlagSettingDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-14 09:15
 */
@Data
@ApiModel("资料包-尺寸表-洗后尺寸设置 WashSkippingFlagSettingDto")
public class WashSkippingFlagSettingDto extends PackCommonSearchDto {

    @ApiModelProperty(value = "洗后尺寸设置:(0关闭,1开启)")
    @Pattern(regexp = "[01]", message = "值不对,0关闭,1开启")
    private String washSkippingFlag;

}
