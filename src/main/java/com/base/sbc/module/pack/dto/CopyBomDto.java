package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.CopyBomDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-10-09 10:58
 */
@Data
@ApiModel("资料包-复制BOM CopyBomDto")
public class CopyBomDto extends CreatePackInfoByStyleDto {

    @ApiModelProperty(value = "源资料包id", example = "12333")
    String sourceForeignId;


    @ApiModelProperty(value = "源资料包类型", example = "12333")
    String sourcePackType;
}
