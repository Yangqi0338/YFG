package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：样衣设计下发
 * @address com.base.sbc.module.patternmaking.dto.SampleDesignSendDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-31 17:36
 * @version 1.0
 */
@Data
@ApiModel("样衣设计下发dto SampleDesignSendDto ")
public class SampleDesignSendDto {
    @ApiModelProperty(value = "打版id", required = true)
    @NotBlank(message = "打版id不能为空")
    private String id;


}
