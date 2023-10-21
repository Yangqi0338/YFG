package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackCopyDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-06 14:41
 */

@Data
@ApiModel("资料包-复制dto PackCopyDto")
public class PackCopyDto {
    @ApiModelProperty(value = "源资料包id")
    private String sourceForeignId;
    @ApiModelProperty(value = "源资料包类型")
    private String sourcePackType;

    @ApiModelProperty(value = "目标资料包id")
    private String targetForeignId;
    @ApiModelProperty(value = "目标资料包类型")
    private String targetPackType;
    @ApiModelProperty(value = "复制的项目", example = "物料清单,尺寸表,工艺说明")
    private String item;
}
