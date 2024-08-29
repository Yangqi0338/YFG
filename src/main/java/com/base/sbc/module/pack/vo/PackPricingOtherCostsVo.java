package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 类描述：资料包-核价信息-其他费用Vo
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackPricingOtherCostsVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-10 14:07
 */
@Data
@ApiModel("资料包-核价信息-其他费用Vo PackPricingOtherCostsVo")
public class PackPricingOtherCostsVo extends PackPricingOtherCosts {
    private int index = 0;
    private String role = "加工厂报价";
    private String parentId;
}
