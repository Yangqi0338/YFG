package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.SetSampleBarCodeDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-17 15:44
 */

@Data
@AllArgsConstructor
@ApiModel("设置样衣条码Dto  SetSampleBarCodeDto ")
public class SetSampleBarCodeDto {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "样衣条码")
    @NotNull(message = "样衣条码不能为空")
    private String sampleBarCode;
}
