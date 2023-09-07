package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackTechSpecReferencesDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-06 13:17
 */
@Data
@ApiModel("资料包-工艺说明-引用 PackTechSpecReferencesDto")
public class PackTechSpecReferencesDto {

    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    @ApiModelProperty(value = "资料包id")
    private String targetForeignId;
    @ApiModelProperty(value = "资料包类型")
    private String targetPackType;
    @ApiModelProperty(value = "复制的项目,多个逗号分开")
    private String item;
}
