package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：设计bom关联配色
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackInfoAssociationDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-04 15:41
 */
@Data
@ApiModel("资料包-关联大货 PackInfoAssociationDto")
public class PackInfoAssociationDto {


    @ApiModelProperty(value = "资料包id")
    private String packId;
    @ApiModelProperty(value = "款式配色id")
    private String styleColorId;
    private String name;

}
