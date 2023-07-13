package com.base.sbc.module.pack.dto;


import com.base.sbc.module.pack.entity.PackPricing;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-核价信息-dto
 *
 * @author lixianglin
 * @version 1.0
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-10 13:49
 */
@Data
@ApiModel("资料包-核价信息 PackPricingDto")
public class PackPricingDto extends PackPricing {
    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;
}
