package com.base.sbc.module.pack.dto;


import com.base.sbc.config.enums.business.PackPricingOtherCostsItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：资料包-核价信息-其他费用分页查询dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.OtherCostsPageDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-10 14:45
 */
@Data
@ApiModel("资料包-核价信息-其他费用分页查询dto OtherCostsPageDto")
public class OtherCostsPageDto extends PackCommonPageSearchDto {
    @ApiModelProperty(value = "类型:包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    private PackPricingOtherCostsItemType costsItem;
}
