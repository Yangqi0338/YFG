package com.base.sbc.module.pack.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-基础信息-查询Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackInfoDetailSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-14 10:50
 */
@Data
@ApiModel("资料包-基础信息-查询Dto PackInfoDetailSearchDto")
public class PackInfoDetailSearchDto {

    @ApiModelProperty(value = "id", required = true, example = "1675770402093846529")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型不能为空")
    private String packType;
}
