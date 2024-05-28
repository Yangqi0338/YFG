package com.base.sbc.module.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卞康
 * @date 2023/9/21 12:59:03
 * @mail 247967116@qq.com
 */
@Data
public class RemoveDto {
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "id集合")
    private String ids;

    @ApiModelProperty(value = "名称集合")
    private String names;

    @ApiModelProperty(value = "编码集合")
    private String codes;

    @ApiModelProperty(value = "模块名称")
    private String name;

    private String parentId;

}
