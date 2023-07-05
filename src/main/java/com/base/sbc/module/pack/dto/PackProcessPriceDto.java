package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackProcessPrice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-工序工价 dto
 *
 * @author lxl
 * @version 1.0
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 14:12:07
 */
@Data
@ApiModel("资料包-工序工价 PackProcessPriceDto")
public class PackProcessPriceDto extends PackProcessPrice {
    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;
}
