package com.base.sbc.module.patternmaking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：款式设计下发
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.dto.StyleSendDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-31 17:36
 */
@Data
@ApiModel("款式设计下发dto StyleSendDto ")
public class StyleSendDto {
    @ApiModelProperty(value = "打版id", required = true)
    @NotBlank(message = "打版id不能为空")
    private String id;

    private String supplierName;


}
