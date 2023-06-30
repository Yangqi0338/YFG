package com.base.sbc.module.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：发送打板指令 dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.dto.SampleDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-09 15:41
 */
@Data
@ApiModel("发送打板指令 SendSampleMakingDto")
public class SendSampleMakingDto {
    @ApiModelProperty(value = "样衣id", example = "1656207820238086145")
    @NotBlank(message = "编号不能为空")
    private String id;
    @ApiModelProperty(value = "是否齐套:0未齐套，1已齐套", example = "0")
    @NotBlank(message = "是否齐套不能为空")
    private String kitting;
}
