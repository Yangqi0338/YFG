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
 * @address com.base.sbc.module.pack.dto.PackCopyDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-09-06 14:41
 */

@Data
@ApiModel("资料包-复制dto PackCopyDto")
public class PackCopyDto {


    @ApiModelProperty(value = "源资料包id")
    @NotBlank(message = "源id为空")
    private String sourceForeignId;
    @ApiModelProperty(value = "覆盖标识:1覆盖")
    private String overlayFlag;
    @ApiModelProperty(value = "源资料包类型")
    @NotBlank(message = "源类型为空")
    private String sourcePackType;

    @ApiModelProperty(value = "目标资料包id")
    @NotBlank(message = "目标id为空")
    private String targetForeignId;
    @ApiModelProperty(value = "目标资料包类型")

    @NotBlank(message = "目标类型为空")
    private String targetPackType;
    @ApiModelProperty(value = "复制的项目", example = "物料清单,尺寸表,工艺说明")
    @NotBlank(message = "复制项目为空")
    private String item;

    @ApiModelProperty(value = "引用类型")
    private String specType;
}
